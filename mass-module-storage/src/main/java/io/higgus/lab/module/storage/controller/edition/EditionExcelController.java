package io.higgus.lab.module.storage.controller.edition;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveRespVO;
import io.higgus.lab.module.storage.service.edition.CollaborationEditFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * Excel 在线编辑接口
 */
@Slf4j
@RestController
@RequestMapping("/collaboration/content/edition/excel")
@Tag(name = "Excel 在线编辑")
public class EditionExcelController {

    @Resource
    private CollaborationEditFacade facadeService;

    @Operation(summary = "保存 Excel 单元格变更")
    @PostMapping("/save")
    public CommonResult<EditionExcelSaveRespVO> saveExcel(
            @Parameter(description = "变更请求")
            @Valid @RequestBody EditionExcelSaveReqVO reqVO) {
        log.info("收到 Excel 保存请求, contentId={}, row={}, col={}",
                reqVO.getContentId(), reqVO.getRow(), reqVO.getCol());
        facadeService.saveEdition(reqVO);
        return CommonResult.success(null);
    }
}
