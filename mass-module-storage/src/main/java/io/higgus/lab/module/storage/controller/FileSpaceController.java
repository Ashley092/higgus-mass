package io.higgus.lab.module.storage.controller;


import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.service.FileSpaceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/file/space")
public class FileSpaceController {


    @Resource
    FileSpaceService fileSpaceService;

    @PostMapping("/uploadSingleFile")
    public CommonResult<Boolean> uploadSingleFile(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("userId") Long userId,
                                                  @RequestParam("parentId") Long parentId) {

        fileSpaceService.uploadSingleFile(file, parentId, userId);
    }

}
