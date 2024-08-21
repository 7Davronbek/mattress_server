package com.alibou.security.mattress.entities.mattress_size.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MattressSizeResponse {
    private UUID id;
    private String size;
    private int price;
    private UUID mattressId;
}
