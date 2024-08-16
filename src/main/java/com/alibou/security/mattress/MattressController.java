package com.alibou.security.mattress;

import com.alibou.security.mattress.dto.MattressResponseDto;
import com.alibou.security.mattress.dto.MattressWithSizeResponseDto;
import com.alibou.security.mattress.entities.Mattress;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@RestController
public class MattressController {
    private final MattressService mattressService;

    @GetMapping("/mattress")
    public ResponseEntity<List<MattressResponseDto>> getMattresses() {
        return ResponseEntity.ok(mattressService.getMattresses());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileId) {
        Mattress image = mattressService.getImage(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getFileName())
                .body(new ByteArrayResource(image.getBytes()));
    }

    @GetMapping("/mattress/{id}")
    public ResponseEntity<MattressResponseDto> getMattress(@PathVariable UUID id) {
        MattressResponseDto mattress = mattressService.getMattress(id);
        return ResponseEntity.ok(mattress);
    }
}
