package io.higgus.lab.module.storage.dal.dataobject;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName( value = "")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDetailDO extends BaseDO {

    @TableId("id")
    private Long id;

    private String fileMd5;

    private Long parentId;

    private Long userId;


}
