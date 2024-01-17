package com.punkzieeee.order.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.order.model.OrderStep;

public interface OrderStepRepository extends R2dbcRepository<OrderStep, Long>{
    
}
