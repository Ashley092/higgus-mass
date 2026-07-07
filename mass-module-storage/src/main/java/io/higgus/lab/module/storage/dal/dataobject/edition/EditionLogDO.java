package io.higgus.lab.module.storage.dal.dataobject.edition;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 协作编辑操作日志（事件溯源）
 */
@Data
@TableName("edition_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditionLogDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内容ID（关联content_metadata表）
     */
    private Long contentId;

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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
