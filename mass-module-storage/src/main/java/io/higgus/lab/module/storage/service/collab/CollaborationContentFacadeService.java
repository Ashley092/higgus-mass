package io.higgus.lab.module.storage.service.collab;

import io.higgus.lab.module.storage.controller.collab.vo.ContentUploadReqVO;
import io.higgus.lab.module.storage.controller.collab.vo.UploadResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 内容门面服务 - 编排元数据和文件存储
 */
public interface CollaborationContentFacadeService {

    /**
     * 上传文件（后端计算MD5，支持秒传）
     *
     * @param file 上传的文件
     * @param creator 创建者ID
     * @return 上传结果
     */
    UploadResultVO uploadFile(MultipartFile file, Long creator) throws IOException;

    /**
     * 上传文件（带业务参数）
     *
     * @param file 上传的文件
     * @param reqVO 业务参数
     * @param creator 创建者ID
     * @return 上传结果
     */
    UploadResultVO uploadFile(MultipartFile file, ContentUploadReqVO reqVO, Long creator) throws IOException;

    /**
     * 删除内容（同时删除文件和元数据）
     *
     * @param contentId 内容ID
     */
    void deleteContent(Long contentId);
}
