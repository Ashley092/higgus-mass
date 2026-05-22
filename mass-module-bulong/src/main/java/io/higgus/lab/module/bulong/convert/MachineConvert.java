package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.machine.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 机台管理对象转换器
 */
@Mapper
public interface MachineConvert {

    MachineConvert INSTANCE = Mappers.getMapper(MachineConvert.class);

    /**
     * CreateReqVO -> MachineDO
     */
    MachineDO toMachineDO(MachineCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> MachineDO
     */
    MachineDO toMachineDO(MachineUpdateReqVO updateReqVO);

    /**
     * MachineDO -> MachineRespVO
     */
    MachineRespVO toMachineRespVO(MachineDO machineDO);

    /**
     * 批量转换
     */
    List<MachineRespVO> toMachineRespVOList(List<MachineDO> machineDOList);
}
