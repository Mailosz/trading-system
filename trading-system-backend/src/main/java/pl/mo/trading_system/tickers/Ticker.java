package pl.mo.trading_system.tickers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticker {

    String name;
    String ticker;
    String isin;
    String tradeCurrency;
    String mic;
    Double price;
}
