package io.higgus.lab.module.storage.service.editionOld;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveRespVO;
import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationContentDO;
import io.higgus.lab.module.storage.dal.mysql.collab.ContentMetadataMapper;
import io.higgus.lab.module.storage.service.collab.FileStorageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class EditionExcelServiceImpl implements EditionExcelService {

    private static final int MAX_RETRY = 3;

    @Resource
    private ContentMetadataMapper contentMetadataMapper;

    @Resource
    private FileStorageService fileStorageService;


    /**
     * 使用乐观锁进行并发控制。
     * @param reqVO 变更请求（行号、列号从 0 开始）
     * @return
     */
    @Override
    public EditionExcelSaveRespVO saveExcel(EditionExcelSaveReqVO reqVO) {
        Long contentId = Long.valueOf(reqVO.getContentId());
        Integer reqVersion = reqVO.getReversion();
        int rowIndex = reqVO.getRow();
        int colIndex = reqVO.getCol();
        String newContent = reqVO.getNewContent();

        log.info("开始保存 Excel, contentId={}, row={}, col={}, version={}",
                contentId, rowIndex, colIndex, reqVersion);

        // 乐观锁重试机制
        for (int i = 0; i < MAX_RETRY; i++) {
            try {
                return doSave(contentId, reqVersion, rowIndex, colIndex, newContent);
            } catch (VersionConflictException e) {
                log.warn("版本冲突，重试第 {} 次", i + 1);
                if (i == MAX_RETRY - 1) {
                    return EditionExcelSaveRespVO.fail("文件已被他人修改，请刷新后重试",
                            EditionExcelSaveRespVO.ErrorType.VERSION_CONFLICT);
                }
            } catch (Exception e) {
                log.error("保存 Excel 失败", e);
                return EditionExcelSaveRespVO.fail("保存失败: " + e.getMessage(),
                        EditionExcelSaveRespVO.ErrorType.UNKNOWN_ERROR);
            }
        }

        return EditionExcelSaveRespVO.fail("未知错误", EditionExcelSaveRespVO.ErrorType.UNKNOWN_ERROR);
    }

    private EditionExcelSaveRespVO doSave(Long contentId, Integer reqVersion,
                                          int rowIndex, int colIndex, String newContent) throws Exception {
        // 1. 查询元数据
        CollaborationContentDO meta = contentMetadataMapper.selectById(contentId);
        if (meta == null) {
            return EditionExcelSaveRespVO.fail("文件不存在", EditionExcelSaveRespVO.ErrorType.FILE_NOT_FOUND);
        }

        String storageKey = meta.getStorageKey();
        Integer currentVersion = meta.getVersion();

        // 2. 检查版本（乐观锁检查）
        if (!currentVersion.equals(reqVersion)) {
            throw new VersionConflictException("版本冲突");
        }
        // 3. 从 MinIO 下载文件
        byte[] excelBytes = fileStorageService.downloadAsBytes(storageKey);

        // 4. 用 POI 解析并修改
        byte[] newExcelBytes;
        try (ByteArrayInputStream is = new ByteArrayInputStream(excelBytes);
             Workbook workbook = WorkbookFactory.create(is)) {

            // 获取第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 获取或创建行（POI 行索引从 0 开始）
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            // 获取或创建单元格（POI 列索引从 0 开始）
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }

            // 设置新值（统一存为字符串）
            cell.setCellValue(newContent);

            // 5. 写入字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            newExcelBytes = baos.toByteArray();
        }

        // 6. 上传回 MinIO（覆盖原文件）
        fileStorageService.upload(newExcelBytes, storageKey);

        // 7. 更新 version（乐观锁更新）
        LambdaUpdateWrapper<CollaborationContentDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CollaborationContentDO::getId, contentId)
                .eq(CollaborationContentDO::getVersion, reqVersion)
                .set(CollaborationContentDO::getVersion, reqVersion + 1)
                .set(CollaborationContentDO::getUpdater, 0L); // TODO: 获取真实用户

        int rows = contentMetadataMapper.update(null, updateWrapper);
        if (rows == 0) {
            throw new VersionConflictException("更新版本号失败，版本已变更");
        }

        log.info("Excel 保存成功, contentId={}, newVersion={}", contentId, reqVersion + 1);
        return EditionExcelSaveRespVO.success(reqVersion + 1);
    }

    /**
     * 版本冲突异常
     */
    private static class VersionConflictException extends RuntimeException {
        public VersionConflictException(String message) {
            super(message);
        }
    }
}
