package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.models.vos.CoffeeOrderVO;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class OrderControllerIntegrationTest extends BaseIntegrationTest {

    private final String basePath = "/orders";

    @MockBean
    private SqsClient sqsClient;

    @Nested
    class Create {

        @Test
        void shouldAddToQueueWhenPayloadIsValid() {
            List<CoffeeOrderVO.OrderItemVO> items = List.of(new CoffeeOrderVO.OrderItemVO(1L, 2L));
            CoffeeOrderVO payload = new CoffeeOrderVO(items, "aRandomHash");
            ResponseEntity<Void> response = template.exchange(basePath, HttpMethod.POST, getEntity(payload, true), Void.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            verify(sqsClient).sendMessage(any(SendMessageRequest.class));
        }
    }
}
