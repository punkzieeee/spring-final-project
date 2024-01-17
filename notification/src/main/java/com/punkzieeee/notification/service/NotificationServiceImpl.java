package com.punkzieeee.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.repository.NotificationRepository;

import reactor.core.publisher.Flux;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Flux<Notification> getAll() {
        return notificationRepository.findAll();
    }    
}
