package com.punkzieeee.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "customers")
@Data
public class Customer {
    @Id
    private String customerId;
    private String username;
    private String password;
    private String status;
}
