package com.punkzieeee.notification.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "notifications")
@Data
public class Notification {
    @Id
    private String notifId;
    private String customerId;
    private String message;
    private Timestamp createdAt;
}
