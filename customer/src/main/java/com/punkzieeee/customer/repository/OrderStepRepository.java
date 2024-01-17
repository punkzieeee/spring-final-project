package com.punkzieeee.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.customer.model.OrderStep;

import reactor.core.publisher.Flux;

public interface OrderStepRepository extends R2dbcRepository<OrderStep, Long> {
    Flux<OrderStep> findByActionId(Long actionId);
}
