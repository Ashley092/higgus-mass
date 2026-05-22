package io.higgus.lab.module.bulong.service.plan;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.convert.ProductionPlanConvert;
import io.higgus.lab.module.bulong.dal.dataobject.plan.ProductionPlanDO;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.bulong.dal.mysql.plan.ProductionPlanMapper;
import io.higgus.lab.module.bulong.dal.mysql.machine.MachineMapper;
import io.higgus.lab.module.bulong.dal.mysql.product.ProductMapper;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生产计划管理 Service 实现类
 */
@Slf4j
@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    @Resource
    private ProductionPlanMapper productionPlanMapper;

    @Resource
    private MachineMapper machineMapper;

    @Resource
    private ProductMapper productMapper;

    private static final ProductionPlanConvert planConvert = ProductionPlanConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlan(ProductionPlanCreateReqVO createReqVO) {
        // 检查计划编码是否已存在
        ProductionPlanDO existPlan = productionPlanMapper.selectByPlanCode(createReqVO.getPlanCode());
        if (existPlan != null) {
            throw new RuntimeException("计划编码已存在");
        }

        // 转换并保存
        ProductionPlanDO planDO = planConvert.toProductionPlanDO(createReqVO);
        // 设置默认状态
        if (planDO.getStatus() == null) {
            planDO.setStatus("DRAFT");
        }
        // 设置默认优先级
        if (planDO.getPriority() == null) {
            planDO.setPriority(5);
        }
        productionPlanMapper.insert(planDO);
        log.info("【生产计划】创建计划成功，ID: {}, 编码: {}", planDO.getId(), planDO.getPlanCode());

        return planDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(ProductionPlanUpdateReqVO updateReqVO) {
        // 校验计划存在
        ProductionPlanDO existPlan = productionPlanMapper.selectById(updateReqVO.getId());
        if (existPlan == null) {
            throw new RuntimeException("生产计划不存在");
        }

        // 检查计划编码是否被其他计划使用
        if (updateReqVO.getPlanCode() != null && !updateReqVO.getPlanCode().equals(existPlan.getPlanCode())) {
            ProductionPlanDO byCode = productionPlanMapper.selectByPlanCode(updateReqVO.getPlanCode());
            if (byCode != null && !byCode.getId().equals(updateReqVO.getId())) {
                throw new RuntimeException("计划编码已被其他计划使用");
            }
        }

        // 转换并更新
        ProductionPlanDO planDO = planConvert.toProductionPlanDO(updateReqVO);
        productionPlanMapper.updateById(planDO);
        log.info("【生产计划】更新计划成功，ID: {}", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        productionPlanMapper.deleteById(id);
        log.info("【生产计划】删除计划成功，ID: {}", id);
    }

    @Override
    public ProductionPlanRespVO getPlan(Long id) {
        ProductionPlanDO planDO = productionPlanMapper.selectById(id);
        return fillPlanDetails(planConvert.toProductionPlanRespVO(planDO), planDO);
    }

    @Override
    public ProductionPlanRespVO getPlanByCode(String planCode) {
        ProductionPlanDO planDO = productionPlanMapper.selectByPlanCode(planCode);
        return fillPlanDetails(planConvert.toProductionPlanRespVO(planDO), planDO);
    }

    @Override
    public List<ProductionPlanRespVO> getPlanList(ProductionPlanListReqVO listReqVO) {
        LambdaQueryWrapper<ProductionPlanDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(listReqVO.getPlanCode() != null, ProductionPlanDO::getPlanCode, listReqVO.getPlanCode())
                .eq(listReqVO.getProductId() != null, ProductionPlanDO::getProductId, listReqVO.getProductId())
                .eq(listReqVO.getMachineId() != null, ProductionPlanDO::getMachineId, listReqVO.getMachineId())
                .eq(listReqVO.getStatus() != null, ProductionPlanDO::getStatus, listReqVO.getStatus())
                .eq(listReqVO.getPriority() != null, ProductionPlanDO::getPriority, listReqVO.getPriority())
                .orderByDesc(ProductionPlanDO::getCreateTime);

        List<ProductionPlanDO> planList = productionPlanMapper.selectList(wrapper);
        List<ProductionPlanRespVO> respList = planConvert.toProductionPlanRespVOList(planList);
        return fillPlanDetailList(respList, planList);
    }

    @Override
    public PageResult<ProductionPlanRespVO> getPlanPage(ProductionPlanPageReqVO pageReqVO) {
        IPage<ProductionPlanDO> page = productionPlanMapper.selectPage(
                new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
                new LambdaQueryWrapper<ProductionPlanDO>()
                        .like(pageReqVO.getPlanCode() != null, ProductionPlanDO::getPlanCode, pageReqVO.getPlanCode())
                        .eq(pageReqVO.getProductId() != null, ProductionPlanDO::getProductId, pageReqVO.getProductId())
                        .eq(pageReqVO.getMachineId() != null, ProductionPlanDO::getMachineId, pageReqVO.getMachineId())
                        .eq(pageReqVO.getStatus() != null, ProductionPlanDO::getStatus, pageReqVO.getStatus())
                        .eq(pageReqVO.getPriority() != null, ProductionPlanDO::getPriority, pageReqVO.getPriority())
                        .ge(pageReqVO.getPlannedStartFrom() != null, ProductionPlanDO::getPlannedStart, pageReqVO.getPlannedStartFrom())
                        .le(pageReqVO.getPlannedStartTo() != null, ProductionPlanDO::getPlannedStart, pageReqVO.getPlannedStartTo())
                        .orderByDesc(ProductionPlanDO::getCreateTime)
        );

        List<ProductionPlanRespVO> respList = planConvert.toProductionPlanRespVOList(page.getRecords());
        fillPlanDetailList(respList, page.getRecords());

        PageResult<ProductionPlanRespVO> result = new PageResult<>();
        result.setList(respList);
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public List<ProductionPlanRespVO> getUnassignedPlanList() {
        List<ProductionPlanDO> planList = productionPlanMapper.selectUnassignedPlans();
        List<ProductionPlanRespVO> respList = planConvert.toProductionPlanRespVOList(planList);
        return fillPlanDetailList(respList, planList);
    }

    @Override
    public List<ProductionPlanRespVO> getPlanListByMachineAndTimeRange(Long machineId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductionPlanDO> planList = productionPlanMapper.selectByMachineAndTimeRange(machineId, startTime, endTime);
        List<ProductionPlanRespVO> respList = planConvert.toProductionPlanRespVOList(planList);
        return fillPlanDetailList(respList, planList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long id) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if (!"DRAFT".equals(plan.getStatus())) {
            throw new RuntimeException("只有草稿状态的计划可以提交审核");
        }

        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("PENDING");
        productionPlanMapper.updateById(updateDO);
        log.info("【生产计划】提交审核，计划ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePlan(Long id, String approvedBy) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if (!"PENDING".equals(plan.getStatus())) {
            throw new RuntimeException("只有待审核状态的计划可以审核");
        }

        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("APPROVED");
        updateDO.setApprovedBy(approvedBy);
        updateDO.setApprovedAt(LocalDateTime.now());
        productionPlanMapper.updateById(updateDO);
        log.info("【生产计划】审核通过，计划ID: {}, 审核人: {}", id, approvedBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectPlan(Long id) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if (!"PENDING".equals(plan.getStatus())) {
            throw new RuntimeException("只有待审核状态的计划可以拒绝");
        }

        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("DRAFT");
        productionPlanMapper.updateById(updateDO);
        log.info("【生产计划】审核拒绝，计划ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProduction(Long id) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if (!"APPROVED".equals(plan.getStatus())) {
            throw new RuntimeException("只有已审核状态的计划可以开始生产");
        }
        if (plan.getMachineId() == null) {
            throw new RuntimeException("计划未分配机台，无法开始生产");
        }

        // 更新计划状态
        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("IN_PROGRESS");
        updateDO.setActualStart(LocalDateTime.now());
        productionPlanMapper.updateById(updateDO);

        // 更新机台状态
        MachineDO machine = machineMapper.selectById(plan.getMachineId());
        if (machine != null) {
            machine.setStatus("RUNNING");
            machine.setCurrentProductId(plan.getProductId());
            machineMapper.updateById(machine);
        }

        log.info("【生产计划】开始生产，计划ID: {}, 机台ID: {}", id, plan.getMachineId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeProduction(Long id, BigDecimal actualOutput, BigDecimal defectOutput) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if (!"IN_PROGRESS".equals(plan.getStatus())) {
            throw new RuntimeException("只有生产中的计划可以完成");
        }

        // 计算疪品率
        BigDecimal defectRate = BigDecimal.ZERO;
        if (actualOutput != null && actualOutput.compareTo(BigDecimal.ZERO) > 0 && defectOutput != null) {
            defectRate = defectOutput.divide(actualOutput, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        // 更新计划
        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("COMPLETED");
        updateDO.setActualEnd(LocalDateTime.now());
        updateDO.setActualOutput(actualOutput);
        updateDO.setDefectOutput(defectOutput);
        updateDO.setDefectRate(defectRate);
        productionPlanMapper.updateById(updateDO);

        // 更新机台状态
        if (plan.getMachineId() != null) {
            MachineDO machine = machineMapper.selectById(plan.getMachineId());
            if (machine != null) {
                machine.setStatus("IDLE");
                machine.setCurrentProductId(null);
                // 累计产量
                if (actualOutput != null) {
                    machine.setTotalOutput(
                            machine.getTotalOutput() != null ? machine.getTotalOutput().add(actualOutput) : actualOutput
                    );
                }
                machineMapper.updateById(machine);
            }
        }

        log.info("【生产计划】完成生产，计划ID: {}, 实际产量: {}, 疪品率: {}%", id, actualOutput, defectRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPlan(Long id) {
        ProductionPlanDO plan = productionPlanMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }
        if ("COMPLETED".equals(plan.getStatus()) || "CANCELLED".equals(plan.getStatus())) {
            throw new RuntimeException("已完成或已取消的计划无法取消");
        }

        // 如果是生产中，需要释放机台
        if ("IN_PROGRESS".equals(plan.getStatus()) && plan.getMachineId() != null) {
            MachineDO machine = machineMapper.selectById(plan.getMachineId());
            if (machine != null) {
                machine.setStatus("IDLE");
                machine.setCurrentProductId(null);
                machineMapper.updateById(machine);
            }
        }

        ProductionPlanDO updateDO = new ProductionPlanDO();
        updateDO.setId(id);
        updateDO.setStatus("CANCELLED");
        productionPlanMapper.updateById(updateDO);
        log.info("【生产计划】取消计划，计划ID: {}", id);
    }

    /**
     * 填充计划详情（产品、机台信息）
     */
    private ProductionPlanRespVO fillPlanDetails(ProductionPlanRespVO respVO, ProductionPlanDO planDO) {
        if (respVO == null || planDO == null) {
            return respVO;
        }
        // 填充产品信息
        if (planDO.getProductId() != null) {
            ProductDO product = productMapper.selectById(planDO.getProductId());
            if (product != null) {
                respVO.setProductName(product.getProductName());
            }
        }
        // 填充机台信息
        if (planDO.getMachineId() != null) {
            MachineDO machine = machineMapper.selectById(planDO.getMachineId());
            if (machine != null) {
                respVO.setMachineCode(machine.getMachineCode());
                respVO.setMachineName(machine.getMachineName());
            }
        }
        return respVO;
    }

    /**
     * 批量填充计划详情
     */
    private List<ProductionPlanRespVO> fillPlanDetailList(List<ProductionPlanRespVO> respList, List<ProductionPlanDO> planList) {
        if (respList == null || respList.isEmpty()) {
            return respList;
        }
        // 收集产品ID和机台ID
        List<Long> productIds = planList.stream()
                .map(ProductionPlanDO::getProductId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        List<Long> machineIds = planList.stream()
                .map(ProductionPlanDO::getMachineId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询
        List<ProductDO> products = productIds.isEmpty() ? List.of() : productMapper.selectBatchIds(productIds);
        List<MachineDO> machines = machineIds.isEmpty() ? List.of() : machineMapper.selectBatchIds(machineIds);

        // 构建ID到对象的映射
        java.util.Map<Long, ProductDO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDO::getId, p -> p));
        java.util.Map<Long, MachineDO> machineMap = machines.stream()
                .collect(Collectors.toMap(MachineDO::getId, m -> m));

        // 填充详情
        for (int i = 0; i < respList.size(); i++) {
            ProductionPlanRespVO resp = respList.get(i);
            ProductionPlanDO plan = planList.get(i);

            if (plan.getProductId() != null && productMap.containsKey(plan.getProductId())) {
                resp.setProductName(productMap.get(plan.getProductId()).getProductName());
            }
            if (plan.getMachineId() != null && machineMap.containsKey(plan.getMachineId())) {
                MachineDO machine = machineMap.get(plan.getMachineId());
                resp.setMachineCode(machine.getMachineCode());
                resp.setMachineName(machine.getMachineName());
            }
        }

        return respList;
    }
}
