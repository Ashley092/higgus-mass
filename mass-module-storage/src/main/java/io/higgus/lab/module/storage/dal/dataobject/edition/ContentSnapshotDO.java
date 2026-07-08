package io.higgus.lab.module.storage.dal.dataobject.edition;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("content_snapshot")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentSnapshotDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 关联的内容ID */
    private Long contentId;

    /** 快照版本号 */
    private Integer snapshotVersion;

    /** MinIO中的Object Key */
    private String storageKey;

    /** 文件大小 */
    private Long fileSize;

    /** 文件MD5（上传完成后计算） */
    private String fileMd5;

    /** 该快照包含的编辑日志数 */
    private Integer editionLogCount;

    /** 是否为当前版本: 0否, 1是 */
    private Integer isCurrent;

    /** 创建者ID */
    private Long creator;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 是否删除 */
    private Boolean deleted;
}
