package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.convert.MachineProductionConvert;
import io.higgus.lab.module.bulong.dal.dataobject.production.MachineProductionDO;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import io.higgus.lab.module.bulong.dal.mysql.production.MachineProductionMapper;
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
 * 机台采集数据 Service 实现类
 */
@Slf4j
@Service
public class MachineProductionServiceImpl implements MachineProductionService {

    @Resource
    private MachineProductionMapper machineProductionMapper;

    @Resource
    private MachineMapper machineMapper;

    private static final MachineProductionConvert convert = MachineProductionConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMachineProduction(MachineProductionCreateReqVO createReqVO) {
        MachineProductionDO machineProductionDO = convert.toMachineProductionDO(createReqVO);
        machineProductionMapper.insert(machineProductionDO);
        log.info("【机台采集】创建采集数据成功，ID: {}, 机台ID: {}", machineProductionDO.getId(), machineProductionDO.getMachineId());
        return machineProductionDO.getId();
    }

    @Override
    public List<MachineProductionRespVO> getMachineProductionListByMachineAndTimeRange(
            Long machineId, LocalDateTime startTime, LocalDateTime endTime) {
        List<MachineProductionDO> list = machineProductionMapper.selectByMachineAndTimeRange(machineId, startTime, endTime);
        List<MachineProductionRespVO> respList = convert.toMachineProductionRespVOList(list);
        return fillMachineCode(respList, list);
    }

    @Override
    public List<MachineProductionRespVO> getMachineProductionListByPlanId(Long planId) {
        List<MachineProductionDO> list = machineProductionMapper.selectByPlanId(planId);
        List<MachineProductionRespVO> respList = convert.toMachineProductionRespVOList(list);
        return fillMachineCode(respList, list);
    }

    @Override
    public MachineProductionRespVO getLatestMachineProduction(Long machineId) {
        MachineProductionDO machineProductionDO = machineProductionMapper.selectLatestByMachineId(machineId);
        MachineProductionRespVO respVO = convert.toMachineProductionRespVO(machineProductionDO);
        if (respVO != null && machineProductionDO != null) {
            fillMachineCode(respVO, machineProductionDO);
        }
        return respVO;
    }

    @Override
    public MachineProductionRespVO getMachineProductionByTimePoint(Long machineId, LocalDateTime timePoint) {
        MachineProductionDO machineProductionDO = machineProductionMapper.selectByMachineAndTimePoint(machineId, timePoint);
        MachineProductionRespVO respVO = convert.toMachineProductionRespVO(machineProductionDO);
        if (respVO != null && machineProductionDO != null) {
            fillMachineCode(respVO, machineProductionDO);
        }
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateMachineProduction(List<MachineProductionCreateReqVO> createReqVOList) {
        if (createReqVOList == null || createReqVOList.isEmpty()) {
            return;
        }
        List<MachineProductionDO> toInsert = new ArrayList<>();
        for (MachineProductionCreateReqVO createReqVO : createReqVOList) {
            toInsert.add(convert.toMachineProductionDO(createReqVO));
        }
//        machineProductionMapper.insertBatch(toInsert);
        log.info("【机台采集】批量创建采集数据成功，数量: {}", toInsert.size());
    }

    /**
     * 填充机台编码
     */
    private MachineProductionRespVO fillMachineCode(MachineProductionRespVO respVO, MachineProductionDO machineProductionDO) {
        if (respVO == null || machineProductionDO == null) {
            return respVO;
        }
        if (machineProductionDO.getMachineId() != null) {
            MachineDO machine = machineMapper.selectById(machineProductionDO.getMachineId());
            if (machine != null) {
                respVO.setMachineCode(machine.getMachineCode());
            }
        }
        return respVO;
    }

    /**
     * 批量填充机台编码
     */
    private List<MachineProductionRespVO> fillMachineCode(List<MachineProductionRespVO> respList, List<MachineProductionDO> doList) {
        if (respList == null || respList.isEmpty()) {
            return respList;
        }
        List<Long> machineIds = doList.stream()
                .map(MachineProductionDO::getMachineId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, MachineDO> machineMap = machineIds.isEmpty() ? Map.of() :
                machineMapper.selectBatchIds(machineIds).stream()
                        .collect(Collectors.toMap(MachineDO::getId, m -> m));

        for (int i = 0; i < respList.size(); i++) {
            MachineProductionDO mp = doList.get(i);
            if (mp.getMachineId() != null && machineMap.containsKey(mp.getMachineId())) {
                respList.get(i).setMachineCode(machineMap.get(mp.getMachineId()).getMachineCode());
            }
        }
        return respList;
    }
}
