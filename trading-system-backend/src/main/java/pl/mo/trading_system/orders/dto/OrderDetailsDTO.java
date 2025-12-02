package pl.mo.trading_system.orders.dto;

import lombok.Getter;
import lombok.Setter;
import pl.mo.trading_system.orders.model.OrderEntity;
import pl.mo.trading_system.orders.model.OrderStatus;

import java.util.UUID;

@Setter
@Getter
public class OrderDetailsDTO {
    UUID id;
    long accountId;
    long orderId;
    OrderStatus status;
    String isin;
    String tickerName;
    String currency;
    Double executionPrice;
    int quantity;
    long registrationTime;
    Long filledDate;
    Double commission;

    public static OrderDetailsDTO fromEntity(OrderEntity order) {
        var dto = new OrderDetailsDTO();

        dto.setId(order.getId());
        dto.setAccountId(order.getAccountId());
        dto.setOrderId(order.getOrderId());
        dto.setStatus(order.getStatus());
        dto.setIsin(order.getTicker().getIsin());
        dto.setTickerName(order.getTicker().getName());
        dto.setCurrency(order.getTicker().getTradeCurrency());
        dto.setExecutionPrice(order.getExecutionPrice());
        dto.setQuantity(order.getQuantity());
        dto.setRegistrationTime(order.getRegistrationTime());
        dto.setFilledDate(order.getFilledDate());
        dto.setCommission(order.getCommission());

        return dto;
    }
}
