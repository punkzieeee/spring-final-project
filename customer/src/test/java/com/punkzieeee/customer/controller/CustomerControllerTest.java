package com.punkzieeee.customer.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.punkzieeee.customer.enums.OrderStatus;
import com.punkzieeee.customer.model.Customer;
import com.punkzieeee.customer.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@WebFluxTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    CustomerService customerService;

    Customer customer;

    @BeforeAll
    void setup() {
        customer = new Customer();
        customer.setCustomerId("0");
        customer.setUsername("username");
        customer.setPassword("password");
        customer.setStatus(OrderStatus.ACTIVE.toString());
    }

    @Test
    void testCheck() {
        String id = "0";
        Mono<Customer> mono = Mono.just(customer);
        when(customerService.check(id)).thenReturn(mono);

        webClient.get().uri("/customer/search/" + id).exchange().expectBody().equals(mono);
    }

    @Test
    void testGetAll() {
        Flux<Customer> flux = Flux.just(customer);
        when(customerService.getAll()).thenReturn(flux);

        webClient.get().uri("/customer/list").exchange().expectBody().equals(flux);
    }
}
