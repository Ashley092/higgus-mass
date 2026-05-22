package io.higgus.lab.module.bulong.service.machine;

import io.higgus.lab.module.bulong.controller.admin.machine.vo.*;
import io.higgus.lab.module.bulong.convert.MachineConvert;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import io.higgus.lab.module.bulong.dal.mysql.machine.MachineMapper;
import io.higgus.lab.module.bulong.enums.BulongErrorCodeConstants;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机台管理 Service 实现类
 */
@Slf4j
@Service
public class MachineServiceImpl implements MachineService {

    @Resource
    private MachineMapper machineMapper;

    private static final MachineConvert machineConvert = MachineConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMachine(MachineCreateReqVO createReqVO) {
        // 检查机台编码是否已存在
        MachineDO existMachine = machineMapper.selectByMachineCode(createReqVO.getMachineCode());
        if (existMachine != null) {
            throw new RuntimeException("机台编码已存在");
        }

        // 转换并保存
        MachineDO machineDO = machineConvert.toMachineDO(createReqVO);
        // 设置默认状态
        if (machineDO.getStatus() == null) {
            machineDO.setStatus("IDLE");
        }
        machineMapper.insert(machineDO);
        log.info("【机台管理】创建机台成功，ID: {}, 编码: {}", machineDO.getId(), machineDO.getMachineCode());

        return machineDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMachine(MachineUpdateReqVO updateReqVO) {
        // 校验机台存在
        MachineDO existMachine = machineMapper.selectById(updateReqVO.getId());
        if (existMachine == null) {
            throw new RuntimeException("机台不存在");
        }

        // 检查机台编码是否被其他机台使用
        if (updateReqVO.getMachineCode() != null && !updateReqVO.getMachineCode().equals(existMachine.getMachineCode())) {
            MachineDO byCode = machineMapper.selectByMachineCode(updateReqVO.getMachineCode());
            if (byCode != null && !byCode.getId().equals(updateReqVO.getId())) {
                throw new RuntimeException("机台编码已被其他机台使用");
            }
        }

        // 转换并更新
        MachineDO machineDO = machineConvert.toMachineDO(updateReqVO);
        machineMapper.updateById(machineDO);
        log.info("【机台管理】更新机台成功，ID: {}", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMachine(Long id) {
        MachineDO machine = machineMapper.selectById(id);
        if (machine == null) {
            throw new RuntimeException("机台不存在");
        }
        machineMapper.deleteById(id);
        log.info("【机台管理】删除机台成功，ID: {}", id);
    }

    @Override
    public MachineRespVO getMachine(Long id) {
        MachineDO machineDO = machineMapper.selectById(id);
        return machineConvert.toMachineRespVO(machineDO);
    }

    @Override
    public MachineRespVO getMachineByCode(String machineCode) {
        MachineDO machineDO = machineMapper.selectByMachineCode(machineCode);
        return machineConvert.toMachineRespVO(machineDO);
    }

    @Override
    public List<MachineRespVO> getMachineList(MachineListReqVO listReqVO) {
        LambdaQueryWrapper<MachineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(listReqVO.getMachineCode() != null, MachineDO::getMachineCode, listReqVO.getMachineCode())
                .like(listReqVO.getMachineName() != null, MachineDO::getMachineName, listReqVO.getMachineName())
                .eq(listReqVO.getMachineType() != null, MachineDO::getMachineType, listReqVO.getMachineType())
                .eq(listReqVO.getWorkshop() != null, MachineDO::getWorkshop, listReqVO.getWorkshop())
                .eq(listReqVO.getStatus() != null, MachineDO::getStatus, listReqVO.getStatus())
                .orderByDesc(MachineDO::getCreateTime);

        List<MachineDO> machineList = machineMapper.selectList(wrapper);
        return machineConvert.toMachineRespVOList(machineList);
    }

    @Override
    public PageResult<MachineRespVO> getMachinePage(MachinePageReqVO pageReqVO) {
        IPage<MachineDO> page = machineMapper.selectPage(
                new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
                new LambdaQueryWrapper<MachineDO>()
                        .like(pageReqVO.getMachineCode() != null, MachineDO::getMachineCode, pageReqVO.getMachineCode())
                        .like(pageReqVO.getMachineName() != null, MachineDO::getMachineName, pageReqVO.getMachineName())
                        .eq(pageReqVO.getMachineType() != null, MachineDO::getMachineType, pageReqVO.getMachineType())
                        .eq(pageReqVO.getWorkshop() != null, MachineDO::getWorkshop, pageReqVO.getWorkshop())
                        .eq(pageReqVO.getStatus() != null, MachineDO::getStatus, pageReqVO.getStatus())
                        .orderByDesc(MachineDO::getCreateTime)
        );

        PageResult<MachineRespVO> result = new PageResult<>();
        result.setList(machineConvert.toMachineRespVOList(page.getRecords()));
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public List<MachineRespVO> getIdleMachineList() {
        List<MachineDO> idleMachines = machineMapper.selectIdleMachines();
        return machineConvert.toMachineRespVOList(idleMachines);
    }
}
