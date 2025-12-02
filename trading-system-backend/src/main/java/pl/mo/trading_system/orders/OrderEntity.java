package pl.mo.trading_system.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    String isin;

    String tickerName;

    String currency;

    Double executionPrice;

    int quantity;

    long registrationTime;

    Long filledDate;

    double commission;

}
