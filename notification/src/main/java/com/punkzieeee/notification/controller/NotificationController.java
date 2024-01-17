package com.punkzieeee.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.punkzieeee.notification.model.Notification;
import com.punkzieeee.notification.service.NotificationService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    
    @Autowired
    NotificationService notificationService;

    @GetMapping("/list")
    public Flux<Notification> getAll() {
        return notificationService.getAll();
    }
}
