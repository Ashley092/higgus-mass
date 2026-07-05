package io.higgus.lab.module.storage.dal.dataobject.collab;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("collaboration_space")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationSpaceDO extends BaseDO {

    @TableId("id")
    private Long id;

    private String name;

    private String description;

    private Integer status;

    private String creator;

    private String updater;

    @TableField("deleted")
    private Boolean deleted;
}
