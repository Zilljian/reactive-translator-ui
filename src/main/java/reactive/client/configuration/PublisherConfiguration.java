package reactive.client.configuration;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactive.client.dto.operation.UIEvent;
import reactive.client.dto.service.TranslationResponse;
import reactor.core.publisher.UnicastProcessor;

@Configuration
public class PublisherConfiguration {

    @Bean
    @UIScope
    public UnicastProcessor<UIEvent> uiEventUnicastProcessor() {
        return UnicastProcessor.create();
    }

    @Bean
    public UnicastProcessor<TranslationResponse> translationResponseUnicastProcessor() {
        return UnicastProcessor.create();
    }
}
