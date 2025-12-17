package ru.learning.java.validator.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JsonSchemaValidator {

  private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);
  private final JsonSchema schema;
  private final ObjectMapper objectMapper;

  public JsonSchemaValidator() {
    this.objectMapper = new ObjectMapper();
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

    try {
      InputStream schemaStream = getClass().getClassLoader()
        .getResourceAsStream("schemas/employee-with-department-event-v1.schema.json");

      if (schemaStream == null) {
        throw new IllegalStateException("Schema file not found!");
      }

      this.schema = factory.getSchema(schemaStream);
      log.info("JSON Schema загружена успешно");
    } catch (Exception e) {
      log.error("Ошибка загрузки схемы", e);
      throw new RuntimeException("Не удалось загрузить JSON Schema", e);
    }
  }

  public ValidationResult validate(String jsonMessage) {
    try {
      JsonNode jsonNode = objectMapper.readTree(jsonMessage);
      Set<ValidationMessage> errors = schema.validate(jsonNode);

      if (errors.isEmpty()) {
        log.debug("Сообщение валидно");
        return ValidationResult.valid();
      } else {
        String errorMessages = errors.stream()
          .map(ValidationMessage::getMessage)
          .collect(Collectors.joining("; "));
        log.warn("Валидация не пройдена: {}", errorMessages);
        return ValidationResult.invalid(errorMessages);
      }
    } catch (Exception e) {
      log.error("Ошибка парсинга JSON", e);
      return ValidationResult.invalid("Невалидный JSON: " + e.getMessage());
    }
  }

  public static class ValidationResult {
    private final boolean valid;
    private final String errorMessage;

    private ValidationResult(boolean valid, String errorMessage) {
      this.valid = valid;
      this.errorMessage = errorMessage;
    }

    public static ValidationResult valid() {
      return new ValidationResult(true, null);
    }

    public static ValidationResult invalid(String errorMessage) {
      return new ValidationResult(false, errorMessage);
    }

    public boolean isValid() {
      return valid;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
  }
}
