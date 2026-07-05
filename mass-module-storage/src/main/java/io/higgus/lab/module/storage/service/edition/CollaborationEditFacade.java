package io.higgus.lab.module.storage.service.edition;

import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;

public interface CollaborationEditFacade {


    void saveEdition(EditionExcelSaveReqVO reqVO);
    /*
    * 处理单元格编辑
    * */
    void handleRealtimeEdition(Object cellEditDTO);

    void handlePersistEdition(Object logDto);
}
