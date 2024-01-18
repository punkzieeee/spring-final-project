package com.punkzieeee.orderorchestration.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.punkzieeee.orderorchestration.model.OrderTransaction;

public interface OrderTransactionRepository extends R2dbcRepository<OrderTransaction, Long>{
    
}
