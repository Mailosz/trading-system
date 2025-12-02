package pl.mo.trading_system.tickers;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.mo.trading_system.tickers.model.TickerEntity;

import java.util.List;
import java.util.Optional;

public interface TickerRepository extends JpaRepository<TickerEntity, String> {
    List<TickerEntity> findByTickerContainingIgnoreCase(String ticker);

    Optional<TickerEntity> findByIsin(String isin);
}
