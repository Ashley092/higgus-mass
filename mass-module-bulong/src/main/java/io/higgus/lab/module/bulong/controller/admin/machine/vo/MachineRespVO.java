package io.higgus.lab.module.bulong.controller.admin.machine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 管理后台 - 机台响应 VO
 */
@Schema(description = "管理后台 - 机台响应 VO")
@Data
public class MachineRespVO {

    @Schema(description = "机台ID", example = "1")
    private Long id;

    @Schema(description = "机台编码", example = "RSE4-01")
    private String machineCode;

    @Schema(description = "机台名称", example = "高速经编机1号")
    private String machineName;

    @Schema(description = "机台类型", example = "高速特里科")
    private String machineType;

    @Schema(description = "品牌", example = "卡尔迈耶")
    private String brand;

    @Schema(description = "型号", example = "RSE4-150-28")
    private String model;

    @Schema(description = "制造商", example = "卡尔迈耶(中国)有限公司")
    private String manufacturer;

    @Schema(description = "额定转速（转/分）", example = "2000")
    private Integer speedRated;

    @Schema(description = "最高转速", example = "2500")
    private Integer speedMax;

    @Schema(description = "工作幅宽（cm）", example = "150.00")
    private BigDecimal widthWorking;

    @Schema(description = "针距范围", example = "E18-E32")
    private String needleGaugeRange;

    @Schema(description = "所属车间", example = "一车间")
    private String workshop;

    @Schema(description = "所属区域", example = "A区")
    private String area;

    @Schema(description = "具体位置", example = "A区-01号工位")
    private String location;

    @Schema(description = "状态", example = "IDLE")
    private String status;

    @Schema(description = "当前生产品ID", example = "1")
    private Long currentProductId;

    @Schema(description = "上次保养日期", example = "2026-01-01")
    private LocalDate maintenanceDate;

    @Schema(description = "下次保养日期", example = "2026-02-01")
    private LocalDate nextMaintenanceDate;

    @Schema(description = "保养周期（天）", example = "30")
    private Integer maintenanceInterval;

    @Schema(description = "累计运行时间（小时）", example = "1000.5")
    private BigDecimal totalRunningHours;

    @Schema(description = "累计产量", example = "50000.00")
    private BigDecimal totalOutput;

    @Schema(description = "备注", example = "2024年采购")
    private String remark;

    @Schema(description = "创建时间", example = "2026-01-01 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-01-01 10:00:00")
    private LocalDateTime updateTime;
}
