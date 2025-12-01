package pl.mo.trading_system.gpw;

public record GpwOrderResponse(
        long orderId,
        String status,
        long registrationTime
) {
}
