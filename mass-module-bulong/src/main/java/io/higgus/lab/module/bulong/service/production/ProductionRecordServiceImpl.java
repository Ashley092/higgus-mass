package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.convert.ProductionRecordConvert;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import io.higgus.lab.module.bulong.dal.mysql.production.ProductionRecordMapper;
import io.higgus.lab.module.bulong.service.common.ProductionRecordCacheService;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 生产记录管理 Service 实现类
 */
@Slf4j
@Service
public class ProductionRecordServiceImpl implements ProductionRecordService {

    @Resource
    private ProductionRecordMapper productionRecordMapper;

    @Resource
    private ProductionRecordCacheService productionRecordCacheService;

    private static final ProductionRecordConvert productionRecordConvert = ProductionRecordConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRecord(ProductionRecordCreateReqVO createReqVO) {
        // 生成 recordId: PR + 时间戳 + 随机数
        String recordId = generateRecordId();

        // 转换并保存
        ProductionRecordDO recordDO = productionRecordConvert.toProductionRecordDO(createReqVO);
        recordDO.setRecordId(recordId);
        productionRecordMapper.insert(recordDO);

        // 【新增】清除该产品的生产记录列表缓存
        if (createReqVO.getProductCode() != null) {
            productionRecordCacheService.evictListByProductCode(createReqVO.getProductCode());
            log.info("【缓存同步】新增生产记录，清除产品 {} 的记录列表缓存", createReqVO.getProductCode());
        }

        return recordDO.getRecordId();
    }

    private String generateRecordId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "PR" + timestamp + random;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(ProductionRecordUpdateReqVO updateReqVO) {
        // 校验记录存在
        ProductionRecordDO existRecord = productionRecordMapper.selectById(updateReqVO.getRecordId());
        if (existRecord == null) {
            throw new RuntimeException("生产记录不存在");
        }

        // 转换并更新
        ProductionRecordDO recordDO = productionRecordConvert.toProductionRecordDO(updateReqVO);
        productionRecordMapper.updateById(recordDO);

        // 【新增】清除缓存
        productionRecordCacheService.evictByRecordId(updateReqVO.getRecordId());
        // 如果产品编号变化，也要清除旧产品和新产品的列表缓存
        if (existRecord.getProductCode() != null) {
            productionRecordCacheService.evictListByProductCode(existRecord.getProductCode());
        }
        if (updateReqVO.getProductCode() != null) {
            productionRecordCacheService.evictListByProductCode(updateReqVO.getProductCode());
        }
        log.info("【缓存同步】更新生产记录 {}，清除相关缓存", updateReqVO.getRecordId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(String recordId) {
        // 先获取记录，用于清除相关缓存
        ProductionRecordDO record = productionRecordMapper.selectById(recordId);
        if (record != null && record.getProductCode() != null) {
            productionRecordCacheService.evictListByProductCode(record.getProductCode());
        }
        productionRecordCacheService.evictByRecordId(recordId);

        productionRecordMapper.deleteById(recordId);
        log.info("【缓存同步】删除生产记录 {}，清除相关缓存", recordId);
    }

    @Override
    public ProductionRecordRespVO getRecord(String recordId) {
        // 【第一步】先查缓存
        ProductionRecordDO cached = productionRecordCacheService.getByRecordId(recordId);
        if (cached != null) {
            log.info("【查询】生产记录: {} - 来源: 缓存", recordId);
            return productionRecordConvert.toProductionRecordRespVO(cached);
        }

        // 【第二步】缓存未命中，查询数据库
        log.info("【查询】生产记录: {} - 来源: 数据库", recordId);
        ProductionRecordDO recordDO = productionRecordMapper.selectById(recordId);

        // 【第三步】写入缓存
        if (recordDO != null) {
            productionRecordCacheService.setByRecordId(recordDO);
        }

        return productionRecordConvert.toProductionRecordRespVO(recordDO);
    }

    @Override
    public List<ProductionRecordRespVO> getRecordList(ProductionRecordListReqVO listReqVO) {
        List<ProductionRecordDO> recordList = productionRecordMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductionRecordDO>()
                .eq(listReqVO.getProductCode() != null, ProductionRecordDO::getProductCode, listReqVO.getProductCode())
                .eq(listReqVO.getIsActive() != null, ProductionRecordDO::getIsActive, listReqVO.getIsActive())
                .orderByDesc(ProductionRecordDO::getCreateTime)
        );
        return productionRecordConvert.toProductionRecordRespVOList(recordList);
    }

    @Override
    public PageResult<ProductionRecordRespVO> getRecordPage(ProductionRecordPageReqVO pageReqVO) {
        com.baomidou.mybatisplus.core.metadata.IPage<ProductionRecordDO> page = productionRecordMapper.selectPage(
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductionRecordDO>()
                .like(pageReqVO.getProductCode() != null, ProductionRecordDO::getProductCode, pageReqVO.getProductCode())
                .eq(pageReqVO.getIsActive() != null, ProductionRecordDO::getIsActive, pageReqVO.getIsActive())
                .orderByDesc(ProductionRecordDO::getCreateTime)
        );
        PageResult<ProductionRecordRespVO> result = new PageResult<>();
        result.setList(productionRecordConvert.toProductionRecordRespVOList(page.getRecords()));
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public List<ProductionRecordRespVO> getRecordListByProductCode(String productCode) {
        // 【第一步】先查缓存
        List<ProductionRecordDO> cachedList = productionRecordCacheService.getListByProductCode(productCode);
        if (cachedList != null) {
            log.info("【查询】产品 {} 的生产记录列表 - 来源: 缓存, 数量: {}", productCode, cachedList.size());
            return productionRecordConvert.toProductionRecordRespVOList(cachedList);
        }

        // 【第二步】缓存未命中，查询数据库
        log.info("【查询】产品 {} 的生产记录列表 - 来源: 数据库", productCode);
        List<ProductionRecordDO> recordList = productionRecordMapper.selectByProductCode(productCode);

        // 【第三步】写入缓存
        if (recordList != null && !recordList.isEmpty()) {
            productionRecordCacheService.setListByProductCode(productCode, recordList);
        }

        return productionRecordConvert.toProductionRecordRespVOList(recordList);
    }
}
