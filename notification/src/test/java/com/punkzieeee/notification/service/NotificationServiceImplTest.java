package com.punkzieeee.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.repository.NotificationRepository;

import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class NotificationServiceImplTest {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationServiceImpl notificationService;

    @Test
    void testGetAll() {
        Notification notification = new Notification();
        Flux<Notification> flux = Flux.just(notification);

        lenient().when(notificationRepository.findAll()).thenReturn(flux);
        Flux<Notification> result = notificationService.getAll();
        assertEquals(flux, result);
    }
}
