package pl.mo.trading_system.tickers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import pl.mo.trading_system.gpw.GpwConnector;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TickerService {

    Map<String, Ticker> tickersMap = new HashMap<>();

    final GpwConnector gpwConnector;

    @PostConstruct
    public void init() {

        updateInstruments();
        updatePrices();

    }

    @Scheduled(fixedRateString = "${gpw.priceUpdateRate}")
    void updateInstrumentPrices() {
        System.out.println("Scheduled prices update");
        if (!tickersMap.isEmpty()) {
            updatePrices();
        }

    }


    void updateInstruments() {

        try {
            var tickers = gpwConnector.getTickers();

            this.tickersMap = tickers.stream().map((ticker) -> {
                var instrument = new Ticker();
                instrument.setIsin(ticker.isin());
                instrument.setMic(ticker.mic());
                instrument.setTradeCurrency(ticker.tradeCurrency());
                instrument.setName(ticker.name());

                return instrument;
            }).collect(Collectors.toMap(Ticker::getIsin, (i) -> i));
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }

    }

    void updatePrices() {
        try {
            var prices = gpwConnector.getPrices();

            for (var price : prices) {

                var instrument = this.tickersMap.get(price.isin());
                if (instrument != null) {
                    instrument.setPrice(price.price());
                }

            }
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }
    }


    public List<Ticker> findInstrumentsByName(String name) {
        String lowerCaseName = name.toLowerCase(Locale.ROOT);
        return tickersMap.values().stream().filter((i) -> i.getName().toLowerCase(Locale.ROOT).contains(lowerCaseName)).toList();
    }

    public List<Ticker> getAllInstruments() {

        return tickersMap.values().stream().toList();
    }

    public Optional<Ticker> findTickerByIsin(String isin) {
        return Optional.ofNullable(tickersMap.get(isin));
    }
}
