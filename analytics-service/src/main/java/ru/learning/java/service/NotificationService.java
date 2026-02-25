package ru.learning.java.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.learning.java.model.ServiceMetrics;

@Service
public class NotificationService {

  private final SimpMessagingTemplate messagingTemplate;

  public NotificationService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void sendMetricsUpdate(ServiceMetrics metrics) {
    messagingTemplate.convertAndSend("/topic/metrics", metrics);
  }
}