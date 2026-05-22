package io.higgus.lab.module.bulong.dal.dataobject.machine;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 机台表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_machine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 机台编码（如 RSE4-01）
     */
    private String machineCode;

    /**
     * 机台名称
     */
    private String machineName;

    /**
     * 机台类型：高速特里科、拉舍尔等
     */
    private String machineType;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 额定转速（转/分）
     */
    private Integer speedRated;

    /**
     * 最高转速
     */
    private Integer speedMax;

    /**
     * 工作幅宽（cm）
     */
    private BigDecimal widthWorking;

    /**
     * 针距范围
     */
    private String needleGaugeRange;

    /**
     * 所属车间
     */
    private String workshop;

    /**
     * 所属区域
     */
    private String area;

    /**
     * 具体位置
     */
    private String location;

    /**
     * 状态：IDLE-闲置 RUNNING-运行 STOPPED-停机 MAINTENANCE-保养
     */
    private String status;

    /**
     * 当前生产品ID
     */
    private Long currentProductId;

    /**
     * 上次保养日期
     */
    private LocalDate maintenanceDate;

    /**
     * 下次保养日期
     */
    private LocalDate nextMaintenanceDate;

    /**
     * 保养周期（天）
     */
    private Integer maintenanceInterval;

    /**
     * 累计运行时间（小时）
     */
    private BigDecimal totalRunningHours;

    /**
     * 累计产量
     */
    private BigDecimal totalOutput;

    /**
     * 备注
     */
    private String remark;
}
