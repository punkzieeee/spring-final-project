package com.punkzieeee.orderorchestration.service;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.orderorchestration.model.OrderStep;
import com.punkzieeee.orderorchestration.repository.OrderStepRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class OrchestratorService {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    OrderStepRepository orderStepRepository;

    @Autowired
    ObjectMapper objectMapper;

    @JmsListener(destination = "queue.order")
    public void register(final Message<String> message) throws Exception {
        log.info("message: {}", message);

        // layer 1
        LinkedHashMap<String, Object> objMessage = (LinkedHashMap<String, Object>) objectMapper
                .readValue(message.getPayload(), LinkedHashMap.class);
        log.info("object: {}", objMessage);
        LinkedHashMap<String, Object> mapMessage = (LinkedHashMap<String, Object>) objMessage.get("data");
        log.info("OrderActionDto: {}", mapMessage);
        Integer actionId = (Integer) objMessage.get("actionId");
        log.info("actionId: {}", actionId);

        // layer 2
        String objectAction = (String) mapMessage.get("action");
        log.info("action: {}", objectAction);
        Object objectMap = mapMessage.get("data");
        log.info("object: {}", objectMap);
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) objectMap;
        log.info("map: {}", map);

        Flux<OrderStep> orderStep = orderStepRepository.findByActionIdOrderByPriority(Long.valueOf(actionId));
        orderStep.subscribe(step -> {
            jmsTemplate.convertAndSend(step.getStep(), map);
            log.info("step: {}", step.getStep());
        });

        log.info("orderStep: {}", orderStep.collectList().block());
    }
}
