package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.production.MachineProductionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 机台采集数据对象转换器
 */
@Mapper
public interface MachineProductionConvert {

    MachineProductionConvert INSTANCE = Mappers.getMapper(MachineProductionConvert.class);

    /**
     * CreateReqVO -> MachineProductionDO
     */
    MachineProductionDO toMachineProductionDO(MachineProductionCreateReqVO createReqVO);

    /**
     * MachineProductionDO -> MachineProductionRespVO
     */
    MachineProductionRespVO toMachineProductionRespVO(MachineProductionDO machineProductionDO);

    /**
     * 批量转换
     */
    List<MachineProductionRespVO> toMachineProductionRespVOList(List<MachineProductionDO> machineProductionDOList);
}
