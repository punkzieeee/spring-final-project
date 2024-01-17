package com.punkzieeee.customer.dto;

import com.punkzieeee.customer.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponseDto {
    private String customerId;
    private String username;
    private String password;
    private OrderStatus status;
}
