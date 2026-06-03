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
        if (transactionLine == null || transactionLine.isBlank()) {
            return;
        }

        String[] transactionData = transactionLine.split(", ");
        if (transactionData.length < 3) {
            return;
        }

        try {
            long senderId = Long.parseLong(transactionData[0].trim());
            long receiverId = Long.parseLong(transactionData[1].trim());
            float amount = Float.parseFloat(transactionData[2].trim());

            kafkaTemplate.send(topic, new Transaction(senderId, receiverId, amount));
        } catch (NumberFormatException e) {
        }
    }
}
