package pl.mo.trading_system.tickers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.mo.trading_system.tickers.dto.PriceDTO;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TickersController {

    final TickerService tickerService;

    @PostMapping("/api/tickers/search")
    public List<TickerEntity> searchForTickers(@RequestBody String searchstring) {
        return tickerService.findTickersByTicker(searchstring);
    }

    @GetMapping("/api/tickers/price/{mic}/{isin}")
    public PriceDTO getTickerPrice(@PathVariable("mic") String mic, @PathVariable("isin") String isin) {
        return tickerService.getTickerPrice(mic, isin).map((price) -> new PriceDTO(isin, price)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
