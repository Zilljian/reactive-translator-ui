package reactive.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactive.client.dto.service.TranslationResponse;
import reactive.client.ui.UiFrame;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@UIScope
@Component
@EnableKafka
@RequiredArgsConstructor
public class KafkaController {
    private final UiFrame frame;
    private final ObjectMapper mapper;

    private TranslationResponse last = new TranslationResponse(200, "", Collections.singletonList(""), LocalDateTime.now());

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(String message) throws IOException {
        Flux.just(mapper.readValue(message, TranslationResponse.class))
                .filter(event -> event.getCreated().isAfter(last.getCreated()))
                .log("onMessage")
                .subscribe(
                        event -> {
                            last = event;
                            frame.getUI().ifPresent(
                                    ui -> {
                                        ui.access(
                                                () -> frame.getField().getField().setValue(message));
                                        ui.push();
                                    });
                        }
                );
    }
}
