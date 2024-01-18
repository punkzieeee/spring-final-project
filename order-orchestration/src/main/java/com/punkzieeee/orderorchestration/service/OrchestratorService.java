package com.punkzieeee.orderorchestration.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.orderorchestration.enums.OrderStatus;
import com.punkzieeee.orderorchestration.model.OrderStep;
import com.punkzieeee.orderorchestration.model.OrderTransaction;
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

    public OrchestratorService(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

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
        orderStep.collectList().subscribe(step -> {
            for (int i = 0; i < step.size(); i++) {
                String queue = step.get(i).getStep();
                jmsTemplate.convertAndSend(queue, map);

                OrderTransaction start = new OrderTransaction();
                start.setActionId(Long.valueOf(actionId));
                start.setCustomerId(map.get("customerId"));
                start.setStep(queue);
                start.setStatus(OrderStatus.INIT.toString());
                start.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                r2dbcEntityTemplate.insert(start).subscribe();

                LinkedHashMap<String, String> callback = (LinkedHashMap<String, String>) jmsTemplate
                        .receiveAndConvert("queue.order.callback");
                log.info("callback: {}", callback);

                OrderTransaction finish = new OrderTransaction();
                finish.setActionId(Long.valueOf(actionId));
                finish.setCustomerId(map.get("customerId"));
                finish.setStep(queue);
                finish.setStatus(callback.get("status"));
                finish.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                r2dbcEntityTemplate.insert(finish).subscribe();

                log.info("step: {}, trx: {}", queue, finish);

                if (callback.get("status").equalsIgnoreCase(OrderStatus.FAILED.toString()))
                    throw new RuntimeException("Process failed!");
            }
        });

        log.info("orderStep: {}", orderStep.collectList().block());
    }

    @JmsListener(destination = "queue.complete")
    private void complete(Message<LinkedHashMap<String, String>> message) {
        LinkedHashMap<String, String> object = message.getPayload();
        object.put("status", OrderStatus.CREATED.toString());
        jmsTemplate.convertAndSend("queue.order.callback", object);
    }
}
