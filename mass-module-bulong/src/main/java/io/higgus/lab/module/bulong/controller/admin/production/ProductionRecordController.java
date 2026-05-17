package io.higgus.lab.module.bulong.controller.admin.production;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.service.production.ProductionRecordService;
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

@Tag(name = "布隆后台 - 生产记录管理")
@RestController
@RequestMapping("/admin/bulong/production")
@Validated
public class ProductionRecordController {

    @Resource
    private ProductionRecordService productionRecordService;

    @Resource
    private ProductionGbDetailService productionGbDetailService;

    @Operation(summary = "创建生产记录")
    @PostMapping("/create")
    public CommonResult<String> createRecord(@RequestBody @Valid ProductionRecordCreateReqVO createReqVO) {
        String recordId = productionRecordService.createRecord(createReqVO);
        return success(recordId);
    }

    @Operation(summary = "更新生产记录")
    @PutMapping("/update")
    public CommonResult<Boolean> updateRecord(@RequestBody @Valid ProductionRecordUpdateReqVO updateReqVO) {
        productionRecordService.updateRecord(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除生产记录")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteRecord(@Parameter(name = "recordId", description = "生产记录序号", required = true) @RequestParam String recordId) {
        productionRecordService.deleteRecord(recordId);
        return success(true);
    }

    @Operation(summary = "获取生产记录")
    @GetMapping("/get")
    public CommonResult<ProductionRecordRespVO> getRecord(@Parameter(name = "recordId", description = "生产记录序号", required = true) @RequestParam String recordId) {
        ProductionRecordRespVO record = productionRecordService.getRecord(recordId);
        // 填充梳栉明细
        if (record != null) {
            List<GbDetailRespVO> gbDetails = productionGbDetailService.getGbDetailListByRecordId(recordId);
            record.setGbDetails(gbDetails);
        }
        return success(record);
    }

    @Operation(summary = "获取生产记录列表")
    @GetMapping("/list")
    public CommonResult<List<ProductionRecordRespVO>> getRecordList(ProductionRecordListReqVO listReqVO) {
        List<ProductionRecordRespVO> list = productionRecordService.getRecordList(listReqVO);
        return success(list);
    }

    @Operation(summary = "根据产品编号获取生产记录列表")
    @GetMapping("/list-by-product-code")
    public CommonResult<List<ProductionRecordRespVO>> getRecordListByProductCode(
            @Parameter(name = "productCode", description = "产品编号", required = true) @RequestParam String productCode) {
        List<ProductionRecordRespVO> list = productionRecordService.getRecordListByProductCode(productCode);
        return success(list);
    }

    @Operation(summary = "获取生产记录分页列表")
    @GetMapping("/page")
    public CommonResult<PageResult<ProductionRecordRespVO>> getRecordPage(ProductionRecordPageReqVO pageReqVO) {
        PageResult<ProductionRecordRespVO> result = productionRecordService.getRecordPage(pageReqVO);
        return success(result);
    }
}
