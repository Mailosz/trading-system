package pl.mo.trading_system.orders.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByAccountId(long currentAccountId);

    Optional<Order> findByIdAndAccountId(UUID id, long currentAccountId);

    @Query("SELECT o FROM Order o JOIN FETCH o.ticker WHERE o.status = :status")
    List<Order> findAllByStatusWithTicker(@Param("status") OrderStatus status);
}
