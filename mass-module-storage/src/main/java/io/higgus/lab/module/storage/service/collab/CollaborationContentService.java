package io.higgus.lab.module.storage.service.collab;

import io.higgus.lab.module.storage.controller.collab.vo.ContentMetadataCreateReqVO;
import io.higgus.lab.module.storage.controller.collab.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.controller.collab.vo.collab.content.CollaborationContentUpdateReqVO;
import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationContentDO;

import java.util.List;

public interface CollaborationContentService {

    Long create(ContentMetadataCreateReqVO createReqVO, Long creator);

    void update(CollaborationContentUpdateReqVO updateReqVO, Long updater);

    void delete(Long id);

    ContentMetadataRespVO get(Long id);

    CollaborationContentDO getDO(Long id);

    List<ContentMetadataRespVO> getListByItemId(Long itemId);

    List<ContentMetadataRespVO> getListByItemIdAndType(Long itemId, Integer contentType);

    CollaborationContentDO findByMd5(String fileMd5);

    List<ContentMetadataRespVO> getList();


}
