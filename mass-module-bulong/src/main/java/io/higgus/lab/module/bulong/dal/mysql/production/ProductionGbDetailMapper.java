package io.higgus.lab.module.bulong.dal.mysql.production;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionGbDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 梳栉工艺明细从表 Mapper
 */
@Mapper
public interface ProductionGbDetailMapper extends BaseMapperX<ProductionGbDetailDO> {

    /**
     * 根据生产记录序号查询明细列表
     */
    default List<ProductionGbDetailDO> selectByRecordId(String recordId) {
        return selectList(new LambdaQueryWrapper<ProductionGbDetailDO>()
                .eq(ProductionGbDetailDO::getRecordId, recordId));
    }

    /**
     * 根据生产记录序号和梳栉序号查询
     */
    default ProductionGbDetailDO selectByRecordIdAndGbIndex(String recordId, Integer gbIndex) {
        return selectOne(new LambdaQueryWrapper<ProductionGbDetailDO>()
                .eq(ProductionGbDetailDO::getRecordId, recordId)
                .eq(ProductionGbDetailDO::getGbIndex, gbIndex));
    }
}
