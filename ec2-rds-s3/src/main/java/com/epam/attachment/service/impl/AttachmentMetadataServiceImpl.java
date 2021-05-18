package com.epam.attachment.service.impl;

import com.epam.attachment.api.FileStorage;
import com.epam.attachment.converter.AttachmentMetadataConverter;
import com.epam.attachment.dto.AttachmentMetadataDto;
import com.epam.attachment.model.AttachmentMetadata;
import com.epam.attachment.repository.AttachmentMetadataRepository;
import com.epam.attachment.service.AttachmentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentMetadataServiceImpl implements AttachmentMetadataService {

    private final AttachmentMetadataRepository repository;
    private final AttachmentMetadataConverter converter;
    private final FileStorage fileStorage;

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentMetadataDto> findAll() {
        var metadata = repository.findAll();
        return metadata.stream().map(converter::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttachmentMetadataDto create(MultipartFile multipartFile) {
        var filename = multipartFile.getOriginalFilename();
        var size = multipartFile.getSize();
        fileStorage.upload(multipartFile);
        var metadata = repository.save(AttachmentMetadata.builder()
                .fileName(filename)
                .fileSize(size)
                .build());
        return converter.toDto(metadata);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long id) {
        var metadata = findById(id);
        return fileStorage.download(metadata.getFileName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var metadata = findById(id);
        repository.delete(metadata);
        fileStorage.delete(metadata.getFileName());
    }

    private AttachmentMetadata findById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
