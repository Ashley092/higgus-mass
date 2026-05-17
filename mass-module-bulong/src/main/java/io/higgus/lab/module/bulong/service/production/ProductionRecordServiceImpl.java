package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.convert.ProductionRecordConvert;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import io.higgus.lab.module.bulong.dal.mysql.production.ProductionRecordMapper;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 生产记录管理 Service 实现类
 */
@Service
public class ProductionRecordServiceImpl implements ProductionRecordService {

    @Resource
    private ProductionRecordMapper productionRecordMapper;

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
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(String recordId) {
        productionRecordMapper.deleteById(recordId);
    }

    @Override
    public ProductionRecordRespVO getRecord(String recordId) {
        ProductionRecordDO recordDO = productionRecordMapper.selectById(recordId);
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
        List<ProductionRecordDO> recordList = productionRecordMapper.selectByProductCode(productCode);
        return productionRecordConvert.toProductionRecordRespVOList(recordList);
    }
}
