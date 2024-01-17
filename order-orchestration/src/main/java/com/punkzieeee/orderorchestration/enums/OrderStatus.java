package com.punkzieeee.orderorchestration.enums;

public enum OrderStatus {
    // status di log/response
    INIT,
    CREATED, 
    FAILED, 
    // status di db
    ACTIVE, 
    INACTIVE
}
