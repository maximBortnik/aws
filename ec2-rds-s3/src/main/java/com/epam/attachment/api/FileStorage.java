package com.epam.attachment.api;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    void upload(MultipartFile multipartFile);
    Resource download(String fileName);
    void  delete(String fileName);
}
