package ru.learning.java.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import ru.learning.java.model.EmployeeEvent;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

  @Bean
  public KStream<String, EmployeeEvent> processEmployeeEvents(StreamsBuilder streamsBuilder) {
    // Читаем из всех топиков
    KStream<String, EmployeeEvent> employeeStream = streamsBuilder
      .stream("employee-events",
        Consumed.with(Serdes.String(), new JsonSerde<>(EmployeeEvent.class)));

    KStream<String, EmployeeEvent> validatedStream = streamsBuilder
      .stream("validated-employees",
        Consumed.with(Serdes.String(), new JsonSerde<>(EmployeeEvent.class)));

    // Объединяем потоки
    KStream<String, EmployeeEvent> mergedStream = employeeStream.merge(validatedStream);

    // Группируем и считаем
    mergedStream
      .groupBy((key, value) -> value.getStatus() != null ? value.getStatus() : "unknown",
        Grouped.with(Serdes.String(), new JsonSerde<>(EmployeeEvent.class)))
      .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
      .count(Materialized.as("events-by-status-store"))
      .toStream()
      .foreach((windowedKey, count) -> System.out.println("Status: " + windowedKey.key() +
        ", Window: " + windowedKey.window() +
        ", Count: " + count));

    return mergedStream;
  }
}