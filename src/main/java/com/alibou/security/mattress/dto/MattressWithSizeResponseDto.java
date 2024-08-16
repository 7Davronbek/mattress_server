package com.alibou.security.mattress.dto;

import com.alibou.security.mattress.entities.MattressType;
import com.alibou.security.mattress.entities.mattress_size.dto.MattressSizeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MattressWithSizeResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private MattressType mattressType;
    private List<MattressSizeResponse> sizes;
}
