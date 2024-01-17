package com.punkzieeee.order.service;

import com.punkzieeee.order.dto.OrderActionDto;

public interface OrderService {
    String makeQueueOrder(OrderActionDto dto);
}
