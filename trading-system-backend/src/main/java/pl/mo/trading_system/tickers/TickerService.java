package pl.mo.trading_system.tickers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import pl.mo.trading_system.gpw.GpwConnector;
import pl.mo.trading_system.gpw.GpwPrice;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickerService {

    Map<String, Double> pricesMap = new HashMap<>();

    final GpwConnector gpwConnector;
    final TickerRepository tickerRepository;

    @PostConstruct
    public void init() {

        updateTickers();
        updatePrices();

    }

    @Scheduled(fixedRateString = "${gpw.priceUpdateRate}")
    void updateInstrumentPrices() {
        log.info("Scheduled prices update");
        updatePrices();
    }


    public void updateTickers() {

        try {
            var tickers = gpwConnector.getTickers().stream()
                    .map((gpwTicker -> TickerEntity.builder()
                            .isin(gpwTicker.isin())
                            .ticker(gpwTicker.ticker())
                            .name(gpwTicker.name())
                            .tradeCurrency(gpwTicker.tradeCurrency())
                            .mic(gpwTicker.mic())
                            .build()
                    ))
                    .toList();

            tickerRepository.saveAll(tickers);

        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }

    }

    void updatePrices() {
        try {
            this.pricesMap = gpwConnector.getPrices().stream()
                    .collect(Collectors.toMap(GpwPrice::isin, GpwPrice::price));
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }
    }


    public List<TickerEntity> findTickersByTicker(String name) {
        return tickerRepository.findByTickerContainingIgnoreCase(name);
    }

    public Optional<Double> getTickerPrice(String isin) {
        return Optional.of(pricesMap.get(isin));
    }
}
