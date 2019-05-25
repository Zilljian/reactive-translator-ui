package reactive.client.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactive.client.dto.operation.UIEvent;

@Slf4j
@UIScope
@Component
@EnableKafka
@RequiredArgsConstructor
public class KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void sendEvent(UIEvent event) {
        try {
            kafkaTemplate.send("reactive-ui-event", mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("Error while serializing UIEvent");
        }
    }
}
