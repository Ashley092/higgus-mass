package io.higgus.lab.module.bulong.dal.mysql.production;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生产记录主表 Mapper
 */
@Mapper
public interface ProductionRecordMapper extends BaseMapperX<ProductionRecordDO> {

    /**
     * 根据产品编号查询生产记录列表
     */
    default List<ProductionRecordDO> selectByProductCode(String productCode) {
        return selectList(new LambdaQueryWrapper<ProductionRecordDO>()
                .eq(ProductionRecordDO::getProductCode, productCode));
    }

    /**
     * 根据是否有效查询生产记录列表
     */
    default List<ProductionRecordDO> selectByIsActive(Boolean isActive) {
        return selectList(new LambdaQueryWrapper<ProductionRecordDO>()
                .eq(ProductionRecordDO::getIsActive, isActive));
    }
}
