package com.epam.attachment.repository;

import com.epam.attachment.model.AttachmentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentMetadataRepository extends JpaRepository<AttachmentMetadata, Long> {
}
