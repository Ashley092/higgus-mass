package io.higgus.lab.framework.mybatis.core.dataobject;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(value = "transMap")
public abstract class BaseDO implements Serializable, TransPojo {

}
