package io.higgus.lab.module.bulong.dal.dataobject.process;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 产品工艺主表 DO（按机型分组）
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_product_process")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcessDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 关联产品ID
     */
    private Long productId;

    /**
     * 适用机台类型
     */
    private String machineType;

    /**
     * 工艺编号
     */
    private String processCode;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 推荐车速（转/分）
     */
    private Integer speedSetting;

    /**
     * 温度设置（℃）
     */
    private BigDecimal temperatureSetting;

    /**
     * 湿度设置（%）
     */
    private BigDecimal humiditySetting;

    /**
     * 张力设置（N）
     */
    private BigDecimal tensionSetting;

    /**
     * 辊筒速度（m/min）
     */
    private BigDecimal rollerSpeed;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 备注
     */
    private String remark;
}
