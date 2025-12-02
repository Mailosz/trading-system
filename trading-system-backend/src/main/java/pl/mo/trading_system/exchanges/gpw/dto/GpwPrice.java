package pl.mo.trading_system.exchanges.gpw.dto;

public record GpwPrice(
    String isin,
    double price
) {}
