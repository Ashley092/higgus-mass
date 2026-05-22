package io.higgus.lab.module.infra.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSpaceService {



    default String calculateMd5(MultipartFile file) {
        return null;
    }


    default void doUploadBusiness(MultipartFile file , String md5, Long userId, Long parentId) {}
}
