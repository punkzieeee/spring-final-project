package com.punkzieeee.orderorchestration.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.orderorchestration.model.OrderStep;
import com.punkzieeee.orderorchestration.repository.OrderStepRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class OrchestratorServiceTest {

    @Mock
    OrderStepRepository orderStepRepository;

    @Mock
    JmsTemplate jmsTemplate;
    
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    OrchestratorService orchestratorService;

    LinkedHashMap<String, Object> objMsg;
    LinkedHashMap<String, Object> objMap;
    LinkedHashMap<String, String> objActionDto;
    OrderStep orderStep;
    String objDto;

    @BeforeAll
    void setup() {
        orchestratorService = new OrchestratorService(jmsTemplate, objectMapper);

        objActionDto = new LinkedHashMap<>();
        objActionDto.put("customerId", "0");
        objActionDto.put("username", "username");
        objActionDto.put("password", "password");

        objMap = new LinkedHashMap<>();
        objMap.put("action", "register");
        objMap.put("data", objActionDto);

        objMsg = new LinkedHashMap<>();
        objMsg.put("actionId", "1");
        objMsg.put("data", objMap);

        orderStep = new OrderStep();
    }

    @Test
    void testRegister() throws Exception {
        log.info("objMsg: {}", objMsg);
        objDto = objectMapper.writeValueAsString(objMsg);
        log.info("objDto: {}", objDto);
        Message<String> message = new GenericMessage<String>(objDto);

        // layer 1
        LinkedHashMap<String, Object> objMessage = (LinkedHashMap<String, Object>) objectMapper
                .readValue(message.getPayload(), LinkedHashMap.class);
        LinkedHashMap<String, Object> mapMessage = (LinkedHashMap<String, Object>) objMessage.get("data");
        Integer actionId = Integer.parseInt((String) objMessage.get("actionId"));

        // layer 2
        String objectAction = (String) mapMessage.get("action");
        Object objectMap = mapMessage.get("data");
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) objectMap;

        lenient().when(orderStepRepository.findByActionIdOrderByPriority(Long.valueOf(actionId)))
            .thenReturn(Flux.just(orderStep));

        // StepVerifier.create(orderStepRepository.findByActionIdOrderByPriority(Long.valueOf(actionId)).collectList())
        // .expectComplete();

        orchestratorService.register(message);
    }
}
