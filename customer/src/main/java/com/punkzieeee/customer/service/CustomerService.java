package com.punkzieeee.customer.service;

import com.punkzieeee.customer.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> getAll();
    Mono<Customer> check(String id);
}
