package io.higgus.lab.module.storage.service;

import io.higgus.lab.module.storage.dal.dataobject.ContentMetadataDO;
import io.higgus.lab.module.storage.vo.ContentMetadataCreateReqVO;
import io.higgus.lab.module.storage.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.vo.ContentMetadataUpdateReqVO;

import java.util.List;

/**
 * 内容元数据服务 - 仅负责元数据的 CRUD 操作
 * 不涉及文件存储逻辑
 */
public interface ContentMetadataService {

    /**
     * 创建内容元数据
     */
    Long create(ContentMetadataCreateReqVO createReqVO, Long creator);

    /**
     * 更新内容元数据
     */
    void update(ContentMetadataUpdateReqVO updateReqVO, Long updater);

    /**
     * 删除内容元数据（仅删除记录，不删除文件）
     */
    void delete(Long id);

    /**
     * 根据 ID 获取内容元数据
     */
    ContentMetadataRespVO get(Long id);

    /**
     * 根据 ID 获取内容元数据（返回 DO）
     */
    ContentMetadataDO getDO(Long id);

    /**
     * 根据项目获取内容列表
     */
    List<ContentMetadataRespVO> getListByItemId(Long itemId);

    /**
     * 根据项目和类型获取内容列表
     */
    List<ContentMetadataRespVO> getListByItemIdAndType(Long itemId, Integer contentType);

    /**
     * 根据 MD5 查询内容（用于秒传检查）
     */
    ContentMetadataDO findByMd5(String fileMd5);

    /**
     * 获取所有内容列表
     */
    List<ContentMetadataRespVO> getList();
}
