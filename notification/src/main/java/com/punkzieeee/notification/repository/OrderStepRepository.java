package com.punkzieeee.notification.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.notification.model.OrderStep;

import reactor.core.publisher.Flux;

public interface OrderStepRepository extends R2dbcRepository<OrderStep, Long> {
    Flux<OrderStep> findByActionId(Long actionId);
}
