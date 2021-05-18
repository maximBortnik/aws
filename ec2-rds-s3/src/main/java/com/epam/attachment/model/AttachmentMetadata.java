package com.epam.attachment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attachment_metadata")
public class AttachmentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_size")
    private long fileSize;
    @LastModifiedDate
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;
    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
}
