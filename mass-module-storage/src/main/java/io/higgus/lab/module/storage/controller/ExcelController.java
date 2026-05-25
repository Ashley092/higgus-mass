package io.higgus.lab.module.storage.controller;

import io.higgus.lab.module.storage.service.ExcelStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelStorageService excelService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "folder", defaultValue = "default/") String folder) {
        try {
            String objectKey = excelService.uploadExcel(file, folder);
            return "Upload success: " + objectKey;
        } catch (Exception e) {
            return "Upload failed: " + e.getMessage();
        }
    }

    @GetMapping("/download/{objectKey}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String objectKey) {
        InputStream inputStream = excelService.downloadExcel(objectKey);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + objectKey + "\"")
                .body(new InputStreamResource(inputStream));
    }
}