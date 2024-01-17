package com.punkzieeee.customer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "order_steps")
@Data
public class OrderStep {
    @Id
    private Long id;
    private Long actionId;
    private String step;
    private int priority;
}
