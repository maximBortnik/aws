package com.epam.attachment.converter;

import com.epam.attachment.dto.AttachmentMetadataDto;
import com.epam.attachment.model.AttachmentMetadata;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMetadataConverter {
    public AttachmentMetadataDto toDto(AttachmentMetadata attachmentMetadata) {
        return AttachmentMetadataDto.builder()
                .id(attachmentMetadata.getId())
                .fileName(attachmentMetadata.getFileName())
                .fileSize(attachmentMetadata.getFileSize())
                .build();
    }
}
