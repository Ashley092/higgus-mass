package io.higgus.lab.module.storage.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("content_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentMetadataDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long itemId;

    private String title;

    private Integer contentType;

    private String storageKey;

    private Long fileSize;

    private String fileExtension;

    private String fileMd5;

    private Integer version;

    private Long creator;

    private Long updater;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField("deleted")
    private Boolean deleted;
}
