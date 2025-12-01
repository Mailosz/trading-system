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

    final OrderRepository orderRepository;
    final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderEntity> getOrdersList() {

        return orderRepository.findAll();

    }

    @GetMapping("/orders/{id}")
    public Optional<OrderEntity> getOrderDetails(@PathVariable("id")UUID id) {
        return orderRepository.findById(id);
    }

    @PostMapping("/orders/place")
    public OrderEntity placeOrder(@RequestBody OrderRequest order) {
        return orderService.placeOrder(order);
    }

}
