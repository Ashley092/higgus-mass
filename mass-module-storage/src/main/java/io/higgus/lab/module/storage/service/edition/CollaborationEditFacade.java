package io.higgus.lab.module.storage.service.edition;

import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;

public interface CollaborationEditFacade {


    void saveEdition(EditionExcelSaveReqVO reqVO);
    /*
    * 处理单元格编辑
    * */
    void handleRealtimeEdition(EditionExcelSaveLogDto dto);

    void handlePersistEdition(EditionExcelSaveLogDto dto);
}
