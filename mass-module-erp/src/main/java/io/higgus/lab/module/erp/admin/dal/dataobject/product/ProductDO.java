package io.higgus.lab.module.erp.admin.dal.dataobject.product;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName(value = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO {
    /**
     * 产品编号
     */
    @TableId(type = IdType.AUTO)  // 👈 关键：指定为自增主键
    private Long id;

    /**
     * 产品名称
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 理论门幅
     */
    private Double tWidth;

    /**
     * 理论密度
     */
    private Double tDensity;

    /**
     * 理论间隔
     */
    private Double tInterval;

    /**
     * 记录日期
     */
    private LocalDateTime recordDate;

    /**
     * 是否有效（1：有效，0：无效）
     */
    private Integer isValid;


}
