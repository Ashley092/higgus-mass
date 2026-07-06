package io.higgus.lab.module.storage.service.edition.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.higgus.lab.module.storage.controller.edition.vo.EditionLogRespVO;
import io.higgus.lab.module.storage.convert.edition.EditionExcelSaveLogConvert;
import io.higgus.lab.module.storage.dal.dataobject.edition.EditionLogDO;
import io.higgus.lab.module.storage.dal.mysql.EditionLogMapper;
import io.higgus.lab.module.storage.service.edition.EditionLogService;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 协作编辑日志 Service 实现
 *
 * TODO: 业务逻辑待实现
 */
@Slf4j
@Service
public class EditionLogServiceImpl implements EditionLogService {

    @Resource
    private EditionLogMapper editionLogMapper;

    // 转换器
    private static final EditionExcelSaveLogConvert editionConvert = EditionExcelSaveLogConvert.INSTANCE;

    @Override
    public EditionLogRespVO appendLog(EditionExcelSaveLogDto dto) {
        // 1. 映射转换对象
        EditionLogDO logDO = editionConvert.toEditionLogDO(dto);
        // 2. 插入数据库
        editionLogMapper.insert(logDO);
        // 3. 返回 RespVO
        return editionConvert.toEditionLogRespVO(logDO);
    }

    @Override
    public Integer getLatestVersion(String contentId) {
        Long longTypeID = toLongType(contentId);
        List<EditionLogDO> list = editionLogMapper.selectByContentId(longTypeID);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.get(0).getVersion();
    }

    @Override
    public List<EditionLogRespVO> getLogsByContentId(String contentId) {
        // 查询指定 contentId 的所有日志，按时间倒序
        Long longTypeID = toLongType(contentId);
        List<EditionLogDO> list = editionLogMapper.selectByContentId(longTypeID);
        return editionConvert.toListEditionLogRespVO(list);
    }

    @Override
    public List<EditionLogRespVO> getLogsByVersionRange(String contentId,
                                                        LocalDateTime startTime,
                                                        LocalDateTime endTime) {
        // 查询指定时间范围内的日志
        Long longTypeID = toLongType(contentId);
        List<EditionLogDO> list = editionLogMapper.selectByTimeRange(longTypeID, startTime, endTime);
        return editionConvert.toListEditionLogRespVO(list);
    }

    @Override
    public List<EditionLogRespVO> getLogByVersion(String contentId, Integer snapVersion) {
        Long longTypeID = toLongType(contentId);
        List<EditionLogDO> list = editionLogMapper.selectBySnapshotVersion(longTypeID, snapVersion);
        return editionConvert.toListEditionLogRespVO(list);
    }

    @Override
    public IPage<EditionLogRespVO> getHistory(String contentId, Integer pageNum, Integer pageSize) {
        // 分页查询编辑历史
        Long longTypeID = toLongType(contentId);
        Page<EditionLogDO> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<EditionLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EditionLogDO::getContentId, longTypeID)
                .orderByDesc(EditionLogDO::getVersion);
        IPage<EditionLogDO> doPage = editionLogMapper.selectPage(page, wrapper);
        Page<EditionLogRespVO> respPage = new Page<>(doPage.getCurrent(), doPage.getSize(), doPage.getTotal());
        respPage.setRecords(editionConvert.toListEditionLogRespVO(doPage.getRecords()));
        return respPage;
    }


    private Long toLongType(String id) {
        return Long.valueOf(id);
    }

}
