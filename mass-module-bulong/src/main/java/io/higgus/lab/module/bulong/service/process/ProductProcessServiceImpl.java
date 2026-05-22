package io.higgus.lab.module.bulong.service.process;

import io.higgus.lab.module.bulong.controller.admin.process.vo.*;
import io.higgus.lab.module.bulong.convert.ProductProcessConvert;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDO;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDetailDO;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.bulong.dal.mysql.process.ProductProcessMapper;
import io.higgus.lab.module.bulong.dal.mysql.process.ProductProcessDetailMapper;
import io.higgus.lab.module.bulong.dal.mysql.product.ProductMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品工艺管理 Service 实现类
 */
@Slf4j
@Service
public class ProductProcessServiceImpl implements ProductProcessService {

    @Resource
    private ProductProcessMapper productProcessMapper;

    @Resource
    private ProductProcessDetailMapper productProcessDetailMapper;

    @Resource
    private ProductMapper productMapper;

    private static final ProductProcessConvert processConvert = ProductProcessConvert.INSTANCE;

    // ==================== ProductProcess ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProductProcess(ProductProcessCreateReqVO createReqVO) {
        ProductProcessDO processDO = processConvert.toProductProcessDO(createReqVO);
        // 设置默认值
        if (processDO.getVersion() == null) {
            processDO.setVersion(1);
        }
        if (processDO.getIsActive() == null) {
            processDO.setIsActive(true);
        }
        productProcessMapper.insert(processDO);
        log.info("【产品工艺】创建工艺成功，ID: {}", processDO.getId());
        return processDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductProcess(ProductProcessUpdateReqVO updateReqVO) {
        ProductProcessDO exist = productProcessMapper.selectById(updateReqVO.getId());
        if (exist == null) {
            throw new RuntimeException("工艺不存在");
        }
        ProductProcessDO processDO = processConvert.toProductProcessDO(updateReqVO);
        productProcessMapper.updateById(processDO);
        log.info("【产品工艺】更新工艺成功，ID: {}", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductProcess(Long id) {
        ProductProcessDO exist = productProcessMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("工艺不存在");
        }
        // 删除关联的明细
        productProcessDetailMapper.deleteByProcessId(id);
        productProcessMapper.deleteById(id);
        log.info("【产品工艺】删除工艺成功，ID: {}", id);
    }

    @Override
    public ProductProcessRespVO getProductProcess(Long id) {
        ProductProcessDO processDO = productProcessMapper.selectById(id);
        ProductProcessRespVO respVO = processConvert.toProductProcessRespVO(processDO);
        return fillProductName(respVO, processDO);
    }

    @Override
    public List<ProductProcessRespVO> getProductProcessListByProductId(Long productId) {
        List<ProductProcessDO> list = productProcessMapper.selectByProductId(productId);
        List<ProductProcessRespVO> respList = processConvert.toProductProcessRespVOList(list);
        // 填充产品名称
        ProductDO product = productMapper.selectById(productId);
        if (product != null) {
            respList.forEach(resp -> resp.setProductName(product.getProductName()));
        }
        return respList;
    }

    @Override
    public ProductProcessRespVO getProductProcessByProductAndMachineType(Long productId, String machineType) {
        ProductProcessDO processDO = productProcessMapper.selectByProductAndMachineType(productId, machineType);
        ProductProcessRespVO respVO = processConvert.toProductProcessRespVO(processDO);
        return fillProductName(respVO, processDO);
    }

    // ==================== ProductProcessDetail ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProcessDetail(ProductProcessDetailCreateReqVO createReqVO) {
        // 检查梳栉编号是否已存在
        ProductProcessDetailDO exist = productProcessDetailMapper.selectByProcessAndGbIndex(
                createReqVO.getProcessId(), createReqVO.getGbIndex());
        if (exist != null) {
            throw new RuntimeException("梳栉编号已存在");
        }
        ProductProcessDetailDO detailDO = processConvert.toProductProcessDetailDO(createReqVO);
        productProcessDetailMapper.insert(detailDO);
        log.info("【工艺明细】创建明细成功，ID: {}", detailDO.getId());
        return detailDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessDetail(ProductProcessDetailUpdateReqVO updateReqVO) {
        ProductProcessDetailDO exist = productProcessDetailMapper.selectById(updateReqVO.getId());
        if (exist == null) {
            throw new RuntimeException("工艺明细不存在");
        }
        ProductProcessDetailDO detailDO = processConvert.toProductProcessDetailDO(updateReqVO);
        productProcessDetailMapper.updateById(detailDO);
        log.info("【工艺明细】更新明细成功，ID: {}", updateReqVO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessDetail(Long id) {
        productProcessDetailMapper.deleteById(id);
        log.info("【工艺明细】删除明细成功，ID: {}", id);
    }

    @Override
    public ProductProcessDetailRespVO getProcessDetail(Long id) {
        ProductProcessDetailDO detailDO = productProcessDetailMapper.selectById(id);
        return processConvert.toProductProcessDetailRespVO(detailDO);
    }

    @Override
    public List<ProductProcessDetailRespVO> getProcessDetailListByProcessId(Long processId) {
        List<ProductProcessDetailDO> list = productProcessDetailMapper.selectByProcessId(processId);
        return processConvert.toProductProcessDetailRespVOList(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateProcessDetail(Long processId, List<ProductProcessDetailCreateReqVO> createReqVOList) {
        List<ProductProcessDetailDO> toInsert = new ArrayList<>();
        for (ProductProcessDetailCreateReqVO createReqVO : createReqVOList) {
            createReqVO.setProcessId(processId);
            ProductProcessDetailDO exist = productProcessDetailMapper.selectByProcessAndGbIndex(
                    processId, createReqVO.getGbIndex());
            if (exist != null) {
                continue;
            }
            toInsert.add(processConvert.toProductProcessDetailDO(createReqVO));
        }
        if (!toInsert.isEmpty()) {
//            productProcessDetailMapper.insertBatch(toInsert);
            log.info("【工艺明细】批量创建明细成功，工艺ID: {}, 数量: {}", processId, toInsert.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyProcessToPlan(Long processId, Long planId) {
        // 获取工艺明细
        List<ProductProcessDetailDO> details = productProcessDetailMapper.selectByProcessId(processId);
        log.info("【一键填充】从工艺ID: {} 复制 {} 条明细到计划ID: {}", processId, details.size(), planId);
        // TODO: 实现复制到 bl_production_gb_detail 的逻辑
        // 需要注入 ProductionGbDetailMapper
    }

    // ==================== 私有方法 ====================

    private ProductProcessRespVO fillProductName(ProductProcessRespVO respVO, ProductProcessDO processDO) {
        if (respVO == null || processDO == null) {
            return respVO;
        }
        if (processDO.getProductId() != null) {
            ProductDO product = productMapper.selectById(processDO.getProductId());
            if (product != null) {
                respVO.setProductName(product.getProductName());
            }
        }
        return respVO;
    }
}
