package io.higgus.lab.module.bulong.controller.admin.machine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 管理后台 - 机台创建 reqVO
 */
@Schema(description = "管理后台 - 机台创建 reqVO")
@Data
public class MachineCreateReqVO {

    @Schema(description = "机台编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "RSE4-01")
    @NotBlank(message = "机台编码不能为空")
    @Size(max = 20, message = "机台编码长度不能超过20个字符")
    private String machineCode;

    @Schema(description = "机台名称", example = "高速经编机1号")
    @Size(max = 50, message = "机台名称长度不能超过50个字符")
    private String machineName;

    @Schema(description = "机台类型", example = "高速特里科")
    @Size(max = 50, message = "机台类型长度不能超过50个字符")
    private String machineType;

    @Schema(description = "品牌", example = "卡尔迈耶")
    @Size(max = 50, message = "品牌长度不能超过50个字符")
    private String brand;

    @Schema(description = "型号", example = "RSE4-150-28")
    @Size(max = 50, message = "型号长度不能超过50个字符")
    private String model;

    @Schema(description = "制造商", example = "卡尔迈耶(中国)有限公司")
    @Size(max = 100, message = "制造商长度不能超过100个字符")
    private String manufacturer;

    @Schema(description = "额定转速（转/分）", example = "2000")
    private Integer speedRated;

    @Schema(description = "最高转速", example = "2500")
    private Integer speedMax;

    @Schema(description = "工作幅宽（cm）", example = "150.00")
    private BigDecimal widthWorking;

    @Schema(description = "针距范围", example = "E18-E32")
    @Size(max = 30, message = "针距范围长度不能超过30个字符")
    private String needleGaugeRange;

    @Schema(description = "所属车间", example = "一车间")
    @Size(max = 50, message = "所属车间长度不能超过50个字符")
    private String workshop;

    @Schema(description = "所属区域", example = "A区")
    @Size(max = 50, message = "所属区域长度不能超过50个字符")
    private String area;

    @Schema(description = "具体位置", example = "A区-01号工位")
    @Size(max = 100, message = "具体位置长度不能超过100个字符")
    private String location;

    @Schema(description = "状态", example = "IDLE")
    private String status;

    @Schema(description = "保养周期（天）", example = "30")
    private Integer maintenanceInterval;

    @Schema(description = "备注", example = "2024年采购")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
