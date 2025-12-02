package pl.mo.trading_system.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    long orderId;

    OrderStatus status;

    long registrationTime;

    String isin;

    int quantity;

    double priceLimit;
}
