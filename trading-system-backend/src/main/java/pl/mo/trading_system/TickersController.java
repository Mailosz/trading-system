package pl.mo.trading_system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mo.trading_system.instruments.Instrument;
import pl.mo.trading_system.instruments.InstrumentService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequiredArgsConstructor
public class TickersController {

    final InstrumentService instrumentService;

    @PostMapping("/api/tickers/search")
    public List<Instrument> searchForTickers(@RequestBody String searchstring) {
        return instrumentService.findInstrumentsByName(searchstring);
    }
}
