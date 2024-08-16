package com.alibou.security.mattress.dto;

import com.alibou.security.mattress.entities.MattressType;
import com.alibou.security.mattress.entities.mattress_size.dto.MattressSizeCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MattressCreateDto {
    private String name;
    private String description;
    private MattressType mattressType;
    private MultipartFile file;
    private List<MattressSizeCreateDto> sizes;
}
