package com.punkzieeee.customer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.punkzieeee.customer.model.Customer;
import com.punkzieeee.customer.repository.CustomerRepository;
import com.punkzieeee.customer.repository.OrderStepRepository;
import com.punkzieeee.customer.dto.CustomerResponseDto;
import com.punkzieeee.customer.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderStepRepository orderStepRepository;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    JmsTemplate jmsTemplate;

    @Async
    @JmsListener(destination = "queue.crm.register")
    public void register(Message<LinkedHashMap<String, String>> message) throws Exception {
        log.info("message: {}", message);
        LinkedHashMap<String, String> object = message.getPayload();
        log.info("object: {}", object);

        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(object.get("customerId"));
        dto.setUsername(object.get("username"));
        dto.setPassword(object.get("password"));
        dto.setStatus(OrderStatus.INIT);
        log.info("dto init: {}", dto);

        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        customer.setStatus(OrderStatus.ACTIVE.toString());
        // insert
        r2dbcEntityTemplate.insert(customer)
                .doOnNext(s -> {
                    log.info("data success: {}", s);
                    dto.setStatus(OrderStatus.CREATED);
                })
                .doOnError(e -> {
                    dto.setStatus(OrderStatus.FAILED);
                    log.info("error: {}", e.getMessage());
                }).subscribe();

        // revert
        Flux<Customer> checkSameUsername = customerRepository.findByUsername(dto.getUsername());
        List<Customer> check = checkSameUsername.collectList().block();
        log.info("checkSameUsername: {}", check);
        if (check.size() > 1) {
            customerRepository.delete(customer).subscribe();
            dto.setStatus(OrderStatus.FAILED);
            log.info("data deleted due to similar username!");
        }

        log.info("dto: {}", dto);
        object.put("status", dto.getStatus().toString());
        log.info("callback: {}", object);

        jmsTemplate.convertAndSend("queue.order.callback", object);
    }
}
