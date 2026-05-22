package io.higgus.lab.module.infra.dal.dataobject;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "file_index")
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class FileIndexDO extends BaseDO {

    @TableId
    private Long id;

    private String fileMd5;

    private String fileName;

    private String fileExt;

    private Long fileSize;

    private String filePath;

}
