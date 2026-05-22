package io.higgus.lab.module.bulong.dal.dataobject.process;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 产品工艺明细表 DO（梳栉等工艺参数）
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_product_process_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcessDetailDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 关联工艺主表ID
     */
    private Long processId;

    /**
     * 梳栉编号（1, 2, 3...）
     */
    private Integer gbIndex;

    /**
     * 送纱量
     */
    private BigDecimal yarnFeed;

    /**
     * 穿纱方式
     */
    private String threadingType;

    /**
     * 纱线规格
     */
    private String yarnCount;

    /**
     * 纱线品牌/型号
     */
    private String yarnBrand;

    /**
     * 目标张力（N）
     */
    private BigDecimal tensionTarget;

    /**
     * 针型
     */
    private String needleType;

    /**
     * 机号/针距
     */
    private String needleGauge;

    /**
     * 梳栉排列图
     */
    private String guideBarPattern;

    /**
     * 链节顺序
     */
    private String chainLinkSequence;

    /**
     * 花型循环数
     */
    private Integer patternRepeat;

    /**
     * 备注
     */
    private String remark;
}
