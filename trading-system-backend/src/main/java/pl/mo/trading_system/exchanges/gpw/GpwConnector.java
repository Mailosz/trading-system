package pl.mo.trading_system.exchanges.gpw;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pl.mo.trading_system.exchanges.OrderResponse;
import pl.mo.trading_system.exchanges.StatusResponse;
import pl.mo.trading_system.exchanges.StockExchangeConnector;
import pl.mo.trading_system.exchanges.gpw.dto.*;
import pl.mo.trading_system.orders.model.OrderStatus;
import pl.mo.trading_system.tickers.dto.PriceDTO;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class GpwConnector implements StockExchangeConnector {

    Map<String, Double> pricesMap = new HashMap<>();

    RestTemplate restTemplate;

    URI baseUri;

    public GpwConnector(@Value("${gpw.baseUrl}") String baseUrl, RestTemplateBuilder restTemplateBuilder) {
        this.baseUri = URI.create(baseUrl);
        this.restTemplate = restTemplateBuilder.build();
    }


//    @PostConstruct
//    public void init() {
//        updatePrices();
//    }

    @Scheduled(fixedRateString = "${gpw.priceUpdateRate}")
    void updateInstrumentPrices() {
        log.info("Scheduled prices update");
        updatePrices();
    }

    void updatePrices() {
        try {
            this.pricesMap = getPrices()
                    .collect(Collectors.toMap(PriceDTO::isin, PriceDTO::price));
        } catch (ResourceAccessException ex) {
            log.error("Error during update prices ", ex);
        }
    }


    public Stream<TickerEntity> getTickers() {

        var response = restTemplate.getForEntity(baseUri.resolve("gpw/tickers"), GpwTickersResponse.class);

        var body = response.getBody();

        if (body == null) {
            return Stream.empty();
        }

        return body.tickers().stream()
                .map(gpwTicker -> TickerEntity.builder()
                        .isin(gpwTicker.isin())
                        .ticker(gpwTicker.ticker())
                        .name(gpwTicker.name())
                        .tradeCurrency(gpwTicker.tradeCurrency())
                        .mic(gpwTicker.mic())
                        .build()
                );
    }

    public Optional<Double> getPrice(String isin) {
        return Optional.of(pricesMap.get(isin));
    }

    Stream<PriceDTO> getPrices() {
        var response = restTemplate.getForEntity(baseUri.resolve("gpw/prices/current"), GpwCurrentPriceResponse.class);

        var body = response.getBody();

        if (body == null) {
            return Stream.empty();
        }

        return body.results().stream().map(price -> new PriceDTO(price.isin(), price.price()));
    }

    public Optional<OrderResponse> placeOrder(GpwOrderRequest request) {
        var response = restTemplate.postForEntity(baseUri.resolve("gpw/orders"), request, GpwOrderResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            return Optional.ofNullable(body).map((gpwOrderResponse -> new OrderResponse(
                    gpwOrderResponse.orderId(),
                    gpwOrderResponse.status(),
                    gpwOrderResponse.registrationTime()
            )));
        } else {
            return Optional.empty();
        }

    }

    public Optional<StatusResponse> getOrderStatus(String id) {
        var response = restTemplate.getForEntity(baseUri.resolve("gpw/order/").resolve(id), GpwStatusResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            return Optional.ofNullable(body).map(gpwStatusResponse -> new StatusResponse(
                        OrderStatus.valueOf(gpwStatusResponse.status().toUpperCase(Locale.ROOT)),
                        gpwStatusResponse.executionPrice(),
                        gpwStatusResponse.executedTime(),
                        gpwStatusResponse.quantity(),
                        computeCommission(gpwStatusResponse.executionPrice(), gpwStatusResponse.quantity())
                    ));
        } else {
            return Optional.empty();
        }
    }

    private double computeCommission(Double price, int quantity) {

        return Math.max(5, (price * quantity) * 0.03);

    }

}
