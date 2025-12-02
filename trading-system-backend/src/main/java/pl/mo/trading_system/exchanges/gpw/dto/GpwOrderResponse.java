package pl.mo.trading_system.exchanges.gpw.dto;

public record GpwOrderResponse(
        long orderId,
        String status,
        long registrationTime
) {
}
