package pl.mo.trading_system.tickers.dto;

public record PriceDTO(
        String isin,
        double price
) {
}
