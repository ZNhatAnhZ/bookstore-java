package com.book.kafka.consumer;

import com.book.dto.GetProductRequestDTO;
import com.book.dto.ProductsDTO;
import com.book.kafka.producer.ProductProducer;
import com.book.model.ProductsEntity;
import com.book.service.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {
    private final ProductsService productsService;
    private final ProductProducer producer;
    @KafkaListener(id = "multi", topics = "productRequest")
    public void listenGetProductRequest(GetProductRequestDTO getProductRequestDTO) {
        Optional<Page<ProductsEntity>> result = productsService.findAllProductsBySort(getProductRequestDTO.getPage(), getProductRequestDTO.getSize(), getProductRequestDTO.getSort());

        log.info(result.toString());

        if (result.isPresent()) {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO.setProductsEntityList(result.get().toList());
            productsDTO.setCurrentPage(result.get().getNumber());
            productsDTO.setTotalPages(result.get().getTotalPages());
            productsDTO.setTotalItems(result.get().getTotalElements());

            producer.sendProductsDTO("productResponse", productsDTO);
        } else {
            log.error("cant find products");
        }
    }
}
