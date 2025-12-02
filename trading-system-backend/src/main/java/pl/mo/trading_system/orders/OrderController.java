package pl.mo.trading_system.orders;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.web.bind.annotation.*;
import pl.mo.trading_system.orders.dto.OrderDetailsDTO;
import pl.mo.trading_system.orders.dto.OrderListItemDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @GetMapping("/api/orders")
    public List<OrderListItemDTO> getOrdersList() {
        return orderService.getUserOrders().stream().map(OrderListItemDTO::fromEntity).toList();
    }

    @GetMapping("/api/orders/{id}")
    public Optional<OrderDetailsDTO> getOrderDetails(@PathVariable("id")UUID id) {
        return orderService.findById(id).map(OrderDetailsDTO::fromEntity);
    }

    @PostMapping("/api/orders/place")
    public OrderEntity placeOrder(@RequestBody OrderRequest order) {
        return orderService.placeOrder(order);
    }

}
