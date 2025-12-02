package pl.mo.trading_system.exchanges;

public record OrderResponse(
        long orderId,
        String status,
        long registrationTime
) {
}
