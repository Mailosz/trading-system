package pl.mo.trading_system.gpw;

import lombok.Builder;
import pl.mo.trading_system.orders.OrderType;

@Builder
public record GpwOrderRequest(
        long accountId,
        String isin,
        String side,
        String tradeCurrency,
        int quantity,
        Long expiresAt,
        OrderType orderType,
        Double limitPrice
) {
}
