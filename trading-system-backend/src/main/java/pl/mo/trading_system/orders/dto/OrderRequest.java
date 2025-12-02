package pl.mo.trading_system.orders.dto;

import lombok.Builder;
import pl.mo.trading_system.orders.model.OrderType;

@Builder
public record OrderRequest(
        String isin,
        int quantity,
        OrderType orderType,
        Double priceLimit,
        Long expiresAt
) {

}
