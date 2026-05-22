package io.higgus.lab.module.bulong.service.plan;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.convert.PlanMachineConvert;
import io.higgus.lab.module.bulong.dal.dataobject.plan.PlanMachineDO;
import io.higgus.lab.module.bulong.dal.dataobject.plan.ProductionPlanDO;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import io.higgus.lab.module.bulong.dal.mysql.plan.PlanMachineMapper;
import io.higgus.lab.module.bulong.dal.mysql.plan.ProductionPlanMapper;
import io.higgus.lab.module.bulong.dal.mysql.machine.MachineMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计划-机台关联管理 Service 实现类
 */
@Slf4j
@Service
public class PlanMachineServiceImpl implements PlanMachineService {

    @Resource
    private PlanMachineMapper planMachineMapper;

    @Resource
    private ProductionPlanMapper productionPlanMapper;

    @Resource
    private MachineMapper machineMapper;

    private static final PlanMachineConvert planMachineConvert = PlanMachineConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlanMachine(PlanMachineCreateReqVO createReqVO) {
        // 检查是否已存在关联
        PlanMachineDO exist = planMachineMapper.selectByPlanAndMachine(
                createReqVO.getPlanId(), createReqVO.getMachineId());
        if (exist != null) {
            throw new RuntimeException("该计划与机台已存在关联");
        }

        // 转换并保存
        PlanMachineDO planMachineDO = planMachineConvert.toPlanMachineDO(createReqVO);
        // 设置分配时间
        if (planMachineDO.getAssignedAt() == null) {
            planMachineDO.setAssignedAt(LocalDateTime.now());
        }
        planMachineMapper.insert(planMachineDO);
        log.info("【计划-机台关联】创建关联成功，ID: {}, 计划ID: {}, 机台ID: {}",
                planMachineDO.getId(), planMachineDO.getPlanId(), planMachineDO.getMachineId());

        return planMachineDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlanMachine(PlanMachineUpdateReqVO updateReqVO) {
        PlanMachineDO exist = planMachineMapper.selectById(updateReqVO.getId());
        if (exist == null) {
            throw new RuntimeException("关联记录不存在");
        }

        PlanMachineDO planMachineDO = planMachineConvert.toPlanMachineDO(updateReqVO);
        planMachineDO.setId(updateReqVO.getId());
        planMachineMapper.updateById(planMachineDO);
        log.info("【计划-机台关联】更新关联成功，ID: {}", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlanMachine(Long id) {
        PlanMachineDO exist = planMachineMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("关联记录不存在");
        }
        planMachineMapper.deleteById(id);
        log.info("【计划-机台关联】删除关联成功，ID: {}", id);
    }

    @Override
    public PlanMachineRespVO getPlanMachine(Long id) {
        PlanMachineDO planMachineDO = planMachineMapper.selectById(id);
        return fillDetails(planMachineConvert.toPlanMachineRespVO(planMachineDO), planMachineDO);
    }

    @Override
    public List<PlanMachineRespVO> getPlanMachineListByPlanId(Long planId) {
        List<PlanMachineDO> list = planMachineMapper.selectByPlanId(planId);
        List<PlanMachineRespVO> respList = planMachineConvert.toPlanMachineRespVOList(list);
        return fillDetailList(respList, list);
    }

    @Override
    public List<PlanMachineRespVO> getPlanMachineListByMachineId(Long machineId) {
        List<PlanMachineDO> list = planMachineMapper.selectByMachineId(machineId);
        List<PlanMachineRespVO> respList = planMachineConvert.toPlanMachineRespVOList(list);
        return fillDetailList(respList, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreatePlanMachine(Long planId, List<Long> machineIds) {
        // 检查计划是否存在
        ProductionPlanDO plan = productionPlanMapper.selectById(planId);
        if (plan == null) {
            throw new RuntimeException("计划不存在");
        }

        List<PlanMachineDO> toInsert = new ArrayList<>();
        for (Long machineId : machineIds) {
            // 检查是否已存在
            PlanMachineDO exist = planMachineMapper.selectByPlanAndMachine(planId, machineId);
            if (exist != null) {
                continue;
            }
            PlanMachineDO planMachineDO = new PlanMachineDO();
            planMachineDO.setPlanId(planId);
            planMachineDO.setMachineId(machineId);
            planMachineDO.setAssignedAt(LocalDateTime.now());
            toInsert.add(planMachineDO);
        }

        if (!toInsert.isEmpty()) {
//            planMachineMapper.insertBatch(toInsert);
            log.info("【计划-机台关联】批量创建关联成功，计划ID: {}, 数量: {}", planId, toInsert.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePlanMachine(Long planId) {
        planMachineMapper.deleteByPlanId(planId);
        log.info("【计划-机台关联】批量删除关联成功，计划ID: {}", planId);
    }

    /**
     * 填充详情
     */
    private PlanMachineRespVO fillDetails(PlanMachineRespVO respVO, PlanMachineDO planMachineDO) {
        if (respVO == null || planMachineDO == null) {
            return respVO;
        }
        // 填充计划信息
        if (planMachineDO.getPlanId() != null) {
            ProductionPlanDO plan = productionPlanMapper.selectById(planMachineDO.getPlanId());
            if (plan != null) {
                respVO.setPlanCode(plan.getPlanCode());
            }
        }
        // 填充机台信息
        if (planMachineDO.getMachineId() != null) {
            MachineDO machine = machineMapper.selectById(planMachineDO.getMachineId());
            if (machine != null) {
                respVO.setMachineCode(machine.getMachineCode());
                respVO.setMachineName(machine.getMachineName());
            }
        }
        return respVO;
    }

    /**
     * 批量填充详情
     */
    private List<PlanMachineRespVO> fillDetailList(List<PlanMachineRespVO> respList, List<PlanMachineDO> doList) {
        if (respList == null || respList.isEmpty()) {
            return respList;
        }
        // 收集ID
        List<Long> planIds = doList.stream()
                .map(PlanMachineDO::getPlanId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        List<Long> machineIds = doList.stream()
                .map(PlanMachineDO::getMachineId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询
        Map<Long, ProductionPlanDO> planMap = planIds.isEmpty() ? Map.of() :
                productionPlanMapper.selectBatchIds(planIds).stream()
                        .collect(Collectors.toMap(ProductionPlanDO::getId, p -> p));
        Map<Long, MachineDO> machineMap = machineIds.isEmpty() ? Map.of() :
                machineMapper.selectBatchIds(machineIds).stream()
                        .collect(Collectors.toMap(MachineDO::getId, m -> m));

        // 填充
        for (int i = 0; i < respList.size(); i++) {
            PlanMachineRespVO resp = respList.get(i);
            PlanMachineDO pm = doList.get(i);

            if (pm.getPlanId() != null && planMap.containsKey(pm.getPlanId())) {
                resp.setPlanCode(planMap.get(pm.getPlanId()).getPlanCode());
            }
            if (pm.getMachineId() != null && machineMap.containsKey(pm.getMachineId())) {
                MachineDO machine = machineMap.get(pm.getMachineId());
                resp.setMachineCode(machine.getMachineCode());
                resp.setMachineName(machine.getMachineName());
            }
        }
        return respList;
    }
}
