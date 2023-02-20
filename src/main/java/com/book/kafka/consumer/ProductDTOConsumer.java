package com.book.kafka.consumer;

import com.book.dto.ProductsDTO;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ProductDTOConsumer {
    private KafkaConsumer<String, ProductsDTO> kafkaConsumer;
    public ProductDTOConsumer() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "test-group");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");
        props.setProperty(JsonDeserializer.TRUSTED_PACKAGES, "com.book.*");
        kafkaConsumer = new KafkaConsumer<>(props);
    }
    public ConsumerRecords<String,ProductsDTO> getProductDTO(String topic) {
        kafkaConsumer.subscribe(Arrays.asList(topic));
        return kafkaConsumer.poll(Duration.ofMillis(10000));
    }
}
