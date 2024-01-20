package com.punkzieeee.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.punkzieeee.customer.model.Customer;
import com.punkzieeee.customer.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer customer;
    String id;

    @BeforeAll
    void setup() {
        id = "0";

        customer = new Customer();
        customer.setCustomerId(id);
        customer.setUsername("username");
        customer.setPassword("password");
        customer.setStatus("ACTIVE");
    }

    @Test
    void testGetAll() {
        Flux<Customer> flux = Flux.just(customer);
        when(customerRepository.findAll()).thenReturn(flux);

        Flux<Customer> result = customerService.getAll();
        assertEquals(flux, result); 
    }

    @Test
    void testCheck() {
        Mono<Customer> mono = Mono.just(customer);
        when(customerRepository.findByCustomerId(id)).thenReturn(mono);

        Mono<Customer> result = customerService.check(id);
        assertEquals(mono, result);
    }
}
