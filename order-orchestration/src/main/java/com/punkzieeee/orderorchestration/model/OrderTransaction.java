package com.punkzieeee.orderorchestration.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "order_transactions")
@Data
public class OrderTransaction {
    @Id
    private Long id;
    private Long actionId;
    private String customerId;
    private String step;
    private String status;
    private Timestamp createdAt;
}
