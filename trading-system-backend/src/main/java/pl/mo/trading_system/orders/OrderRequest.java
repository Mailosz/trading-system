package pl.mo.trading_system.orders;

import lombok.Builder;

@Builder
public record OrderRequest(
        String isin,
        int quantity,
        OrderType orderType,
        Double priceLimit,
        Long expiresAt
) {

}
