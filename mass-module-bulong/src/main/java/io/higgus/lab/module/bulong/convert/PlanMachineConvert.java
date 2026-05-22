package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.plan.PlanMachineDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 计划-机台关联对象转换器
 */
@Mapper
public interface PlanMachineConvert {

    PlanMachineConvert INSTANCE = Mappers.getMapper(PlanMachineConvert.class);

    /**
     * CreateReqVO -> PlanMachineDO
     */
    PlanMachineDO toPlanMachineDO(PlanMachineCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> PlanMachineDO
     */
    PlanMachineDO toPlanMachineDO(PlanMachineUpdateReqVO updateReqVO);

    /**
     * PlanMachineDO -> PlanMachineRespVO
     */
    PlanMachineRespVO toPlanMachineRespVO(PlanMachineDO planMachineDO);

    /**
     * 批量转换
     */
    List<PlanMachineRespVO> toPlanMachineRespVOList(List<PlanMachineDO> planMachineDOList);
}
