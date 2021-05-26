package com.epam.attachment.controller;

import com.epam.attachment.dto.AttachmentMetadataDto;
import com.epam.attachment.service.AttachmentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentMetadataController {

    private final AttachmentMetadataService service;

    @GetMapping
    public ResponseEntity<List<AttachmentMetadataDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentMetadataDto> upload(@RequestParam("file") MultipartFile multipartFile) {
        var attachmentMetadataDto = service.create(multipartFile);
        return new ResponseEntity<>(attachmentMetadataDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        return new ResponseEntity<>(service.download(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
