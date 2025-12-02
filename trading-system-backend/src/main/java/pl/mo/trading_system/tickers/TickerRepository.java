package pl.mo.trading_system.tickers;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.mo.trading_system.tickers.model.Ticker;

import java.util.List;
import java.util.Optional;

public interface TickerRepository extends JpaRepository<Ticker, String> {
    List<Ticker> findByNameContainingIgnoreCase(String name);

    Optional<Ticker> findByIsin(String isin);
}
