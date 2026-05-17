package io.higgus.lab.module.bulong.dal.dataobject.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO extends BaseDO {

    /**
     * 产品编号
     * 注意：不是自增 id，使用业务编号
     */
    @TableId
    private Long id;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 理论门幅
     */
    private BigDecimal theoreticalWidth;

    /**
     * 理论密度
     */
    private BigDecimal theoreticalDensity;

    /**
     * 理论间隔
     */
    private BigDecimal theoreticalSpacing;

    /**
     * 记录日期
     */
    private LocalDateTime recordDate;

    /**
     * 是否有效 (1: 有效, 0: 无效)
     */
    private Boolean isActive;

    /**
     * 产品路由ID（准备弃用）
     */
    private String productRouteId;
}
