package com.punkzieeee.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrchestratorRequestDto {
    private String customerId;
    private String username;
    private String password;
}
