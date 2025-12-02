package pl.mo.trading_system.tickers.dto;

import lombok.Getter;
import lombok.Setter;
import pl.mo.trading_system.tickers.model.TickerEntity;

@Getter
@Setter
public class TickerDTO {

    String isin;
    String name;
    String ticker;
    String tradeCurrency;
    String mic;
    Double price;

    public static TickerDTO fromEntity(TickerEntity ticker) {
        var dto = new TickerDTO();

        dto.setIsin(ticker.getIsin());
        dto.setTicker(ticker.getTicker());
        dto.setName(ticker.getName());
        dto.setTradeCurrency(ticker.getTradeCurrency());
        dto.setMic(ticker.getMic());

        return dto;
    }
}
