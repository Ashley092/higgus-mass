package io.higgus.lab.module.bulong.controller.admin.production;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.service.production.ProductionGbDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 梳栉工艺明细管理")
@RestController
@RequestMapping("/admin/bulong/gb-detail")
@Validated
public class ProductionGbDetailController {

    @Resource
    private ProductionGbDetailService gbDetailService;

    @Operation(summary = "创建梳栉工艺明细")
    @PostMapping("/create")
    public CommonResult<Long> createGbDetail(@RequestBody @Valid GbDetailCreateReqVO createReqVO) {
        Long detailId = gbDetailService.createGbDetail(createReqVO);
        return success(detailId);
    }

    @Operation(summary = "更新梳栉工艺明细")
    @PutMapping("/update")
    public CommonResult<Boolean> updateGbDetail(@RequestBody @Valid GbDetailUpdateReqVO updateReqVO) {
        gbDetailService.updateGbDetail(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除梳栉工艺明细")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteGbDetail(@Parameter(name = "detailId", description = "明细ID", required = true) @RequestParam Long detailId) {
        gbDetailService.deleteGbDetail(detailId);
        return success(true);
    }

    @Operation(summary = "获取梳栉工艺明细")
    @GetMapping("/get")
    public CommonResult<GbDetailRespVO> getGbDetail(@Parameter(name = "detailId", description = "明细ID", required = true) @RequestParam Long detailId) {
        GbDetailRespVO detail = gbDetailService.getGbDetail(detailId);
        return success(detail);
    }

    @Operation(summary = "根据生产记录序号获取明细列表")
    @GetMapping("/list-by-record-id")
    public CommonResult<List<GbDetailRespVO>> getGbDetailListByRecordId(
            @Parameter(name = "recordId", description = "生产记录序号", required = true) @RequestParam String recordId) {
        List<GbDetailRespVO> list = gbDetailService.getGbDetailListByRecordId(recordId);
        return success(list);
    }
}
