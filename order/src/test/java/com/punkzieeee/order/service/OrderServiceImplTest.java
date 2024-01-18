package com.punkzieeee.order.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.order.dto.OrderActionDto;
import com.punkzieeee.order.model.OrderAction;
import com.punkzieeee.order.repository.OrderActionRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceImplTest {

    @Mock
    OrderActionRepository orderActionRepository;

    @Mock
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @InjectMocks
    OrderServiceImpl orderService;

    @Autowired
    JmsTemplate jmsTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    OrderActionDto orderActionDto;
    OrderAction orderAction;

    @BeforeAll
    void setup() {
        orderActionDto = new OrderActionDto();
        orderActionDto.setAction("register");
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "0");
        map.put("username", "username");
        map.put("password", "password");
        orderActionDto.setData(map);

        orderAction = new OrderAction();
        orderAction.setId(1L);
        orderAction.setAction(orderActionDto.getAction().toUpperCase());
    }

    @Test
    void testMakeQueueOrder() {
        Mono<OrderAction> mono = Mono.just(orderAction);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("data", orderActionDto);
        lenient().when(orderActionRepository.findByAction(orderActionDto.getAction().toUpperCase()))
                .thenReturn(mono);
        mono.doOnSuccess(n -> {
            map.put("actionId", n.getId());
            String object;
            try {
                object = objectMapper.writeValueAsString(map);
                jmsTemplate.convertAndSend("queue.order", object);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        String result = orderService.makeQueueOrder(orderActionDto);
        assertNotNull(result);
    }

    @Test
    void testMakeQueueOrderNegative() {
        StepVerifier.create(orderActionRepository.findByAction(orderActionDto.getAction().toUpperCase()))
                .expectError();

        String result = orderService.makeQueueOrder(orderActionDto);
        assertNotNull(result);
    }
}
