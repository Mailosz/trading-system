package pl.mo.trading_system.tickers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TickersController {

    final InstrumentService instrumentService;

    @PostMapping("/api/tickers/search")
    public List<Instrument> searchForTickers(@RequestBody String searchstring) {
        return instrumentService.findInstrumentsByName(searchstring);
    }

}
