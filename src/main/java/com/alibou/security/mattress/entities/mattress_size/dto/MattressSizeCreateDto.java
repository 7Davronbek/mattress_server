package com.alibou.security.mattress.entities.mattress_size.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MattressSizeCreateDto {
    private String size;
    private int price;
}
