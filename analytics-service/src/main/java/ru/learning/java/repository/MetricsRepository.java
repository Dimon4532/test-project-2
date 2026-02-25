package ru.learning.java.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.learning.java.model.ServiceMetrics;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricsRepository extends MongoRepository<ServiceMetrics, String> {
  List<ServiceMetrics> findByServiceName(String serviceName);
  List<ServiceMetrics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
  List<ServiceMetrics> findByServiceNameAndTimestampBetween(
    String serviceName, LocalDateTime start, LocalDateTime end);
}