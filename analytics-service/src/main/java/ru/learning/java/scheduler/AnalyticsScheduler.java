package ru.learning.java.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.learning.java.service.MetricsCollector;

@Component
public class AnalyticsScheduler {

  private static final Logger log = LoggerFactory.getLogger(AnalyticsScheduler.class);

  private final MetricsCollector metricsCollector;

  public AnalyticsScheduler(MetricsCollector metricsCollector) {
    this.metricsCollector = metricsCollector;
  }

  @Scheduled(fixedRate = 60000)
  public void persistMetrics() {
    log.info("Scheduled task: Persisting metrics to database");
    metricsCollector.persistMetrics();
  }

  // Каждый час очищаем старые данные (опционально)
  @Scheduled(cron = "0 0 0 * * *")
  public void cleanupOldMetrics() {
    log.info("Scheduled task: Cleaning up old metrics");
    // Можно добавить логику удаления старых данных
  }
}