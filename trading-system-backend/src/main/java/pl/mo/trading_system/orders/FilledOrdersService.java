package pl.mo.trading_system.orders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.mo.trading_system.orders.dto.FilledOrderMessage;
import pl.mo.trading_system.orders.model.Order;

@Service
public class FilledOrdersService {

    private final KafkaTemplate<String, FilledOrderMessage> kafkaTemplate;
    private final String topicName;

    public FilledOrdersService(@Value("${filledOrders.kafka.topic-name}") String topicName, KafkaTemplate<String, FilledOrderMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    void handleOrderFilled(Order order) {

        var message = new FilledOrderMessage(
                order.getIsin(),
                order.getTicker().getName(),
                order.getAccountId(),
                order.getFilledDate()
        );

        kafkaTemplate.send(topicName, message);
    }
}
