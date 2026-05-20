package io.higgus.lab.module.bulong.dal.dataobject.production;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 梳栉工艺明细从表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_production_gb_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionGbDetailDO extends BaseDO {

    /**
     * 明细自增ID
     */
    @TableId
    private Long detailId;

    /**
     * 关联的生产记录序号
     * 外键关联 bl_production_record(record_id)
     */
    private String recordId;

    /**
     * 梳栉序号 (1=GB1, 2=GB2...)
     */
    private Integer gbIndex;

    /**
     * 送经量
     */
    private Integer yarnFeed;

    /**
     * 头纹/花型类型 (含位置模式如0000/5555)
     */
    private String patternType;

    /**
     * 穿丝方式 (如: 满穿/穿13空7)
     */
    private String threadingType;

    /**
     * 原料规格 (如: 75D/72F)
     */
    private String materialSpec;

    /**
     * 花高（位置模式字符串，可能较长）
     */
    private String patternHeight;

    /**
     * 用纱量 (如: 12.5kg)
     */
    private String yarnUsage;
}
