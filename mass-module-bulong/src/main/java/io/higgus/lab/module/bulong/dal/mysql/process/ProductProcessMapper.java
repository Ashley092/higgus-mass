package io.higgus.lab.module.bulong.dal.mysql.process;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品工艺主表 Mapper
 */
@Mapper
public interface ProductProcessMapper extends BaseMapperX<ProductProcessDO> {

    /**
     * 根据产品ID查询工艺列表
     */
    default List<ProductProcessDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<ProductProcessDO>()
                .eq(ProductProcessDO::getProductId, productId)
                .eq(ProductProcessDO::getIsActive, true)
                .orderByDesc(ProductProcessDO::getCreateTime));
    }

    /**
     * 根据产品ID和机台类型查询
     */
    default ProductProcessDO selectByProductAndMachineType(Long productId, String machineType) {
        return selectOne(new LambdaQueryWrapper<ProductProcessDO>()
                .eq(ProductProcessDO::getProductId, productId)
                .eq(ProductProcessDO::getMachineType, machineType)
                .eq(ProductProcessDO::getIsActive, true));
    }

    /**
     * 根据工艺编号查询
     */
    default ProductProcessDO selectByProcessCode(String processCode) {
        return selectOne(new LambdaQueryWrapper<ProductProcessDO>()
                .eq(ProductProcessDO::getProcessCode, processCode));
    }
}
