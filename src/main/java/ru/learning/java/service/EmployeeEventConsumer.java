package ru.learning.java.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ru.learning.java.model.EmployeeEvent;

import java.sql.SQLOutput;

public class EmployeeEventConsumer {

    @KafkaListener(topics = "test-project-input", groupId = "my-group")
    public void listen (@Payload EmployeeEvent event ,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        System.out.println("Получено сообщение для сотрудника (Key: " + key + "): " + event);
    }
}
