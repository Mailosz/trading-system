package pl.mo.trading_system.tickers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.mo.trading_system.tickers.dto.PriceDTO;
import pl.mo.trading_system.tickers.model.Ticker;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TickersController {

    final TickerService tickerService;

    @PostMapping("/api/tickers/search")
    public List<Ticker> searchForTickers(@RequestBody String searchstring) {
        return tickerService.findTickersByName(searchstring);
    }

    @GetMapping("/api/tickers/price/{isin}")
    public PriceDTO getTickerPrice(@PathVariable("isin") String isin) {
        return tickerService.getTickerPrice(isin).map((price) -> new PriceDTO(isin, price)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
