package com.punkzieeee.notification.controller;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.service.NotificationService;

import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@WebFluxTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    NotificationService notificationService;

    Notification notification;

    @BeforeAll
    void setup() {
        notification = new Notification();
        notification.setNotifId("0");
        notification.setCustomerId("0");
        notification.setMessage("test");
        notification.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    }

    @Test
    void testGetAll() {
        Flux<Notification> flux = Flux.just(notification);
        when(notificationService.getAll()).thenReturn(flux);

        webClient.get().uri("/notification/list").exchange().expectBody().equals(flux);
    }
}
