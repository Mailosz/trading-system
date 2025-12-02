package pl.mo.trading_system.exchanges.gpw.dto;

public record GpwTicker (
    String name,
    String ticker,
    String isin,
    String tradeCurrency,
    String mic,
    Double price
    ){}
