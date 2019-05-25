package reactive.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactive.client.dto.operation.Credentials;

@Configuration
public class RestConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "yandex.credentials")
    public Credentials credentials() {
        return new Credentials();
    }
}
