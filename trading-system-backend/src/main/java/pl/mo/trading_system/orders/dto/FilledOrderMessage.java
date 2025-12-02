package pl.mo.trading_system.orders.dto;

public record FilledOrderMessage(
        String isin,
        String name,
        long accountId,
        Long executionDate
) {

}
