package pl.mo.trading_system.exchanges;


import org.springframework.stereotype.Service;
import pl.mo.trading_system.exchanges.gpw.GpwConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockExchangeService {

    Map<String, StockExchangeConnector> exchangesMap = new HashMap<>();

    public StockExchangeService(GpwConnector gpwConnector) {

        exchangesMap.put("XWAR", gpwConnector);

    }

    public Optional<StockExchangeConnector> getConnectorForMic(String mic) {
        return Optional.of(exchangesMap.get(mic));
    }

    public List<StockExchangeConnector> getAllConnectors() {
        return exchangesMap.values().stream().toList();
    }

}
