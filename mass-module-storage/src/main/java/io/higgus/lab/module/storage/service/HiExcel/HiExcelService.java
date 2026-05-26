package io.higgus.lab.module.storage.service.HiExcel;

import io.higgus.lab.module.storage.controller.HiExcel.vo.HiExcelSaveReqVO;
import io.higgus.lab.module.storage.controller.HiExcel.vo.HiExcelSaveRespVO;

public interface HiExcelService {

    /**
     * 保存 Excel 单个单元格变更
     *
     * @param reqVO 变更请求（行号、列号从 0 开始）
     * @return 保存结果
     */
    HiExcelSaveRespVO saveExcel(HiExcelSaveReqVO reqVO);
}
