package io.higgus.lab.module.storage.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("collaboration_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationProjectDO extends BaseDO {

    @TableId("id")
    private Long id;

    private Long spaceId;

    private String name;

    private Integer type;

    private String creator;

    private String updater;

    @TableField("deleted")
    private Boolean deleted;
}
