package integrationTest;

import com.book.config.KafkaConfig;
import com.book.dto.ProductsDTO;
import com.book.kafka.consumer.ProductDTOConsumer;
import com.book.kafka.producer.ProductProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = { KafkaConfig.class, ProductProducer.class, ProductDTOConsumer.class})
public class KafkaTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private ProductProducer producer;
    @Autowired
    private ProductDTOConsumer productDTOConsumer;

    @Test
    public void sendGetProductKafka() throws InterruptedException {
        producer.sendGetProductRequest("productRequest", 0, 1, "price_asc");

        Thread.sleep(5000);

        ConsumerRecords<String, ProductsDTO> records = productDTOConsumer.getProductDTO("productResponse");
        assertTrue(!records.isEmpty());
        for(ConsumerRecord<String, ProductsDTO> record : records) {
            assertNotNull(record.value());
            log.info(String.valueOf(record.value()));
        }
    }
}
