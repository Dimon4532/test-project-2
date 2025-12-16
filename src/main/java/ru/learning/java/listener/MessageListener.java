/*package ru.learning.java.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @KafkaListener(
            topics = "my-topic",
            groupId = "my-group"
    )
    public void listen (String message){
        System.out.println("Received message: " + message);
    }
}
*/