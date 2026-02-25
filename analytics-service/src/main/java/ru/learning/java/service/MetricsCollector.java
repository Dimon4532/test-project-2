package ru.learning.java.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.learning.java.model.EmployeeEvent;
import ru.learning.java.model.ServiceMetrics;
import ru.learning.java.repository.MetricsRepository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class MetricsCollector {

  private static final Logger log = LoggerFactory.getLogger(MetricsCollector.class);

  private final MetricsRepository metricsRepository;
  private final NotificationService notificationService;

  // Временное хранилище для агрегации
  private final Map<String, ServiceMetrics> currentMetrics = new ConcurrentHashMap<>();

  public MetricsCollector(MetricsRepository metricsRepository,
                          NotificationService notificationService) {
    this.metricsRepository = metricsRepository;
    this.notificationService = notificationService;
  }

  @KafkaListener(topics = "employee-events", groupId = "analytics-group")
  public void collectFromProducer(EmployeeEvent event) {
    log.info("Collecting metrics from producer: {}", event);
    updateMetrics("producer", true);
  }

  @KafkaListener(topics = "validated-employees", groupId = "analytics-group")
  public void collectFromValidator(EmployeeEvent event) {
    log.info("Collecting metrics from validator: {}", event);
    boolean success = event.getStatus() != null && !event.getStatus().equals("INVALID");
    updateMetrics("validator", success);
  }

  private void updateMetrics(String serviceName, boolean success) {
    ServiceMetrics metrics = currentMetrics.computeIfAbsent(
      serviceName,
      k -> new ServiceMetrics(serviceName)
    );

    metrics.setEventsProcessed(metrics.getEventsProcessed() + 1);

    if (success) {
      metrics.setEventsSuccessful(metrics.getEventsSuccessful() + 1);
    } else {
      metrics.setEventsFailed(metrics.getEventsFailed() + 1);
    }

    notificationService.sendMetricsUpdate(metrics);
  }

  public void persistMetrics() {
    currentMetrics.values().forEach(metrics -> {
      log.info("Persisting metrics: {}", metrics);
      metricsRepository.save(metrics);
    });
    currentMetrics.clear();
  }
}