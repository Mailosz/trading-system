package pl.mo.trading_system.orders;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import pl.mo.trading_system.AccountService;
import pl.mo.trading_system.exchanges.StockExchangeService;
import pl.mo.trading_system.exchanges.gpw.GpwConnector;
import pl.mo.trading_system.exchanges.gpw.dto.GpwOrderRequest;
import pl.mo.trading_system.orders.dto.OrderRequest;
import pl.mo.trading_system.orders.model.OrderEntity;
import pl.mo.trading_system.orders.model.OrderRepository;
import pl.mo.trading_system.orders.model.OrderStatus;
import pl.mo.trading_system.orders.model.OrderType;
import pl.mo.trading_system.tickers.TickerRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    final StockExchangeService stockExchangeService;
    final OrderRepository orderRepository;
    final TickerRepository tickerRepository;
    final AccountService accountService;
    final FilledOrdersService filledOrdersService;

    public OrderEntity placeOrder(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);

        var ticker = tickerRepository.findByIsin(orderRequest.isin());

        if (ticker.isEmpty()) {
            throw new RuntimeException("No ticker");
        }

        GpwOrderRequest request = GpwOrderRequest.builder()
                .orderType(orderRequest.orderType())
                .isin(ticker.get().getIsin())
                .tradeCurrency(ticker.get().getTradeCurrency())
                .accountId(accountService.getCurrentAccountId())
                .limitPrice(orderRequest.orderType() == OrderType.LMT ? orderRequest.priceLimit() : null)
                .quantity(orderRequest.quantity())
                .side("BUY")
                .expiresAt(orderRequest.expiresAt())
                .build();

        var order = new OrderEntity();
        order.setAccountId(accountService.getCurrentAccountId());
        order.setIsin(ticker.get().getIsin());
        order.setQuantity(orderRequest.quantity());

        var response = stockExchangeService.getConnectorForMic(ticker.get().getMic()).flatMap(connector -> connector.placeOrder(request));
        if (response.isPresent()) {

            order.setOrderId(response.get().orderId());
            order.setStatus(OrderStatus.valueOf(response.get().status().toUpperCase(Locale.ROOT)));
            order.setRegistrationTime(response.get().registrationTime());
            orderRepository.save(order);

            return order;
        } else {
            throw new RuntimeException("No response from stok exchange");
        }
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest.orderType() == null) {
            throw new RuntimeException("Wrong order type");
        }

        if (orderRequest.quantity() < 1) {
            throw new RuntimeException("Quantity must be > 0");
        }

        if (orderRequest.orderType() == OrderType.LMT && orderRequest.priceLimit() <= 0.0) {
            throw new RuntimeException("Price limit must be >= 0");
        }
    }

    public List<OrderEntity> getUserOrders() {
        return orderRepository.findAllByAccountId(accountService.getCurrentAccountId());
    }

    public Optional<OrderEntity> findById(UUID id) {
        return orderRepository.findByIdAndAccountId(id, accountService.getCurrentAccountId());
    }


    public void checkOrderStatus(OrderEntity entity) {

        try {
            var mic = entity.getTicker().getMic();
            var connector = stockExchangeService.getConnectorForMic(mic);
            connector.orElseThrow(()->new RuntimeException("No connector for mic " + mic)).getOrderStatus(Long.toString(entity.getOrderId())).ifPresent(statusResponse -> {
                if (entity.getStatus() != statusResponse.status()) {

                    if (statusResponse.status() == OrderStatus.FILLED) {
                        entity.setExecutionPrice(statusResponse.executionPrice());
                        entity.setFilledDate(statusResponse.executedTime());
                        entity.setQuantity(statusResponse.quantity());
                        entity.setCommission(statusResponse.commission());

                        filledOrdersService.handleOrderFilled(entity);
                    }

                    entity.setStatus(statusResponse.status());
                    orderRepository.save(entity);
                }
            });
        } catch (RuntimeException ex) {
            log.error("Error during checkOrderStatus", ex);
        }

    }

    @Scheduled(fixedDelayString = "${updateOrderStatuses.delay}")
    void updateOrderStatuses() {
        log.info("Update order statuses");
        orderRepository.findAllByStatusWithTicker(OrderStatus.SUBMITTED).forEach(this::checkOrderStatus);
    }

}
