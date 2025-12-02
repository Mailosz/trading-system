package pl.mo.trading_system.tickers.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tickers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerEntity {

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
