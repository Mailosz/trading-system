package pl.mo.trading_system.tickers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import pl.mo.trading_system.exchanges.StockExchangeConnector;
import pl.mo.trading_system.exchanges.StockExchangeService;
import pl.mo.trading_system.exchanges.gpw.GpwConnector;
import pl.mo.trading_system.tickers.dto.PriceDTO;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickerService {

    final StockExchangeService stockExchangeService;
    final TickerRepository tickerRepository;

//    @PostConstruct
//    public void init() {
//
//
//        updateTickers();
//
//    }



    @Scheduled(fixedRateString = "${tickers.updateRate}")
    public void updateTickers() {

        try {

            stockExchangeService.getAllConnectors().forEach((connector) -> {
                var tickers = connector.getTickers().toList();
                tickerRepository.saveAll(tickers);
            });

        } catch (ResourceAccessException ex) {
            log.error("Error during update tickers", ex);
        }

    }




    public List<TickerEntity> findTickersByTicker(String name) {
        return tickerRepository.findByTickerContainingIgnoreCase(name);
    }

    public Optional<Double> getTickerPrice(String mic, String isin) {
        return stockExchangeService.getConnectorForMic(mic).flatMap(connector -> connector.getPrice(isin));
    }
}
