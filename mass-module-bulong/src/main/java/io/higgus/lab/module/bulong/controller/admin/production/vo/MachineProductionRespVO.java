package io.higgus.lab.module.bulong.controller.admin.production.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 机台采集数据响应 VO
 */
@Schema(description = "管理后台 - 机台采集数据响应 VO")
@Data
public class MachineProductionRespVO {

    @Schema(description = "采集ID", example = "1")
    private Long id;

    @Schema(description = "关联计划ID", example = "1")
    private Long planId;

    @Schema(description = "关联机台ID", example = "1")
    private Long machineId;

    @Schema(description = "机台编码（扩展字段）")
    private String machineCode;

    @Schema(description = "采集时间", example = "2026-05-20 08:00:00")
    private LocalDateTime collectTime;

    @Schema(description = "班次号", example = "A")
    private String shiftNo;

    @Schema(description = "当前车速（转/分）", example = "2000")
    private BigDecimal speedCurrent;

    @Schema(description = "目标车速", example = "2000")
    private BigDecimal speedTarget;

    @Schema(description = "产量计数器", example = "1234567.89")
    private BigDecimal outputCounter;

    @Schema(description = "产量（米）", example = "5000.00")
    private BigDecimal outputMeter;

    @Schema(description = "张力数据 JSON", example = "{\"gb1\":10.5,\"gb2\":11.2}")
    private String tensionValues;

    @Schema(description = "温度（℃）", example = "25.0")
    private BigDecimal temperature;

    @Schema(description = "湿度（%）", example = "60.0")
    private BigDecimal humidity;

    @Schema(description = "运行状态", example = "RUNNING")
    private String runningStatus;

    @Schema(description = "告警代码", example = "")
    private String alarmCode;

    @Schema(description = "告警信息", example = "")
    private String alarmMessage;

    @Schema(description = "能耗（kWh）", example = "15.5")
    private BigDecimal powerConsumption;

    @Schema(description = "扩展数据 JSON")
    private String extraData;

    @Schema(description = "创建时间", example = "2026-05-20 08:00:00")
    private LocalDateTime createTime;
}
