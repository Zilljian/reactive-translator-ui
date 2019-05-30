package reactive.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactive.client.dto.service.TranslationResponse;
import reactor.core.publisher.UnicastProcessor;

import java.io.IOException;

@Component
@EnableKafka
@RequiredArgsConstructor
public class KafkaController {

    private final ObjectMapper mapper;
    private final UnicastProcessor<TranslationResponse> response;

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(String message) throws IOException {
         response.onNext(mapper.readValue(message, TranslationResponse.class));
    }
}
