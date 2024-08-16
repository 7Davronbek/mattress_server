package com.alibou.security.admin;


import com.alibou.security.mattress.MattressService;
import com.alibou.security.mattress.dto.MattressCreateDto;
import com.alibou.security.mattress.entities.mattress_size.MattressSizeService;
import com.alibou.security.mattress.entities.mattress_size.entity.MattressSize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final MattressService mattressService;
    private final MattressSizeService mattressSizeService;

    @PostMapping("/mattress")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> getMattresses(@ModelAttribute MattressCreateDto mattressCreateDto) throws Exception {
        if (mattressService.createMattress(mattressCreateDto))
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }

    @PutMapping("/mattress/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> updateMattress(@PathVariable UUID id, @ModelAttribute MattressCreateDto mattressUpdateDto) throws IOException {
        if (mattressService.updateMattress(id, mattressUpdateDto))
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }

    @DeleteMapping("/mattress/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deleteMattress(@PathVariable UUID id) {
        mattressService.deleteMattress(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted");
    }

    @DeleteMapping("/mattress-size/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deleteMattressSize(@PathVariable UUID id) {
        mattressSizeService.deleteMattressSize(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted");
    }
}














