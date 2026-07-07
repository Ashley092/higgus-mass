package io.higgus.lab.module.storage.service.edition;

import io.higgus.lab.module.storage.dal.dataobject.edition.EditionLogDO;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;

import java.util.List;

public interface EditionPoiService {

    /**
     * 重放该版本下的所有日志
     */
    List<EditionLogDO> rewireLog(EditionExcelSaveLogDto dto);

    /**
     *  尝试更新
     */
    void tryGenerateNewSnapshotFile(EditionExcelSaveLogDto dto);
}
