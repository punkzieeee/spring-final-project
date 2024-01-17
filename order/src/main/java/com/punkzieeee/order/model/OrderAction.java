package com.punkzieeee.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "order_actions")
@Data
public class OrderAction {
    @Id
    private Long id;
    private String action;
}
