

-- 1. 删除旧表（如果存在）
DROP TABLE IF EXISTS `bl-production-gb-detail`;
DROP TABLE IF EXISTS `bl-production-record`;
DROP TABLE IF EXISTS `bl-product`;


-- 2. 创建生产记录主表
CREATE TABLE `bl-product` (
                              `id` BIGINT NOT NULL COMMENT '产品编号',
                              `product_code` VARCHAR(20) NOT NULL COMMENT '产品编号',
                              `product_name` VARCHAR(20) NOT NULL COMMENT '产品名称',
                              `theoretical_width` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论门幅',
                              `theoretical_density` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论密度',
                              `theoretical_spacing` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论间隔',
                              `record_date` DATETIME DEFAULT NULL COMMENT '记录日期',
                              `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否有效 (1: 有效, 0: 无效)',
                              `product_route_id` TEXT DEFAULT NULL COMMENT '产品路由ID',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

CREATE TABLE `bl-production-record` (
                                        `record_id` VARCHAR(32) NOT NULL COMMENT '生产记录序号 (PR001...)',
                                        `product_code` VARCHAR(20) NOT NULL COMMENT '产品编号',
                                        `actual_width` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际门幅',
                                        `actual_density` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际密度',
                                        `actual_spacing` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际间隔',
                                        `weight_gsm` DECIMAL(6, 2) DEFAULT NULL COMMENT '克重 (g/㎡)',
                                        `machine_code` VARCHAR(20) DEFAULT NULL COMMENT '生产机台号',
                                        `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '载入时间',
                                        `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                        `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否有效 (1:有效, 0:无效)',
                                        `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
                                        PRIMARY KEY (`record_id`),
                                        KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产记录主表';


-- 3. 创建梳栉工艺明细从表（核心优化点）
CREATE TABLE `bl-production-gb-detail` (
                                           `detail_id` BIGINT AUTO_INCREMENT COMMENT '明细自增ID',
                                           `record_id` VARCHAR(32) NOT NULL COMMENT '关联的生产记录序号',
                                           `gb_index` TINYINT NOT NULL COMMENT '梳栉序号 (例如: 1代表GB1, 2代表GB2...)',
                                           `yarn_feed` INT DEFAULT NULL COMMENT '送经量',
                                           `pattern_type` VARCHAR(50) DEFAULT NULL COMMENT '头纹/花型类型 (A型, B型...)',
                                           `threading_type` VARCHAR(50) DEFAULT NULL COMMENT '穿丝方式 (单穿, 双穿...)',
                                           `material_spec` VARCHAR(100) DEFAULT NULL COMMENT '原料规格 (如: 75D/72F)',
                                           `pattern_height` DECIMAL(6, 2) DEFAULT NULL COMMENT '花高',
                                           `yarn_usage` VARCHAR(50) DEFAULT NULL COMMENT '用纱量 (如: 12.5kg)',
                                           PRIMARY KEY (`detail_id`),
    -- 建立唯一索引，防止同一条生产记录下 GB1 被重复录入
                                           UNIQUE KEY `uk_record_gb` (`record_id`, `gb_index`),
    -- 外键约束：当主表记录被删除时，子表明细自动连带删除
                                           CONSTRAINT `fk_gb_detail_record` FOREIGN KEY (`record_id`) REFERENCES `bl-production-record` (`record_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产记录-梳栉工艺明细表';