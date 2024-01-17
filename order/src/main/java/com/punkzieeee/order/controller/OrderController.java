package com.punkzieeee.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.punkzieeee.order.dto.OrderActionDto;
import com.punkzieeee.order.service.OrderService;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @PostMapping("/create")
    public String makeQueueOrder(@RequestBody OrderActionDto dto) {
        return orderService.makeQueueOrder(dto);
    }
}
