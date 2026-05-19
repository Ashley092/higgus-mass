package io.higgus.lab.module.ai.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import io.higgus.lab.module.bulong.dal.mysql.production.ProductionRecordMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 生产数据查询工具 - 供 AI Agent 调用
 */
@Slf4j
@Component
public class MachineQueryTools {

    @Resource
    private ProductionRecordMapper productionRecordMapper;

    /**
     * 查询生产记录列表
     * AI 可以调用此工具获取生产相关信息
     */
    @Tool(name = "query_production_records", 
          description = "查询生产记录列表，可用于查询某个机台的生产任务、某个产品的生产历史等")
    public String queryProductionRecords(
            @ToolParam(description = "机台编码，如 RSE4-01，为空则查询所有") String machineCode,
            @ToolParam(description = "产品编号，为空则查询所有") String productCode
    ) {
        log.info("AI 调用工具: queryProductionRecords, machineCode={}, productCode={}", machineCode, productCode);
        
        try {
            LambdaQueryWrapper<ProductionRecordDO> wrapper = new LambdaQueryWrapper<>();
            
            if (machineCode != null && !machineCode.trim().isEmpty()) {
                wrapper.eq(ProductionRecordDO::getMachineCode, machineCode.trim());
            }
            if (productCode != null && !productCode.trim().isEmpty()) {
                wrapper.eq(ProductionRecordDO::getProductCode, productCode.trim());
            }
            
            // 只查询有效的记录
            wrapper.eq(ProductionRecordDO::getIsActive, true);
            wrapper.orderByDesc(ProductionRecordDO::getCreateTime);
            
            List<ProductionRecordDO> records = productionRecordMapper.selectList(wrapper);
            
            if (records == null || records.isEmpty()) {
                return "未找到符合条件的生产记录";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("找到 ").append(records.size()).append(" 条生产记录：\n\n");
            
            for (ProductionRecordDO record : records) {
                sb.append("- 记录ID: ").append(record.getRecordId()).append("\n");
                sb.append("  产品编号: ").append(record.getProductCode()).append("\n");
                sb.append("  机台编号: ").append(record.getMachineCode() != null ? record.getMachineCode() : "未分配").append("\n");
                if (record.getActualWidth() != null) {
                    sb.append("  实际门幅: ").append(record.getActualWidth()).append("\n");
                }
                if (record.getActualDensity() != null) {
                    sb.append("  实际密度: ").append(record.getActualDensity()).append("\n");
                }
                if (record.getWeightGsm() != null) {
                    sb.append("  克重: ").append(record.getWeightGsm()).append(" g/㎡\n");
                }
                if (record.getRemark() != null && !record.getRemark().isEmpty()) {
                    sb.append("  备注: ").append(record.getRemark()).append("\n");
                }
                sb.append("\n");
            }
            
            return sb.toString();
            
        } catch (Exception e) {
            log.error("查询生产记录失败", e);
            return "查询生产记录失败: " + e.getMessage();
        }
    }

    /**
     * 根据机台编号查询该机台的生产记录
     */
    @Tool(name = "query_machine_production", 
          description = "查询指定机台的生产记录列表")
    public String queryMachineProduction(
            @ToolParam(description = "机台编码，如 RSE4-01") String machineCode
    ) {
        log.info("AI 调用工具: queryMachineProduction, machineCode={}", machineCode);
        
        if (machineCode == null || machineCode.trim().isEmpty()) {
            return "机台编码不能为空";
        }
        
        return queryProductionRecords(machineCode.trim(), null);
    }

    /**
     * 根据产品编号查询该产品的生产记录
     */
    @Tool(name = "query_product_production", 
          description = "查询指定产品的生产记录列表")
    public String queryProductProduction(
            @ToolParam(description = "产品编号") String productCode
    ) {
        log.info("AI 调用工具: queryProductProduction, productCode={}", productCode);
        
        if (productCode == null || productCode.trim().isEmpty()) {
            return "产品编号不能为空";
        }
        
        return queryProductionRecords(null, productCode.trim());
    }

    /**
     * 获取所有机台的生产概览
     */
    @Tool(name = "get_all_machines_overview", 
          description = "获取所有机台的生产记录概览，了解各机台的生产情况")
    public String getAllMachinesOverview() {
        log.info("AI 调用工具: getAllMachinesOverview");
        
        try {
            LambdaQueryWrapper<ProductionRecordDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductionRecordDO::getIsActive, true);
            wrapper.isNotNull(ProductionRecordDO::getMachineCode);
            wrapper.orderByDesc(ProductionRecordDO::getCreateTime);
            
            List<ProductionRecordDO> records = productionRecordMapper.selectList(wrapper);
            
            if (records == null || records.isEmpty()) {
                return "当前没有生产记录";
            }
            
            // 按机台分组统计
            java.util.Map<String, java.util.List<ProductionRecordDO>> byMachine = 
                records.stream()
                    .collect(java.util.stream.Collectors.groupingBy(ProductionRecordDO::getMachineCode));
            
            StringBuilder sb = new StringBuilder();
            sb.append("共有 ").append(byMachine.size()).append(" 个机台有生产记录：\n\n");
            
            byMachine.forEach((machineCode, machineRecords) -> {
                sb.append("【").append(machineCode).append("】");
                sb.append(" 共 ").append(machineRecords.size()).append(" 条记录\n");
            });
            
            return sb.toString();
            
        } catch (Exception e) {
            log.error("获取机台概览失败", e);
            return "获取机台概览失败: " + e.getMessage();
        }
    }
}
