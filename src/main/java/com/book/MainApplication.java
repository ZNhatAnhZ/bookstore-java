package com.book;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

    }
    @Bean
    public NewTopic topicProductRequest() {
        return TopicBuilder.name("productRequest")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicProductResponse() {
        return TopicBuilder.name("productResponse")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
