package io.higgus.lab.module.storage.controller.collab;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.controller.collab.vo.collab.content.CollaborationContentUpdateReqVO;
import io.higgus.lab.module.storage.service.collab.CollaborationContentFacadeService;
import io.higgus.lab.module.storage.service.collab.CollaborationContentService;
import io.higgus.lab.module.storage.service.collab.FileStorageService;
import io.higgus.lab.module.storage.controller.collab.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.controller.collab.vo.ContentUploadReqVO;
import io.higgus.lab.module.storage.controller.collab.vo.UploadResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "内容元数据管理")
@Slf4j
@RestController
@RequestMapping("/collaboration/content")
public class CollaborationContentController {

    @Resource
    private CollaborationContentService contentMetadataService;

    @Resource
    private CollaborationContentFacadeService collaborationContentFacadeService;

    @Resource
    private FileStorageService fileStorageService;

    @Operation(summary = "上传文件（后端计算MD5，支持秒传）")
    @PostMapping("/upload")
    public CommonResult<UploadResultVO> uploadFile(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Valid ContentUploadReqVO reqVO,
            @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "0") Long creator) throws Exception {
        log.info("文件上传请求, filename={}, size={}, itemId={}", file.getOriginalFilename(), file.getSize(), reqVO.getItemId());
        return CommonResult.success(collaborationContentFacadeService.uploadFile(file, reqVO, creator));
    }

//    @Operation(summary = "创建内容元数据（纯数据，不上传文件）")
//    @PostMapping("/create")
//    public CommonResult<Long> createContent(
//            @Valid @RequestBody io.higgus.lab.module.storage.controller.vo.ContentMetadataCreateReqVO createReqVO,
//            @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "0") Long creator) {
//        log.info("创建内容元数据, title={}, contentType={}", createReqVO.getTitle(), createReqVO.getContentType());
//        return CommonResult.success(contentMetadataService.create(createReqVO, creator));
//    }

    @Operation(summary = "更新内容元数据")
    @PutMapping("/update")
    public CommonResult<Boolean> updateContent(
            @Valid @RequestBody CollaborationContentUpdateReqVO updateReqVO,
            @Parameter(description = "更新者") @RequestParam(value = "updater", defaultValue = "0") Long updater) {
        log.info("更新内容元数据, id={}", updateReqVO.getId());
        contentMetadataService.update(updateReqVO, updater);
        return CommonResult.success(true);
    }

    @Operation(summary = "删除内容（同时删除文件和元数据）")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteContent(@Parameter(description = "内容ID") @RequestParam("id") Long id) {
        log.info("删除内容, id={}", id);
        collaborationContentFacadeService.deleteContent(id);
        return CommonResult.success(true);
    }

    @Operation(summary = "获取内容元数据")
    @GetMapping("/get")
    public CommonResult<ContentMetadataRespVO> getContent(@Parameter(description = "内容ID") @RequestParam("id") Long id) {
        return CommonResult.success(contentMetadataService.get(id));
    }

    @Operation(summary = "获取内容元数据列表")
    @GetMapping("/list")
    public CommonResult<List<ContentMetadataRespVO>> getContentList() {
        return CommonResult.success(contentMetadataService.getList());
    }

    @Operation(summary = "获取项目下的内容元数据列表")
    @GetMapping("/list-by-item")
    public CommonResult<List<ContentMetadataRespVO>> getContentListByItemId(@Parameter(description = "项目ID") @RequestParam("itemId") Long itemId) {
        return CommonResult.success(contentMetadataService.getListByItemId(itemId));
    }

    @Operation(summary = "获取项目下指定类型的内容元数据列表")
    @GetMapping("/list-by-item-type")
    public CommonResult<List<ContentMetadataRespVO>> getContentListByItemIdAndType(
            @Parameter(description = "项目ID") @RequestParam("itemId") Long itemId,
            @Parameter(description = "内容类型") @RequestParam("contentType") Integer contentType) {
        return CommonResult.success(contentMetadataService.getListByItemIdAndType(itemId, contentType));
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    public void downloadFile(
            @Parameter(description = "内容ID") @RequestParam("id") Long id,
            HttpServletResponse response) {
        // 1. 获取元数据
        ContentMetadataRespVO content = contentMetadataService.get(id);
        if (content == null) {
            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("文件不存在");
            } catch (Exception e) {
                log.error("写入下载响应失败", e);
            }
            return;
        }

        // 2. 获取文件流
        ResponseInputStream<GetObjectResponse> fileStream = fileStorageService.download(content.getCurrentStorageKey());
        if (fileStream == null) {
            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("文件不存在");
            } catch (Exception e) {
                log.error("写入下载响应失败", e);
            }
            return;
        }

        try {
            // 3. 从 MinIO 响应元数据中取真实文件大小（不能信任 DB 中存储的 fileSize）
        // 使用 HeadObject 查询真实大小，避免 Content-Length 与实际文件不一致导致下载卡住
        HeadObjectResponse headObject = fileStorageService.headObject(content.getCurrentStorageKey());
        long actualFileSize = headObject.contentLength();
        String fileName = content.getTitle();
        // 处理文件名编码
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        // 如果有文件后缀，加上后缀
        if (content.getFileExtension() != null && !content.getFileExtension().isEmpty()) {
            fileName = fileName + "." + content.getFileExtension();
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        }

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
        response.setHeader("Content-Length", String.valueOf(actualFileSize));

        // 4. 写入响应
        org.springframework.util.StreamUtils.copy(fileStream, response.getOutputStream());
        response.flushBuffer();
        log.info("文件下载成功, id={}, title={}, fileSize={}", id, content.getTitle(), actualFileSize);
        } catch (IOException e) {
            log.error("文件下载失败, id={}", id, e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置响应状态失败", ex);
            }
        }
    }
}
