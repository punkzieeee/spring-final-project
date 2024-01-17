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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkzieeee.orderorchestration.repository.OrderStepRepository;

// @ExtendWith(MockitoExtension.class)
// @TestInstance(Lifecycle.PER_CLASS)
public class OrchestratorServiceTest {

    // @Mock
    // OrderStepRepository orderStepRepository;

    // @InjectMocks
    // OrchestratorService orchestratorService;

    // @Autowired
    // JmsTemplate jmsTemplate;

    // private ObjectMapper objectMapper = new ObjectMapper();
    // LinkedHashMap<String, Object> objMsg;
    // LinkedHashMap<String, String> objMap;
    // Message<String> message;
    // String objDto;

    // @BeforeAll
    // void setup() {
    //     objMap = new LinkedHashMap<>();
    //     objMap.put("action", "register");
    //     objMap.put("data", "{}");

    //     objMsg = new LinkedHashMap<>();
    //     objMsg.put("actionId", "1");
    //     objMsg.put("data", objMap);
    // }

    // @Test
    // void testRegister() throws Exception {
    //     objDto = objectMapper.writeValueAsString(objMsg);
    //     message = new GenericMessage<String>(objDto);
        
    //     // layer 1
    //     LinkedHashMap<String, Object> objMessage = (LinkedHashMap<String, Object>) objectMapper
    //             .readValue(message.getPayload(), LinkedHashMap.class);
    //     LinkedHashMap<String, Object> mapMessage = (LinkedHashMap<String, Object>) objMessage.get("data");
    //     Integer actionId = (Integer) objMessage.get("actionId");

    //     // layer 2
    //     String objectAction = (String) mapMessage.get("action");
    //     Object objectMap = mapMessage.get("data");
    //     LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) objectMap;

    //     lenient().when(orderStepRepository.findByActionIdOrderByPriority(Long.valueOf(actionId)))
    //         .thenReturn(any());

    //     orchestratorService.register(message);
    // }
}
