package pl.mo.trading_system.orders;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mo.trading_system.gpw.GpwConnector;
import pl.mo.trading_system.gpw.GpwOrderRequest;

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
            //TODO: populate

            orderRepository.save(order);

            return order;
        } else {
            throw new RuntimeException("TODO");
        }
    }


    public void checkOrderStatus(OrderEntity entity) {

        var response = gpwConnector.getOrderStatus(entity.getOrderId());

        response.ifPresent(gpwStatusResponse -> {
            entity.setStatus(OrderStatus.valueOf(gpwStatusResponse.status()));
                    //TODO:
        });

    }

}
