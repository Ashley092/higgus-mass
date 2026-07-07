package io.higgus.lab.module.storage.service.edition.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditionExcelSaveLogDto {

    /**
     * 幂等性 ID（用于 Redis 缓存检查，不写入数据库）
     */
    private String idempotentKey;

    /**
     * 内容ID（关联content_metadata表）
     */

    private String contentId;

    /**
     * 版本号（递增）
     */
    private Integer version;

    /**
     * 快照版本（递增）
     */
    private Integer contentSnapshotVersion;

    /**
     * 行号（数组索引，从0开始）
     */
    private Integer rowIndex;

    /**
     * 列号（数组索引，从0开始）
     */
    private Integer colIndex;

    /**
     * 旧值（修改前的值）
     */
    private String oldValue;

    /**
     * 新值（修改后的值）
     */
    private String newValue;

    /**
     * 更新人ID
     */
    private Long updater;

    private LocalDateTime createTime;
}
