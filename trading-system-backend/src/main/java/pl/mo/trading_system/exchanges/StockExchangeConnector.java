package pl.mo.trading_system.exchanges;

import pl.mo.trading_system.exchanges.gpw.dto.GpwOrderRequest;
import pl.mo.trading_system.tickers.dto.PriceDTO;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface StockExchangeConnector {

    Stream<TickerEntity> getTickers();
    Optional<Double> getPrice(String isin);
    Optional<StatusResponse> getOrderStatus(String id);
    Optional<OrderResponse> placeOrder(GpwOrderRequest request);

}
