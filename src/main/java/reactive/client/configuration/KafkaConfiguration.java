package reactive.client.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import reactive.client.dto.operation.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    public Map<String, Object> kafkaConsumerConfiguration() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties().getUrl());
        configuration.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties().getGroupId());
        configuration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configuration.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return configuration;
    }

    @Bean
    public ConsumerFactory<String, String> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaConsumerConfiguration(),
                new StringDeserializer(),
                new StringDeserializer()
        );
    }

    @Bean
    public ContainerAwareErrorHandler containerAwareErrorHandler() {
        return new SeekToCurrentErrorHandler();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());
        return factory;
    }

    @Bean
    public Map<String, Object> kafkaProducerConfiguration() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties().getUrl());
        configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return configuration;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfiguration());
    }
}
