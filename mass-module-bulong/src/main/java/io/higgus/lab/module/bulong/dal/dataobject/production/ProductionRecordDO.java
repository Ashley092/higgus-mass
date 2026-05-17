package io.higgus.lab.module.bulong.dal.dataobject.production;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 生产记录主表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_production_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionRecordDO extends BaseDO {

    /**
     * 生产记录序号 (PR001/PR20250902222741)
     * 使用业务编号作为主键
     */
    @TableId
    private String recordId;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 实际门幅
     */
    private BigDecimal actualWidth;

    /**
     * 实际密度
     */
    private BigDecimal actualDensity;

    /**
     * 实际间隔
     */
    private BigDecimal actualSpacing;

    /**
     * 克重 (g/㎡)
     */
    private BigDecimal weightGsm;

    /**
     * 生产机台号
     */
    private String machineCode;

    /**
     * 是否有效 (1: 有效, 0: 无效)
     */
    private Boolean isActive;

    /**
     * 备注
     */
    private String remark;
}
