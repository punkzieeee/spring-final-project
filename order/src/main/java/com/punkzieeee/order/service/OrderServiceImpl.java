package com.punkzieeee.order.service;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.order.dto.OrderActionDto;
import com.punkzieeee.order.model.OrderAction;
import com.punkzieeee.order.repository.OrderActionRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderActionRepository orderActionRepository;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public OrderServiceImpl(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String makeQueueOrder(OrderActionDto dto) {
        log.info("dto: {}", dto);
        try {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("data", dto);
            log.info("action: {}", dto.getAction());
            log.info("data: {}", dto.getData());

            Mono<OrderAction> orderAction = orderActionRepository.findByAction(dto.getAction().toUpperCase())
                    .doOnSuccess(n -> {
                        map.put("actionId", n.getId());
                        String object;
                        try {
                            object = objectMapper.writeValueAsString(map);
                            log.info("object to send: {}", object);
                            jmsTemplate.convertAndSend("queue.order", object);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    });

            log.info("orderAction: {}", orderAction.subscribe(m -> {
                System.out.println(m.getId() + " + " + m.getAction());
            }));

            return "On process...";
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
            return e.getMessage();
        }
    }
}
