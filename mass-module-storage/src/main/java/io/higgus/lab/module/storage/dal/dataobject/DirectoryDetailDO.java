package io.higgus.lab.module.storage.dal.dataobject;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName( value = "")
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryDetailDO extends BaseDO {


    @TableId
    private Long id;

    /**
     * 指向其父节点
     */
    private Long belongTo;

    private String realPath;

}
