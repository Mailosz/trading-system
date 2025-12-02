package pl.mo.trading_system.exchanges.gpw.dto;

public record GpwStatusResponse(
        long orderId,
        String status,
        String isin,
        String side,
        String tradeCurrency,
        int quantity,
        Double executionPrice,
        long registrationTime,
        long executedTime
) {
}
