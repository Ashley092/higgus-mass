package io.higgus.lab.module.storage.service.editionOld;

import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveRespVO;

public interface EditionExcelService {

    /**
     * 保存 Excel 单个单元格变更
     *
     * @param reqVO 变更请求（行号、列号从 0 开始）
     * @return 保存结果
     */
    EditionExcelSaveRespVO saveExcel(EditionExcelSaveReqVO reqVO);
}
