package pl.mo.trading_system.orders.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByAccountId(long currentAccountId);

    Optional<OrderEntity> findByIdAndAccountId(UUID id, long currentAccountId);

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.ticker WHERE o.status = :status")
    List<OrderEntity> findAllByStatusWithTicker(@Param("status") OrderStatus status);
}
