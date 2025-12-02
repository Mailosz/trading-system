package pl.mo.trading_system.orders;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mo.trading_system.gpw.GpwConnector;
import pl.mo.trading_system.gpw.GpwOrderRequest;
import pl.mo.trading_system.tickers.Ticker;
import pl.mo.trading_system.tickers.TickerService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderService {

    final GpwConnector gpwConnector;
    final OrderRepository orderRepository;
    final TickerService tickerService;

    public OrderEntity placeOrder(OrderRequest orderRequest) {

        if (orderRequest.quantity() < 1) {
            throw new RuntimeException("Quantity must be > 0");
        }

        if (orderRequest.priceLimit() <= 0.0) {
            throw new RuntimeException("Price limit must be >= 0");
        }

        var ticker = tickerService.findTickerByIsin(orderRequest.isin());

        if (ticker.isEmpty()) {
            throw new RuntimeException("No tiker");
        }

        GpwOrderRequest request = GpwOrderRequest.builder()
                .orderType(orderRequest.orderType())
                .isin(ticker.get().getIsin())
                .tradeCurrency(ticker.get().getTradeCurrency())
                .accountId(123)
                .quantity(orderRequest.quantity())
                .side("BUY")
                .expiresAt(orderRequest.expiresAt())
                .build();

        var order = new OrderEntity();
        order.setIsin(ticker.get().getIsin());
        order.setTickerName(ticker.get().getName());
        order.setCurrency(ticker.get().getTradeCurrency());
        order.setPriceLimit(orderRequest.priceLimit());
        order.setQuantity(orderRequest.quantity());
        order.setCommission(computeCommission(ticker.get().getMic(), orderRequest.priceLimit(), orderRequest.quantity()));

        var response = gpwConnector.placeOrder(request);
        if (response.isPresent()) {

            order.setOrderId(response.get().orderId());
            order.setStatus(OrderStatus.valueOf(response.get().status().toUpperCase(Locale.ROOT)));
            order.setRegistrationTime(response.get().registrationTime());
            orderRepository.save(order);

            return order;
        } else {
            throw new RuntimeException("TODO");
        }
    }

    private double computeCommission(String mic, Double price, int quantity) {

        if ("XWAR".equals(mic)) {
            return Math.max(5, (price * quantity) * 0.03);
        } else {
            return Math.max(10, (price * quantity) * 0.02);
        }

    }


    public void checkOrderStatus(OrderEntity entity) {

        var response = gpwConnector.getOrderStatus(Long.toString(entity.getOrderId()));

        response.ifPresent(gpwStatusResponse -> {
            entity.setStatus(OrderStatus.valueOf(gpwStatusResponse.status()));
                    //TODO:
        });

    }

}
