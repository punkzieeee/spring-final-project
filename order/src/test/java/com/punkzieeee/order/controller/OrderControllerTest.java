package com.punkzieeee.order.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.punkzieeee.order.dto.OrderActionDto;
import com.punkzieeee.order.dto.OrderRequestDto;
import com.punkzieeee.order.service.OrderService;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@WebFluxTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    OrderService orderService;

    OrderActionDto orderActionDto;
    OrderRequestDto dto;
    String response;

    @BeforeAll
    void setup() {
        dto = new OrderRequestDto();
        dto.setCustomerId("0");
        dto.setUsername("username");
        dto.setPassword("password");

        orderActionDto = new OrderActionDto();
        orderActionDto.setAction("register");
        orderActionDto.setData(dto);

        response = "On process...";
    }

    @Test
    void testMakeQueueOrder() {
        when(orderService.makeQueueOrder(orderActionDto)).thenReturn(response);
        webTestClient.post()
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(orderActionDto)
                .exchange()
                .expectBody(String.class);
    }
}
