package pl.mo.trading_system.tickers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticker {

    @Id
    String isin;

    String name;
    String ticker;
    String tradeCurrency;
    String mic;
}
