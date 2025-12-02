package pl.mo.trading_system.orders;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @GetMapping("/api/orders")
    public List<OrderEntity> getOrdersList() {
        return orderService.getUserOrders();
    }

    @GetMapping("/api/orders/{id}")
    public Optional<OrderEntity> getOrderDetails(@PathVariable("id")UUID id) {
        return orderService.findById(id);
    }

    @PostMapping("/api/orders/place")
    public OrderEntity placeOrder(@RequestBody OrderRequest order) {
        return orderService.placeOrder(order);
    }

}
