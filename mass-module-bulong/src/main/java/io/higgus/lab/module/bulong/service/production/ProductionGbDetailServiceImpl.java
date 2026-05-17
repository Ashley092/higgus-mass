package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.convert.ProductionGbDetailConvert;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionGbDetailDO;
import io.higgus.lab.module.bulong.dal.mysql.production.ProductionGbDetailMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 梳栉工艺明细 Service 实现类
 */
@Service
public class ProductionGbDetailServiceImpl implements ProductionGbDetailService {

    @Resource
    private ProductionGbDetailMapper gbDetailMapper;

    private static final ProductionGbDetailConvert gbDetailConvert = ProductionGbDetailConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGbDetail(GbDetailCreateReqVO createReqVO) {
        ProductionGbDetailDO detailDO = gbDetailConvert.toProductionGbDetailDO(createReqVO);
        gbDetailMapper.insert(detailDO);
        return detailDO.getDetailId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGbDetail(GbDetailUpdateReqVO updateReqVO) {
        ProductionGbDetailDO existDetail = gbDetailMapper.selectById(updateReqVO.getDetailId());
        if (existDetail == null) {
            throw new RuntimeException("梳栉工艺明细不存在");
        }
        ProductionGbDetailDO detailDO = gbDetailConvert.toProductionGbDetailDO(updateReqVO);
        gbDetailMapper.updateById(detailDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGbDetail(Long detailId) {
        gbDetailMapper.deleteById(detailId);
    }

    @Override
    public GbDetailRespVO getGbDetail(Long detailId) {
        ProductionGbDetailDO detailDO = gbDetailMapper.selectById(detailId);
        return gbDetailConvert.toGbDetailRespVO(detailDO);
    }

    @Override
    public List<GbDetailRespVO> getGbDetailListByRecordId(String recordId) {
        List<ProductionGbDetailDO> detailList = gbDetailMapper.selectByRecordId(recordId);
        return gbDetailConvert.toGbDetailRespVOList(detailList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateGbDetails(String recordId, List<GbDetailCreateReqVO> createReqVOList) {
        for (GbDetailCreateReqVO createReqVO : createReqVOList) {
            createReqVO.setRecordId(recordId);
            createGbDetail(createReqVO);
        }
    }
}
