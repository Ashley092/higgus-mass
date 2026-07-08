package io.higgus.lab.module.storage.service.edition;

import io.higgus.lab.module.storage.dal.dataobject.edition.ContentSnapshotDO;

public interface ContentSnapshotService {

    /**
     * 创建新快照（临时状态，md5 尚未计算）
     * 流程：
     * 1. 查询当前快照版本号 +1
     * 2. 生成临时 storageKey（带 temp_ 前缀）
     * 3. 写入 content_snapshot 表（is_current = 0）
     * 4. 更新 content_metadata 的 current_snapshot_version
     *
     * @param contentId 内容ID
     * @param creator   创建者ID
     * @return 新创建的快照记录
     */
    ContentSnapshotDO createSnapshotTemp(Long contentId, Long creator);

    /**
     * 快照文件上传完成后，更新快照状态
     * 流程：
     * 1. 计算文件 md5
     * 2. 更新快照记录（md5、file_size）
     * 3. 将该快照设为当前版本（is_current = 1）
     * 4. 更新 content_metadata 的 current_storage_key
     *
     * @param snapshotId 快照ID
     * @param fileBytes  文件字节数组
     */
    void finalizeSnapshot(Long snapshotId, byte[] fileBytes);

    /**
     * 获取指定内容的当前快照
     */
    ContentSnapshotDO getCurrentSnapshot(Long contentId);
}
