package pl.mo.trading_system.tickers.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "tickers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticker {

    @Id
    String isin;

    @Column(name = "name")
    String name;

    @Column(name = "ticker")
    String ticker;

    @Column(name = "trade_currency")
    String tradeCurrency;

    @Column(name = "mic")
    String mic;
}
