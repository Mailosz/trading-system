package pl.mo.trading_system.orders.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.mo.trading_system.tickers.model.Ticker;

import java.util.UUID;

@Entity(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "account_id", nullable = false)
    long accountId;

    @Column(name = "order_id", nullable = false)
    long orderId;

    @Column(name = "status", nullable = false)
    OrderStatus status;

    @Column(name = "isin", nullable = false)
    String isin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "isin", insertable = false, updatable = false)
    Ticker ticker;

    @Column(name = "execution_price")
    Double executionPrice;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "registration_time")
    long registrationTime;

    @Column(name = "filled_date")
    Long filledDate;

    @Column(name = "commision")
    Double commission;

}
