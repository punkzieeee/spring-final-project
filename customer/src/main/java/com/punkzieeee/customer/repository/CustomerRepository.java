package com.punkzieeee.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.customer.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<Customer, String> {
    Mono<Customer> findByCustomerId(String customerId);
    Flux<Customer> findByUsername(String username);
}
