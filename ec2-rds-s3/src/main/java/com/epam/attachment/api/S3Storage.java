package com.epam.attachment.api;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Storage implements FileStorage {

    private final AmazonS3Client s3Client;

    @Value("${bucket.name}")
    private String bucketName;

    @Override
    @SneakyThrows
    public void upload(MultipartFile multipartFile) {
        var filename = multipartFile.getOriginalFilename();
        s3Client.putObject(bucketName, filename, multipartFile.getInputStream(), new ObjectMetadata());
    }

    @Override
    @SneakyThrows
    public Resource download(String fileName) {
        var expirationDateTime = LocalDateTime.now().plusMinutes(5);
        var presignedUrl = s3Client.generatePresignedUrl(bucketName, fileName, Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        return new UrlResource(presignedUrl);
    }

    @Override
    public void delete(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}
