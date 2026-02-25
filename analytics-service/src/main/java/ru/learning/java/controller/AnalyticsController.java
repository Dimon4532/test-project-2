package ru.learning.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.model.AnalyticsReport;
import ru.learning.java.model.ServiceMetrics;
import ru.learning.java.service.AnalyticsService;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @GetMapping("/report")
  public ResponseEntity<AnalyticsReport> getReport(
    @RequestParam(defaultValue = "24") int hours) {
    AnalyticsReport report = analyticsService.generateReport(hours);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/metrics/{serviceName}")
  public ResponseEntity<List<ServiceMetrics>> getServiceMetrics(
    @PathVariable String serviceName,
    @RequestParam(defaultValue = "24") int hours) {
    List<ServiceMetrics> metrics = analyticsService.getMetricsForService(serviceName, hours);
    return ResponseEntity.ok(metrics);
  }

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Analytics Service is running");
  }
}