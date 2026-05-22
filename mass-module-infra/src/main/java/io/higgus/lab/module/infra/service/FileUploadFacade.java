package io.higgus.lab.module.infra.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadFacade {

    void executeUpload(MultipartFile file, Long userId, Long parentId) throws InterruptedException;
}
