package pl.mo.trading_system.orders;

import lombok.Builder;
import pl.mo.trading_system.tickers.Ticker;

@Builder
public record OrderRequest(
        String isin,
        int quantity,
        OrderType orderType,
        Double priceLimit,
        Long expiresAt
) {

}
