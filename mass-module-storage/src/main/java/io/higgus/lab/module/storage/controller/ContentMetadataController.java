package io.higgus.lab.module.storage.controller;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.service.ContentFacadeService;
import io.higgus.lab.module.storage.service.ContentMetadataService;
import io.higgus.lab.module.storage.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.vo.ContentUploadReqVO;
import io.higgus.lab.module.storage.vo.UploadResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "内容元数据管理")
@Slf4j
@RestController
@RequestMapping("/content/metadata")
public class ContentMetadataController {

    @Resource
    private ContentMetadataService contentMetadataService;

    @Resource
    private ContentFacadeService contentFacadeService;

    @Operation(summary = "上传文件（后端计算MD5，支持秒传）")
    @PostMapping("/upload")
    public CommonResult<UploadResultVO> uploadFile(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Valid ContentUploadReqVO reqVO,
            @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "0") Long creator) throws Exception {
        log.info("文件上传请求, filename={}, size={}, itemId={}", file.getOriginalFilename(), file.getSize(), reqVO.getItemId());
        return CommonResult.success(contentFacadeService.uploadFile(file, reqVO, creator));
    }

    @Operation(summary = "创建内容元数据（纯数据，不上传文件）")
    @PostMapping("/create")
    public CommonResult<Long> createContent(
            @Valid @RequestBody io.higgus.lab.module.storage.vo.ContentMetadataCreateReqVO createReqVO,
            @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "0") Long creator) {
        log.info("创建内容元数据, title={}, contentType={}", createReqVO.getTitle(), createReqVO.getContentType());
        return CommonResult.success(contentMetadataService.create(createReqVO, creator));
    }

    @Operation(summary = "更新内容元数据")
    @PutMapping("/update")
    public CommonResult<Boolean> updateContent(
            @Valid @RequestBody io.higgus.lab.module.storage.vo.ContentMetadataUpdateReqVO updateReqVO,
            @Parameter(description = "更新者") @RequestParam(value = "updater", defaultValue = "0") Long updater) {
        log.info("更新内容元数据, id={}", updateReqVO.getId());
        contentMetadataService.update(updateReqVO, updater);
        return CommonResult.success(true);
    }

    @Operation(summary = "删除内容（同时删除文件和元数据）")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteContent(@Parameter(description = "内容ID") @RequestParam("id") Long id) {
        log.info("删除内容, id={}", id);
        contentFacadeService.deleteContent(id);
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
}
