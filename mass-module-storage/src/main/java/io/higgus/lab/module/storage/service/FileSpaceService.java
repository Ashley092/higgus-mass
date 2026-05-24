package io.higgus.lab.module.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSpaceService {

    void uploadSingleFile(MultipartFile file, Long parentId, Long userId);
}
