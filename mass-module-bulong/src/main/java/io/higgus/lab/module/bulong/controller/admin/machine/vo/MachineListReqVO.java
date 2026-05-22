package io.higgus.lab.module.bulong.controller.admin.machine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理后台 - 机台查询 reqVO
 */
@Schema(description = "管理后台 - 机台查询 reqVO")
@Data
public class MachineListReqVO {

    @Schema(description = "机台编码", example = "RSE4-01")
    private String machineCode;

    @Schema(description = "机台名称", example = "高速经编机1号")
    private String machineName;

    @Schema(description = "机台类型", example = "高速特里科")
    private String machineType;

    @Schema(description = "所属车间", example = "一车间")
    private String workshop;

    @Schema(description = "状态", example = "IDLE")
    private String status;
}
