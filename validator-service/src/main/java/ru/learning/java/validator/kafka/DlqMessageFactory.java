package ru.learning.java.validator.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DlqMessageFactory {

  private static final Logger log = LoggerFactory.getLogger(DlqMessageFactory.class);
  private final ObjectMapper objectMapper;

  public DlqMessageFactory() {
    this.objectMapper = new ObjectMapper();
  }

  public String createDlqMessage(String originalMessage, String errorReason, String topic, int partition, long offset) {
    try {
      ObjectNode dlqMessage = objectMapper.createObjectNode();
      dlqMessage.put("originalMessage", originalMessage);
      dlqMessage.put("errorReason", errorReason);
      dlqMessage.put("failedAt", Instant.now().toString());
      dlqMessage.put("sourceTopic", topic);
      dlqMessage.put("sourcePartition", partition);
      dlqMessage.put("sourceOffset", offset);

      return objectMapper.writeValueAsString(dlqMessage);
    } catch (Exception e) {
      log.error("Ошибка создания DLQ сообщения", e);
      return String.format("{\"originalMessage\":\"%s\",\"errorReason\":\"%s\"}",
        originalMessage.replace("\"", "\\\""), errorReason);
    }
  }
}