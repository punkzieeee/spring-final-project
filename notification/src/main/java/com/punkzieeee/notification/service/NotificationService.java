package com.punkzieeee.notification.service;

import com.punkzieeee.notification.model.Notification;

import reactor.core.publisher.Flux;

public interface NotificationService {
    Flux<Notification> getAll();
}
