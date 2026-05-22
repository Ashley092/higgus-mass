package io.higgus.lab.module.infra.service;

import cn.hutool.crypto.SecureUtil;
import io.higgus.lab.module.infra.dal.dataobject.FileIndexDO;
import io.higgus.lab.module.infra.dal.mysql.FileIndexMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class FileSpaceServiceImpl implements FileSpaceService {


    @Resource
    FileIndexMapper<FileIndexDO> fileIndexMapper;

    @Override
    public String calculateMd5(MultipartFile file ) {
        try {
            String md = SecureUtil.md5(file.getInputStream());
            return md;
        } catch (IOException e) {
            throw new RuntimeException("计算 MD5 失败", e);
        }
    }

    @Override
    public void doUploadBusiness(MultipartFile file , String md5, Long userId, Long parentId) {

        try {

        } catch (Exception e) {
            throw new RuntimeException("上传数据库过程中出错了!!");
        } finally {

        }

    }
}
