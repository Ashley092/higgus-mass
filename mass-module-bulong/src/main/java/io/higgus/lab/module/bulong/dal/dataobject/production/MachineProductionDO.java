package io.higgus.lab.module.bulong.dal.dataobject.production;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 机台实际生产表 DO（工控采集数据）
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_machine_production")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineProductionDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 关联计划ID
     */
    private Long planId;

    /**
     * 关联机台ID
     */
    private Long machineId;

    /**
     * 采集时间
     */
    private LocalDateTime collectTime;

    /**
     * 班次号
     */
    private String shiftNo;

    /**
     * 当前车速（转/分）
     */
    private BigDecimal speedCurrent;

    /**
     * 目标车速
     */
    private BigDecimal speedTarget;

    /**
     * 产量计数器
     */
    private BigDecimal outputCounter;

    /**
     * 产量（米）
     */
    private BigDecimal outputMeter;

    /**
     * 张力数据 JSON
     */
    private String tensionValues;

    /**
     * 温度（℃）
     */
    private BigDecimal temperature;

    /**
     * 湿度（%）
     */
    private BigDecimal humidity;

    /**
     * 运行状态
     */
    private String runningStatus;

    /**
     * 告警代码
     */
    private String alarmCode;

    /**
     * 告警信息
     */
    private String alarmMessage;

    /**
     * 能耗（kWh）
     */
    private BigDecimal powerConsumption;

    /**
     * 扩展数据 JSON
     */
    private String extraData;
}
