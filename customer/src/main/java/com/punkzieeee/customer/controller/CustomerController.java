package com.punkzieeee.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.punkzieeee.customer.model.Customer;
import com.punkzieeee.customer.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {
    
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public Flux<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/search/{id}")
    public Mono<Customer> check(@PathVariable String id) {
        return customerService.check(id);
    }
}
