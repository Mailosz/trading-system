package pl.mo.trading_system.emails;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.mo.trading_system.orders.dto.FilledOrderMessage;

@Service
public class FilledOrderQueueConsumer {

    @KafkaListener(topics = "${filledOrders.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(FilledOrderMessage message) {
        System.out.printf("Order filled for user %s for ticker %s with ISIN %s on unix timestamp %s %n", message.accountId(), message.name(), message.isin(), message.executionDate());
    }
}