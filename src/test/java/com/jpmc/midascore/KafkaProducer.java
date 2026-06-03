package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        // 1. Guard against null or empty lines
        if (transactionLine == null || transactionLine.isBlank()) {
            return;
        }

        // 2. Parse split data fields safely
        String[] transactionData = transactionLine.split(", ");
        if (transactionData.length < 3) {
            return;
        }

        try {
            // 3. Trim values to clean up accidental hidden white spaces
            long senderId = Long.parseLong(transactionData[0].trim());
            long receiverId = Long.parseLong(transactionData[1].trim());
            float amount = Float.parseFloat(transactionData[2].trim());

            kafkaTemplate.send(topic, new Transaction(senderId, receiverId, amount));
        } catch (NumberFormatException e) {
            // 4. Safely ignore or log malformed parsing records
        }
    }
}
