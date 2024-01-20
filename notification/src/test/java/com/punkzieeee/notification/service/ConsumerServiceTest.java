package com.punkzieeee.notification.service;

import static org.mockito.Mockito.lenient;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.UUID;

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

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class ConsumerServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    R2dbcEntityTemplate r2dbcEntityTemplate;
    
    @Mock
    JmsTemplate jmsTemplate;

    @InjectMocks
    ConsumerService consumerService;

    Notification notification;
    LinkedHashMap<String, String> map;
    Message<LinkedHashMap<String, String>> message;

    @BeforeAll
    void setup() {
        consumerService = new ConsumerService(r2dbcEntityTemplate, jmsTemplate);
        map = new LinkedHashMap<>();

        map.put("customerId", "0");
        map.put("username", "username");
        map.put("password", "password");

        notification = new Notification();
        notification.setNotifId(UUID.randomUUID().toString());
        notification.setCustomerId(map.get("customerId"));
        notification.setMessage(map.get("customerId") + " -> " + map.get("username"));
        notification.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    }

    @Test
    void testPost() throws Exception {
        message = new GenericMessage<LinkedHashMap<String, String>>(map);
        log.info("message: {}", message);
        LinkedHashMap<String, String> object = message.getPayload();
        log.info("object: {}", object);

        lenient().when(r2dbcEntityTemplate.insert(notification)).thenReturn(Mono.just(notification));

        object.put("status", "CREATED");
        log.info("callback: {}", object);

        jmsTemplate.convertAndSend("queue.order.callback", object);

        consumerService.post(message);
    }
}
