package io.higgus.lab.module.storage.tool;

import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class HiFileTools {


    public String calculateFileMd5(MultipartFile file) throws IOException {
        return SecureUtil.md5(file.getInputStream());
    }
}
