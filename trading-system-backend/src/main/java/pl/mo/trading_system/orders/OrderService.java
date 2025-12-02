package pl.mo.trading_system.orders;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mo.trading_system.gpw.GpwConnector;
import pl.mo.trading_system.gpw.GpwOrderRequest;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderService {

    final GpwConnector gpwConnector;
    final OrderRepository orderRepository;

    public OrderEntity placeOrder(OrderRequest orderRequest) {


        GpwOrderRequest request = GpwOrderRequest.builder()
                .orderType(orderRequest.orderType())
                .isin(orderRequest.isin())
                .tradeCurrency("PLN")
                .accountId(123)
                .quantity(orderRequest.quantity())
                .side("BUY")
                .expiresAt(orderRequest.expiresAt())
                .build();

        var response = gpwConnector.placeOrder(request);

        if (response.isPresent()) {
            var order = new OrderEntity();

            order.setOrderId(response.get().orderId());
            order.setStatus(OrderStatus.valueOf(response.get().status().toUpperCase(Locale.ROOT)));
            order.setIsin(orderRequest.isin());
            order.setQuantity(orderRequest.quantity());
            order.setPriceLimit(orderRequest.priceLimit());
            //TODO: populate

            orderRepository.save(order);

            return order;
        } else {
            throw new RuntimeException("TODO");
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
