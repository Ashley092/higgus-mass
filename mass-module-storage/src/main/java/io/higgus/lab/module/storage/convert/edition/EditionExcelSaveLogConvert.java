package io.higgus.lab.module.storage.convert.edition;


import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.controller.edition.vo.EditionLogRespVO;
import io.higgus.lab.module.storage.dal.dataobject.edition.EditionLogDO;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EditionExcelSaveLogConvert {

    EditionExcelSaveLogConvert INSTANCE = Mappers.getMapper(EditionExcelSaveLogConvert.class);

    EditionExcelSaveLogDto toEditionLogDto(EditionExcelSaveReqVO reqVO);

    EditionLogRespVO toEditionLogRespVO(EditionExcelSaveLogDto dto);

    EditionLogRespVO toEditionLogRespVO(EditionLogDO logDO);

    EditionLogDO toEditionLogDO(EditionExcelSaveLogDto dto);

    List<EditionLogRespVO> toListEditionLogRespVO(List<EditionLogDO> logDoList);

}
