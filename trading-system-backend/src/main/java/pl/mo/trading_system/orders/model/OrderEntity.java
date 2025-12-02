package pl.mo.trading_system.orders.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.mo.trading_system.tickers.Ticker;

import java.util.UUID;

@Entity
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    long accountId;

    long orderId;

    OrderStatus status;

    @Column(name = "isin", nullable = false)
    String isin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "isin", insertable = false, updatable = false)
    Ticker ticker;

    Double executionPrice;

    int quantity;

    long registrationTime;

    Long filledDate;

    Double commission;

}
