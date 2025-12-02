package pl.mo.trading_system.exchanges;

import pl.mo.trading_system.orders.model.OrderStatus;


public record StatusResponse (
        OrderStatus status,
        double executionPrice,
        long executedTime,
        int quantity,
        Double commission

) {



}
