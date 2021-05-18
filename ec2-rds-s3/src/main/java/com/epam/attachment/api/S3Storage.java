package com.epam.attachment.api;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Storage implements FileStorage {

    private final AmazonS3Client s3Client;

    @Override
    @SneakyThrows
    public void upload(MultipartFile multipartFile) {
        var filename = multipartFile.getOriginalFilename();
        s3Client.putObject("task7", filename, multipartFile.getInputStream(), new ObjectMetadata());
    }

    @Override
    @SneakyThrows
    public Resource download(String fileName) {
        var uri = s3Client.getUrl("task7", fileName);
        return new UrlResource(uri);
    }

    @Override
    public void delete(String fileName) {
        s3Client.deleteObject("task7", fileName);
    }
}
