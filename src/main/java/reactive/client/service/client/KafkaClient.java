package reactive.client.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactive.client.dto.operation.UIEvent;
import reactor.core.publisher.UnicastProcessor;

import javax.annotation.PostConstruct;

@Slf4j
@UIScope
@Component
@RequiredArgsConstructor
public class KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final UnicastProcessor<UIEvent> event;

    @PostConstruct
    public void sendEvent() {
        event.subscribe(
                eve -> {
                    try {
                        kafkaTemplate.send("reactive-ui-event", mapper.writeValueAsString(eve));
                    } catch (JsonProcessingException e) {
                        log.error("Error while serializing UIEvent");
                    }
                });
    }
}
