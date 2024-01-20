package com.punkzieeee.customer.service;

import static org.mockito.Mockito.lenient;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.punkzieeee.customer.dto.CustomerResponseDto;
import com.punkzieeee.customer.enums.OrderStatus;
import com.punkzieeee.customer.model.Customer;
import com.punkzieeee.customer.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class ConsumerServiceTest {

    @Mock
    CustomerRepository customerRepository;
    
    @Mock
    R2dbcEntityTemplate r2dbcEntityTemplate;
    
    @Mock
    JmsTemplate jmsTemplate;

    @InjectMocks
    ConsumerService consumerService;

    CustomerResponseDto dto;
    Customer customer;
    Customer customerExist;
    LinkedHashMap<String, String> map;
    Message<LinkedHashMap<String, String>> message;

    @BeforeAll
    void setup() {
        consumerService = new ConsumerService(jmsTemplate);
        map = new LinkedHashMap<>();

        dto = new CustomerResponseDto();
        dto.setCustomerId("0");
        dto.setUsername("username");
        dto.setPassword("password");
        dto.setStatus(OrderStatus.INIT);

        map.put("customerId", "0");
        map.put("username", "username");
        map.put("password", "password");

        customer = new Customer();
        customer.setCustomerId(map.get("customerId"));
        customer.setUsername(map.get("username"));
        customer.setPassword(map.get("password"));
        customer.setStatus(OrderStatus.ACTIVE.toString());
        
        customerExist = new Customer();
        customerExist.setCustomerId("-1");
        customerExist.setUsername(map.get("username"));
        customerExist.setPassword(map.get("password"));
        customerExist.setStatus(OrderStatus.ACTIVE.toString());
    }

    @Test
    void testRegister() throws Exception {
        message = new GenericMessage<LinkedHashMap<String, String>>(map);
        log.info("message: {}", message);
        LinkedHashMap<String, String> object = message.getPayload();
        log.info("object: {}", object);

        lenient().when(r2dbcEntityTemplate.insert(customer)).thenReturn(Mono.just(customer));
        dto.setStatus(OrderStatus.CREATED);
        lenient().when(customerRepository.findByUsername(dto.getUsername())).thenReturn(Flux.empty());

        object.put("status", dto.getStatus().toString());
        jmsTemplate.convertAndSend("queue.order.callback", object);

        consumerService.register(message);
    }
}
