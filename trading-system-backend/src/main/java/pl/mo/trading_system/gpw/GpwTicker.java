package pl.mo.trading_system.gpw;

public record GpwTicker (
    String name,
    String ticker,
    String isin,
    String tradeCurrency,
    String mic,
    Double price
    ){}
