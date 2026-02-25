package ru.learning.java.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.learning.java.model.AnalyticsReport;
import ru.learning.java.model.ServiceMetrics;
import ru.learning.java.repository.MetricsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

  private final MetricsRepository metricsRepository;

  public AnalyticsService(MetricsRepository metricsRepository) {
    this.metricsRepository = metricsRepository;
  }

  @Cacheable(value = "analytics-report", key = "#hours")
  public AnalyticsReport generateReport(int hours) {
    LocalDateTime start = LocalDateTime.now().minusHours(hours);
    LocalDateTime end = LocalDateTime.now();

    List<ServiceMetrics> metrics = metricsRepository.findByTimestampBetween(start, end);

    AnalyticsReport report = new AnalyticsReport();

    // Группируем по сервисам
    Map<String, ServiceMetrics> serviceMetricsMap = metrics.stream()
      .collect(Collectors.groupingBy(
        ServiceMetrics::getServiceName,
        Collectors.collectingAndThen(
          Collectors.toList(),
          this::aggregateMetrics
        )
      ));

    report.setServiceMetrics(serviceMetricsMap);

    // Считаем общее количество событий
    long totalEvents = metrics.stream()
      .mapToLong(ServiceMetrics::getEventsProcessed)
      .sum();
    report.setTotalEventsProcessed(totalEvents);

    double healthScore = calculateHealthScore(metrics);
    report.setSystemHealthScore(healthScore);

    return report;
  }

  private ServiceMetrics aggregateMetrics(List<ServiceMetrics> metricsList) {
    ServiceMetrics aggregated = new ServiceMetrics();
    aggregated.setServiceName(metricsList.getFirst().getServiceName());
    aggregated.setEventsProcessed(
      metricsList.stream().mapToLong(ServiceMetrics::getEventsProcessed).sum()
    );
    aggregated.setEventsSuccessful(
      metricsList.stream().mapToLong(ServiceMetrics::getEventsSuccessful).sum()
    );
    aggregated.setEventsFailed(
      metricsList.stream().mapToLong(ServiceMetrics::getEventsFailed).sum()
    );
    return aggregated;
  }

  private double calculateHealthScore(List<ServiceMetrics> metrics) {
    if (metrics.isEmpty()) return 100.0;

    long totalProcessed = metrics.stream().mapToLong(ServiceMetrics::getEventsProcessed).sum();
    long totalFailed = metrics.stream().mapToLong(ServiceMetrics::getEventsFailed).sum();

    if (totalProcessed == 0) return 100.0;

    double successRate = ((double) (totalProcessed - totalFailed) / totalProcessed) * 100;
    return Math.round(successRate * 100.0) / 100.0;
  }

  public List<ServiceMetrics> getMetricsForService(String serviceName, int hours) {
    LocalDateTime start = LocalDateTime.now().minusHours(hours);
    LocalDateTime end = LocalDateTime.now();
    return metricsRepository.findByServiceNameAndTimestampBetween(serviceName, start, end);
  }
}