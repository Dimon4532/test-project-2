package ru.learning.java.validator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import ru.learning.java.kafka.Topics;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "validator-service-group");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
    // Создаем DLQ recoverer - он отправит сообщение в DLQ после всех retry
    var recoverer = new org.springframework.kafka.listener.DeadLetterPublishingRecoverer(
            kafkaTemplate,
            (record, ex) -> new org.apache.kafka.common.TopicPartition(Topics.EMPLOYEE_DLQ, -1)
    );

    // Настраиваем retry: 3 попытки с экспоненциальной задержкой (2 сек, 4 сек, 8 сек)
    var backOff = new ExponentialBackOffWithMaxRetries(3);
    backOff.setInitialInterval(2000L);
    backOff.setMultiplier(2.0);
    backOff.setMaxInterval(10000L);

    return new DefaultErrorHandler(recoverer, backOff);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
          ConsumerFactory<String, String> consumerFactory,
          DefaultErrorHandler errorHandler
  ) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setCommonErrorHandler(errorHandler);
    return factory;
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}