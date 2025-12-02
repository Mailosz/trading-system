package pl.mo.trading_system.exchanges.gpw.dto;

import lombok.Builder;
import pl.mo.trading_system.orders.model.OrderType;

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
