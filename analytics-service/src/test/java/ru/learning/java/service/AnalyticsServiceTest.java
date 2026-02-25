package ru.learning.java.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.learning.java.model.AnalyticsReport;
import ru.learning.java.model.ServiceMetrics;
import ru.learning.java.repository.MetricsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Analytics Service Unit Tests")
class AnalyticsServiceTest {

  @Mock
  private MetricsRepository metricsRepository;

  @InjectMocks
  private AnalyticsService analyticsService;

  private List<ServiceMetrics> mockMetrics;

  @BeforeEach
  void setUp() {
    mockMetrics = new ArrayList<>();

    ServiceMetrics producerMetrics = new ServiceMetrics("producer");
    producerMetrics.setEventsProcessed(100);
    producerMetrics.setEventsSuccessful(95);
    producerMetrics.setEventsFailed(5);
    producerMetrics.setTimestamp(LocalDateTime.now());

    ServiceMetrics validatorMetrics = new ServiceMetrics("validator");
    validatorMetrics.setEventsProcessed(95);
    validatorMetrics.setEventsSuccessful(90);
    validatorMetrics.setEventsFailed(5);
    validatorMetrics.setTimestamp(LocalDateTime.now());

    mockMetrics.add(producerMetrics);
    mockMetrics.add(validatorMetrics);
  }

  @Test
  @DisplayName("Should generate analytics report with correct totals")
  void shouldGenerateReportWithCorrectTotals() {
    // Arrange
    when(metricsRepository.findByTimestampBetween(any(), any()))
      .thenReturn(mockMetrics);

    // Act
    AnalyticsReport report = analyticsService.generateReport(24);

    // Assert
    assertThat(report).isNotNull();
    assertThat(report.getTotalEventsProcessed()).isEqualTo(195);
    assertThat(report.getServiceMetrics()).hasSize(2);
  }

  @Test
  @DisplayName("Should calculate health score correctly")
  void shouldCalculateHealthScoreCorrectly() {
    // Arrange
    when(metricsRepository.findByTimestampBetween(any(), any())).thenReturn(mockMetrics);

    // Act
    AnalyticsReport report = analyticsService.generateReport(24);

    // Assert
    assertThat(report.getSystemHealthScore()).isGreaterThan(94.0);
    assertThat(report.getSystemHealthScore()).isLessThan(95.0);
  }

  @Test
  @DisplayName("Should return 100% health score when no failures")
  void shouldReturn100HealthScoreWhenNoFailures() {
    // Arrange
    List<ServiceMetrics> perfectMetrics = new ArrayList<>();
    ServiceMetrics metrics = new ServiceMetrics("producer");
    metrics.setEventsProcessed(100);
    metrics.setEventsSuccessful(100);
    metrics.setEventsFailed(0);
    perfectMetrics.add(metrics);

    when(metricsRepository.findByTimestampBetween(any(), any()))
      .thenReturn(perfectMetrics);

    // Act
    AnalyticsReport report = analyticsService.generateReport(24);

    // Assert
    assertThat(report.getSystemHealthScore()).isEqualTo(100.0);
  }

  @Test
  @DisplayName("Should retrieve metrics for specific service")
  void shouldRetrieveMetricsForSpecificService() {
    // Arrange
    List<ServiceMetrics> producerMetrics = List.of(mockMetrics.getFirst());
    when(metricsRepository.findByServiceNameAndTimestampBetween(
      any(), any(), any()))
      .thenReturn(producerMetrics);

    // Act
    List<ServiceMetrics> result = analyticsService.getMetricsForService("producer", 24);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getServiceName()).isEqualTo("producer");
  }

  @Test
  @DisplayName("Should handle empty metrics gracefully")
  void shouldHandleEmptyMetricsGracefully() {
    // Arrange
    when(metricsRepository.findByTimestampBetween(any(), any()))
      .thenReturn(new ArrayList<>());

    // Act
    AnalyticsReport report = analyticsService.generateReport(24);

    // Assert
    assertThat(report).isNotNull();
    assertThat(report.getTotalEventsProcessed()).isZero();
    assertThat(report.getSystemHealthScore()).isEqualTo(100.0);
  }
}
