package io.higgus.lab.module.storage.service.edition;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveRespVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionHistoryRespVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionLogRespVO;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 协作编辑日志 Service 接口
 *
 * 业务逻辑由调用方实现，此接口仅定义方法签名
 */
public interface EditionLogService {




    EditionLogRespVO appendLog(EditionExcelSaveLogDto dto);

    /**
     * 获取内容的最新版本号
     *
     * @param contentId 内容ID
     * @return 最新版本号，如果没有记录则返回0
     */
    Integer getLatestVersion(String contentId);

    /**
     * 获取内容的所有编辑日志
     *
     * @param contentId 内容ID
     * @return 按时间倒序的日志列表
     */
    List<EditionLogRespVO> getLogsByContentId(String contentId);

    /**
     * 获取内容指定版本范围内的日志
     *
     * @param contentId  内容ID
     * @param startTime 起始版本（包含）
     * @param endTIme   结束版本（包含）
     * @return 日志列表
     */
    List<EditionLogRespVO> getLogsByVersionRange(String contentId, LocalDateTime startTime, LocalDateTime endTIme);

    /**
     * 获取内容指定快照版本的系列日志
     *
     * @param contentId 内容ID
     * @param snapVersion    版本号
     * @return 日志记录
     */
    List<EditionLogRespVO> getLogByVersion(String contentId, Integer snapVersion);

    /**
     * 获取编辑历史摘要（分页）
     *
     * @param contentId 内容ID
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 编辑历史
     */
    IPage<EditionLogRespVO> getHistory(String contentId, Integer pageNum, Integer pageSize);




}
