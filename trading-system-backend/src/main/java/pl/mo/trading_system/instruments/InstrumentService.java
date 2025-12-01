package pl.mo.trading_system.instruments;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import pl.mo.trading_system.gpw.GpwConnector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    Map<String, Instrument> instrumentsMap = new HashMap<>();

    final GpwConnector gpwConnector;

    @PostConstruct
    public void init() {

        updateInstruments();
        updatePrices();

    }

    @Scheduled(fixedRateString = "${gpw.priceUpdateRate}")
    void updateInstrumentPrices() {
        System.out.println("Scheduled prices update");
        if (!instrumentsMap.isEmpty()) {
            updatePrices();
        }

    }


    void updateInstruments() {

        try {
            var tickers = gpwConnector.getTickers();

            this.instrumentsMap = tickers.stream().map((ticker) -> {
                var instrument = new Instrument();
                instrument.setIsin(ticker.isin());
                instrument.setMic(ticker.mic());
                instrument.setTradeCurrency(ticker.tradeCurrency());
                instrument.setName(ticker.name());

                return instrument;
            }).collect(Collectors.toMap(Instrument::getIsin, (i) -> i));
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }

    }

    void updatePrices() {
        try {
            var prices = gpwConnector.getPrices();

            for (var price : prices) {

                var instrument = this.instrumentsMap.get(price.isin());
                if (instrument != null) {
                    instrument.setPrice(price.price());
                }

            }
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }
    }


    public List<Instrument> findInstrumentsByName(String name) {
        String lowerCaseName = name.toLowerCase(Locale.ROOT);
        return instrumentsMap.values().stream().filter((i) -> i.getName().toLowerCase(Locale.ROOT).contains(lowerCaseName)).toList();
    }

}
