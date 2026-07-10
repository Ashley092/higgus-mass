package io.higgus.lab.module.storage.service.edition.impl;

import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationContentDO;
import io.higgus.lab.module.storage.dal.dataobject.edition.ContentSnapshotDO;
import io.higgus.lab.module.storage.dal.dataobject.edition.EditionLogDO;
import io.higgus.lab.module.storage.dal.mysql.EditionLogMapper;
import io.higgus.lab.module.storage.dal.mysql.collab.ContentMetadataMapper;
import io.higgus.lab.module.storage.service.collab.FileStorageService;
import io.higgus.lab.module.storage.service.edition.ContentSnapshotService;
import io.higgus.lab.module.storage.service.edition.EditionPoiService;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class EditionPoiServiceImpl implements EditionPoiService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EditionLogMapper logMapper;
    @Autowired
    private ContentMetadataMapper contentMetadataMapper;
    @Autowired
    private FileStorageService fileService;
    @Autowired
    private ContentSnapshotService snapshotService;

    @Override
    public List<EditionLogDO> rewireLog(EditionExcelSaveLogDto dto) {
        Integer snapVersion = dto.getContentSnapshotVersion();
        Long id = toLongType(dto.getContentId());
        List<EditionLogDO> list = logMapper.selectBySnapshotVersion(id, snapVersion);
        list.sort(Comparator.comparingInt(EditionLogDO::getVersion));
        return list;
    }

    @Override
    public void tryGenerateNewSnapshotFile(EditionExcelSaveLogDto dto) {
        logger.info("[4.1] 对象 {}", dto);
        String contentId = dto.getContentId();
        Long id = toLongType(contentId);
        CollaborationContentDO meta = contentMetadataMapper.selectById(id);
        if (meta == null) {
            return;
        }
        String storageKey = meta.getCurrentStorageKey();
        if (storageKey == null || storageKey.isEmpty()) {
            logger.warn("[4.1] contentId={} 的 currentStorageKey 为空，跳过快照生成", contentId);
            return;
        }
        List<EditionLogDO> logList = rewireLog(dto);
        byte[] excelBytes = fileService.downloadAsBytes(storageKey);
        byte[] newExcelBytes = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(excelBytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Workbook workbook = WorkbookFactory.create(is)
            ) {
            // 之后再改，现在先硬编码
            Sheet sheet = workbook.getSheetAt(0);
            for (EditionLogDO logDO : logList) {
                Integer rowIndex = logDO.getRowIndex();
                Integer colIndex = logDO.getColIndex();
                String newContent = logDO.getNewValue();
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                Cell cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.createCell(colIndex);
                }
                cell.setCellValue(newContent);
            workbook.write(baos);
            newExcelBytes = baos.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 这里发现一个问题。逻辑上有问题。要改一下原本的数据结构字段内容。
        // 一个文件对应多个快照版本的话，不能发生覆盖。
        ContentSnapshotDO snapDO = snapshotService.createSnapshotTemp(id, dto.getUpdater());
        snapshotService.finalizeSnapshot(snapDO.getId(), newExcelBytes);

    }


    private Long toLongType(String id) {
        return Long.valueOf(id);
    }
}
