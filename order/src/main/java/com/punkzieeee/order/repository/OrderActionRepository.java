package com.punkzieeee.order.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.order.model.OrderAction;

import reactor.core.publisher.Mono;

public interface OrderActionRepository extends R2dbcRepository<OrderAction, Long> {
    Mono<OrderAction> findByAction(String action);
}
