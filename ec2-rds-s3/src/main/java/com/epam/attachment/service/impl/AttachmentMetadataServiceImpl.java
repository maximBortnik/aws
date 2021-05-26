package com.epam.attachment.service.impl;

import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersRequest;
import com.epam.attachment.api.FileStorage;
import com.epam.attachment.converter.AttachmentMetadataConverter;
import com.epam.attachment.dto.AttachmentMetadataDto;
import com.epam.attachment.model.AttachmentMetadata;
import com.epam.attachment.notification.dto.AttachmentNotification;
import com.epam.attachment.notification.dto.NotificationType;
import com.epam.attachment.notification.sqs.SQSSender;
import com.epam.attachment.repository.AttachmentMetadataRepository;
import com.epam.attachment.service.AttachmentMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentMetadataServiceImpl implements AttachmentMetadataService {

    private static final String DOWNLOAD_URL = "%s:%d/api/v1/attachments/%s/download";

    @Value("${server.port}")
    private int serverPort;

    private final AmazonElasticLoadBalancing  elasticLoadBalancing;
    private final AttachmentMetadataRepository repository;
    private final AttachmentMetadataConverter converter;
    private final FileStorage fileStorage;
    private final SQSSender sqsSender;

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
        sendUploadNotification(metadata);
        return converter.toDto(metadata);
    }

    private void sendUploadNotification(AttachmentMetadata attachmentMetadata) {
        var uploadNotification = AttachmentNotification.builder()
                .filename(attachmentMetadata.getFileName())
                .size(attachmentMetadata.getFileSize())
                .downloadLink(String.format(DOWNLOAD_URL, getLoadBalancerDncName(), serverPort, attachmentMetadata.getId()))
                .type(NotificationType.UPLOAD)
                .build();
        sqsSender.send(uploadNotification);
    }

    private String getLoadBalancerDncName() {
        var describeLoadBalancersRequest = new DescribeLoadBalancersRequest();
        describeLoadBalancersRequest.setNames(List.of("ApplicationLoadBalancer"));
        var loadBalancer = elasticLoadBalancing.describeLoadBalancers(describeLoadBalancersRequest)
                .getLoadBalancers()
                .get(0);
        log.info("Load Balancer with name: {} and dns: {}", loadBalancer.getLoadBalancerName(), loadBalancer.getDNSName());
        return loadBalancer.getDNSName();
    }

    private void sendDeleteNotification(AttachmentMetadata attachmentMetadata) {
        var uploadNotification = AttachmentNotification.builder()
                .filename(attachmentMetadata.getFileName())
                .size(attachmentMetadata.getFileSize())
                .type(NotificationType.DELETE)
                .build();
        sqsSender.send(uploadNotification);
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
        sendDeleteNotification(metadata);
    }

    private AttachmentMetadata findById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
