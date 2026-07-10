package io.higgus.lab.module.storage.controller.edition;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.controller.edition.vo.EditionHistoryRespVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionLogRespVO;
import io.higgus.lab.module.storage.service.edition.EditionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Excel 协作编辑日志查询接口
 */
@Slf4j
@RestController
@RequestMapping("/collaboration/content/edition/excel")
@Tag(name = "Excel 编辑日志")
public class EditionLogController {

    @Resource
    private EditionLogService editionLogService;

    @Operation(summary = "获取内容的最新版本号")
    @GetMapping("/logs/latest-version")
    public CommonResult<Integer> getLatestVersion(
            @Parameter(description = "内容ID", required = true)
            @RequestParam("contentId") String contentId) {
        return CommonResult.success(editionLogService.getLatestVersion(contentId));
    }

    @Operation(summary = "获取内容的所有编辑日志（按时间倒序）")
    @GetMapping("/logs")
    public CommonResult<List<EditionLogRespVO>> getLogs(
            @Parameter(description = "内容ID", required = true)
            @RequestParam("contentId") String contentId) {
        return CommonResult.success(editionLogService.getLogsByContentId(contentId));
    }

    @Operation(summary = "分页获取编辑历史")
    @GetMapping("/logs/page")
    public CommonResult<IPage<EditionLogRespVO>> getHistoryPage(
            @Parameter(description = "内容ID", required = true)
            @RequestParam("contentId") String contentId,
            @Parameter(description = "页码", example = "1")
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "20")
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return CommonResult.success(editionLogService.getHistory(contentId, pageNum, pageSize));
    }

    @Operation(summary = "获取编辑历史摘要（用于历史面板展示）")
    @GetMapping("/logs/summary")
    public CommonResult<EditionHistoryRespVO> getHistorySummary(
            @Parameter(description = "内容ID", required = true)
            @RequestParam("contentId") String contentId) {
        Long id;
        try {
            id = Long.valueOf(contentId);
        } catch (NumberFormatException e) {
            id = null;
        }
        Integer latest = editionLogService.getLatestVersion(contentId);
        List<EditionLogRespVO> logs = editionLogService.getLogsByContentId(contentId);
        EditionHistoryRespVO resp = EditionHistoryRespVO.builder()
                .contentId(id)
                .currentVersion(latest)
                .totalCount((long) logs.size())
                .logs(logs)
                .build();
        return CommonResult.success(resp);
    }
}
