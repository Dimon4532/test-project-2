package ru.learning.java.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
public class AnalyticsReport {
  private LocalDateTime generatedAt;
  private Map<String, ServiceMetrics> serviceMetrics;
  private long totalEventsProcessed;
  private double systemHealthScore;
  private Map<String, Long> eventsByType;

  public AnalyticsReport() {
    this.generatedAt = LocalDateTime.now();
  }
}