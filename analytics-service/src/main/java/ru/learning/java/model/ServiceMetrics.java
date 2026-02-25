package ru.learning.java.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Document(collection = "service_metrics")
public class ServiceMetrics {
  @Id
  private String id;
  private String serviceName; // producer, consumer, validator
  private long eventsProcessed;
  private long eventsSuccessful;
  private long eventsFailed;
  private double averageProcessingTime;
  private LocalDateTime timestamp;
  private Map<String, Object> additionalMetrics;

  public ServiceMetrics() {
    this.timestamp = LocalDateTime.now();
  }

  public ServiceMetrics(String serviceName) {
    this();
    this.serviceName = serviceName;
  }

  @Override
  public String toString() {
    return "ServiceMetrics{" +
      "id='" + id + '\'' +
      ", serviceName='" + serviceName + '\'' +
      ", eventsProcessed=" + eventsProcessed +
      ", eventsSuccessful=" + eventsSuccessful +
      ", eventsFailed=" + eventsFailed +
      ", timestamp=" + timestamp +
      '}';
  }
}