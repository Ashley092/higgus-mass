package io.higgus.lab.module.storage.service.common;

public interface CollabRedisKey {


    String FILE_METADATA_PREFIX = "file:metadata:";

    String CELL_EDIT_LOG = "file:edit:excel:";
    String FILE_SNAPSHOT = "file:snapshot:excel:";


    /** 短时间缓存：5分钟 */
    long TTL_SHORT = 5 * 60L;

    /** 中等缓存：30分钟 */
    long TTL_MEDIUM = 30 * 60L;


    static String getFileMetadataById(String fileId) {

        return FILE_METADATA_PREFIX + fileId;
    }
}
