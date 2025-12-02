package pl.mo.trading_system.tickers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TickersController {

    final TickerService tickerService;

    @PostMapping("/api/tickers/search")
    public List<Ticker> searchForTickers(@RequestBody String searchstring) {
        return tickerService.findInstrumentsByName(searchstring);
    }

}
