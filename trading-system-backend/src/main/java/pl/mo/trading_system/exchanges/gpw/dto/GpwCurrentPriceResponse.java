package pl.mo.trading_system.exchanges.gpw.dto;

import java.util.List;

public record GpwCurrentPriceResponse(
        List<GpwPrice> results
) {
}
