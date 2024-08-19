package com.alibou.security.mattress;

import com.alibou.security.exception.ResourceNotFoundException;
import com.alibou.security.mattress.dto.MattressCreateDto;
import com.alibou.security.mattress.dto.MattressResponseDto;
import com.alibou.security.mattress.dto.MattressWithSizeResponseDto;
import com.alibou.security.mattress.entities.Mattress;
import com.alibou.security.mattress.entities.mattress_size.MattressSizeRepository;
import com.alibou.security.mattress.entities.mattress_size.dto.MattressSizeCreateDto;
import com.alibou.security.mattress.entities.mattress_size.dto.MattressSizeResponse;
import com.alibou.security.mattress.entities.mattress_size.entity.MattressSize;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MattressService {
    private final MattressRepository mattressRepository;
    private final MattressSizeRepository mattressSizeRepository;

    public List<MattressResponseDto> getMattresses() {
        List<Mattress> mattresses = mattressRepository.findAll();

        return mattresses.stream().map(mattress -> {
            String uriString = uriString(mattress.getId().toString());
            return new MattressResponseDto(
                    mattress.getId(),
                    mattress.getName(),
                    mattress.getDescription(),
                    uriString,
                    mattress.getMattressType()
            );
        }).toList();
    }

    public boolean createMattress(MattressCreateDto mattressCreateDto) throws Exception {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mattressCreateDto.getFile().getOriginalFilename()));

            if (fileName.contains("..") || fileName.isBlank()) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }

            Mattress mattress = new Mattress(
                    UUID.randomUUID(),
                    mattressCreateDto.getName(),
                    mattressCreateDto.getDescription(),
                    mattressCreateDto.getMattressType(),
                    mattressCreateDto.getFile().getBytes(),
                    mattressCreateDto.getFile().getContentType(),
                    fileName,
                    null
            );

            mattressRepository.save(mattress);

            if (!mattressCreateDto.getSizes().isEmpty()) {
                for (MattressSizeCreateDto mattressSizeCreateDto : mattressCreateDto.getSizes()) {
                    MattressSize mattressSize = new MattressSize(UUID.randomUUID(), mattressSizeCreateDto.getSize(), mattressSizeCreateDto.getPrice(), mattress);
                    mattressSizeRepository.save(mattressSize);
                }
            }

            return true;

        } catch (Exception e) {
            throw new Exception("Could not save the file " + e);
        }
    }

    public boolean updateMattress(UUID id, MattressCreateDto mattressUpdateDto) throws IOException {
        Mattress mattress = mattressRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find mattress with id - " + id));

        mattress.setName(mattressUpdateDto.getName());
        mattress.setDescription(mattressUpdateDto.getDescription());
        mattress.setMattressType(mattressUpdateDto.getMattressType());

        mattress.setBytes(mattressUpdateDto.getFile().getBytes());
        mattress.setFileType(mattressUpdateDto.getFile().getContentType());
        mattress.setFileName(mattressUpdateDto.getFile().getOriginalFilename());

        mattressRepository.save(mattress);

        if ((mattressUpdateDto.getSizes() != null && mattress.getSizes() != null) && mattressUpdateDto.getSizes().size() > mattress.getSizes().size()) {
            for (MattressSizeCreateDto mattressSizeUpdateDto : mattressUpdateDto.getSizes()) {
                MattressSize mattressSize = new MattressSize(UUID.randomUUID(), mattressSizeUpdateDto.getSize(), mattressSizeUpdateDto.getPrice(), mattress);
                mattressSizeRepository.save(mattressSize);
            }
        }
        return true;
    }

    public MattressResponseDto getMattress(UUID id) {
        Mattress mattress = mattressRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find mattress with id - " + id));

        return new MattressResponseDto(
                mattress.getId(),
                mattress.getName(),
                mattress.getDescription(),
                uriString(mattress.getId().toString()),
                mattress.getMattressType()
        );
    }

    private String uriString(String id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/public/download/")
                .path(id)
                .toUriString();
    }

    public Mattress getImage(String fileId) {
        return mattressRepository
                .findById(UUID.fromString(fileId))
                .orElseThrow(() -> new ResourceNotFoundException("Could not find mattress with id - " + fileId));
    }

    public void deleteMattress(UUID id) {
        List<MattressSize> sizes = mattressSizeRepository.findAll();
        for (MattressSize size : sizes) {
            if (size.getMattress().getId().equals(id)) {
                mattressSizeRepository.delete(size);
            }
        }
        mattressRepository.deleteById(id);
    }

    public List<MattressSizeResponse> getMattressSizes() {
        List<MattressSize> sizes = mattressSizeRepository.findAll();

        return sizes.stream().map(size -> {
            return new MattressSizeResponse(
                    size.getId(),
                    size.getSize(),
                    size.getPrice()
            );
        }).toList();
    }
}


