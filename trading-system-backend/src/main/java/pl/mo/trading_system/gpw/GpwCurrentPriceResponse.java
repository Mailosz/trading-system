package pl.mo.trading_system.gpw;

import java.util.List;

public record GpwCurrentPriceResponse(
        List<GpwPrice> results
) {
}
