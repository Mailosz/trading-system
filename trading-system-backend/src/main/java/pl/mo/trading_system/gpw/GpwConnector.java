package pl.mo.trading_system.gpw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class GpwConnector {

    RestTemplate restTemplate;

    URI baseUri;

    public GpwConnector(@Value("${gpw.baseUrl}") String baseUrl, RestTemplateBuilder restTemplateBuilder) {
        this.baseUri = URI.create(baseUrl);
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<GpwTicker> getTickers() {

        var response = restTemplate.getForEntity(baseUri.resolve("gpw/tickers"), GpwTickersResponse.class);

        var body = response.getBody();

        if (body == null) {
            return List.of();
        }

        return body.tickers();
    }

    public List<GpwPrice> getPrices() {
        var response = restTemplate.getForEntity(baseUri.resolve("gpw/prices/current"), GpwCurrentPriceResponse.class);

        var body = response.getBody();

        if (body == null) {
            return List.of();
        }

        return body.results();
    }

    public Optional<GpwOrderResponse> placeOrder(GpwOrderRequest request) {
        var response = restTemplate.postForEntity(baseUri.resolve("gpw/orders"), request, GpwOrderResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            return Optional.of(body);
        } else {
            return Optional.empty();
        }

    }

    public Optional<GpwStatusResponse> getOrderStatus(String id) {
        var response = restTemplate.getForEntity(baseUri.resolve("gpw/order").resolve(id), GpwStatusResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            return Optional.of(body);
        } else {
            return Optional.empty();
        }
    }

}
