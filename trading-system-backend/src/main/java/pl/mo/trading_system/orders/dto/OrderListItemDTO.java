package pl.mo.trading_system.orders.dto;

import lombok.Getter;
import lombok.Setter;
import pl.mo.trading_system.orders.model.OrderEntity;
import pl.mo.trading_system.orders.model.OrderStatus;

import java.util.UUID;

@Getter
@Setter
public class OrderListItemDTO {
    UUID id;
    long accountId;
    long orderId;
    OrderStatus status;
    String isin;
    Double executionPrice;
    int quantity;

    public static OrderListItemDTO fromEntity(OrderEntity order) {
        var dto = new OrderListItemDTO();

        dto.setId(order.getId());
        dto.setAccountId(order.getAccountId());
        dto.setOrderId(order.getOrderId());
        dto.setStatus(order.getStatus());
        dto.setIsin(order.getIsin());
        dto.setQuantity(order.getQuantity());
        dto.setExecutionPrice(order.getExecutionPrice());

        return dto;
    }
}
