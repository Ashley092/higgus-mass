package io.higgus.lab.module.bulong.dal.mysql.process;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品工艺明细表 Mapper
 */
@Mapper
public interface ProductProcessDetailMapper extends BaseMapperX<ProductProcessDetailDO> {

    /**
     * 根据工艺ID查询明细列表
     */
    default List<ProductProcessDetailDO> selectByProcessId(Long processId) {
        return selectList(new LambdaQueryWrapper<ProductProcessDetailDO>()
                .eq(ProductProcessDetailDO::getProcessId, processId)
                .orderByAsc(ProductProcessDetailDO::getGbIndex));
    }

    /**
     * 根据工艺ID和梳栉编号查询
     */
    default ProductProcessDetailDO selectByProcessAndGbIndex(Long processId, Integer gbIndex) {
        return selectOne(new LambdaQueryWrapper<ProductProcessDetailDO>()
                .eq(ProductProcessDetailDO::getProcessId, processId)
                .eq(ProductProcessDetailDO::getGbIndex, gbIndex));
    }

    /**
     * 删除工艺的所有明细
     */
    default void deleteByProcessId(Long processId) {
        delete(new LambdaQueryWrapper<ProductProcessDetailDO>()
                .eq(ProductProcessDetailDO::getProcessId, processId));
    }
}
