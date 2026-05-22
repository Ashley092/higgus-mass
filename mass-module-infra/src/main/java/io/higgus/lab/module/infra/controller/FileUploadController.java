package io.higgus.lab.module.infra.controller;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.infra.service.FileUploadFacade;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileUploadController {

    @Resource
    private FileUploadFacade fileUploadFacade;

    @PostMapping("/upload")
    public CommonResult<String> fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", defaultValue = "0") Long userId,
            @RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        
        try {
            fileUploadFacade.executeUpload(file, userId, parentId);
            return CommonResult.success("上传成功");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CommonResult.error(500, "上传被中断");
        } catch (Exception e) {
            return CommonResult.error(500, e.getMessage());
        }
    }
}
