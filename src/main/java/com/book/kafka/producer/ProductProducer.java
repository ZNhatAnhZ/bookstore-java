package com.book.kafka.producer;

import com.book.dto.GetProductRequestDTO;
import com.book.dto.ProductsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductProducer {
    private final KafkaTemplate<String, GetProductRequestDTO> kafkaTemplateGetProductRequest;
    private final KafkaTemplate<String, ProductsDTO> kafkaTemplateSendProductsDTO;

    public void sendGetProductRequest(String topic, int page, int size, String sort) {
        GetProductRequestDTO getProductRequestDTO = new GetProductRequestDTO(page, size, sort);
        kafkaTemplateGetProductRequest.send(topic, getProductRequestDTO);
    }

    public void sendProductsDTO(String topic, ProductsDTO productsDTO) {
        kafkaTemplateSendProductsDTO.send(topic, productsDTO);
    }
}
