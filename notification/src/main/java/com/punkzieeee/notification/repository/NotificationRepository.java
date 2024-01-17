package com.punkzieeee.notification.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.notification.model.Notification;

import reactor.core.publisher.Mono;

public interface NotificationRepository extends R2dbcRepository<Notification, String> {
    Mono<Notification> findByNotifId(String notifId);
}
