package com.punkzieeee.notification.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    JmsTemplate jmsTemplate;

    @Async
    @JmsListener(destination = "queue.notif")
    public void post(Message<LinkedHashMap<String, String>> message) throws Exception {
        log.info("message: {}", message);
        LinkedHashMap<String, String> object = message.getPayload();
        log.info("object: {}", object);
        
        Notification notif = new Notification();
        notif.setNotifId(UUID.randomUUID().toString());
        notif.setCustomerId(object.get("customerId"));
        notif.setMessage(object.get("customerId") + " -> " + object.get("username"));
        notif.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        Mono<Notification> mono = r2dbcEntityTemplate.insert(notif);

        log.info("data inserted: {}", mono.subscribe(data -> {
            System.out.println(data);
        }));

        object.put("status", "CREATED");
        log.info("callback: {}", object);

        jmsTemplate.convertAndSend("queue.order.callback", object);
    }
}
