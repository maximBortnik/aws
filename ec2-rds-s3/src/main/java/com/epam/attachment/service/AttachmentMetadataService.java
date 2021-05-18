package com.epam.attachment.service;

import com.epam.attachment.dto.AttachmentMetadataDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentMetadataService {
    List<AttachmentMetadataDto> findAll();
    AttachmentMetadataDto create(MultipartFile multipartFile);
    Resource download(Long id);
    void delete(Long id);
}
