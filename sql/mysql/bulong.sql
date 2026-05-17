-- =====================================================
-- 布隆(Bulong)生产管理系统 数据库结构
-- 版本: v3.0
-- 说明: 
--   1. 修复表名使用下划线替代连字符
--   2. 添加 BaseDO 基础字段（create_time, update_time, creator, updater, deleted）
--   3. 修复主键设计问题，使用 record_id 作为主键
-- =====================================================


-- 1. 删除旧表（如果存在）
DROP TABLE IF EXISTS `bl_production_gb_detail`;
DROP TABLE IF EXISTS `bl_production_record`;
DROP TABLE IF EXISTS `bl_product`;


-- 2. 创建产品表
-- 产品表存储产品的基本信息和理论参数
CREATE TABLE `bl_product` (
                              `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT 'id序号',
                              `product_code` VARCHAR(20) NOT NULL COMMENT '产品编号',
                              `product_name` VARCHAR(50) NOT NULL COMMENT '产品名称',
                              `theoretical_width` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论门幅',
                              `theoretical_density` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论密度',
                              `theoretical_spacing` DECIMAL(6, 2) DEFAULT NULL COMMENT '理论间隔',
                              `record_date` DATETIME DEFAULT NULL COMMENT '记录日期',
                              `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否有效 (1: 有效, 0: 无效)',
                              `product_route_id` TEXT DEFAULT NULL COMMENT '产品路由ID - 准备弃用',
                              `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
                              `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
                              `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';


-- 3. 创建生产记录主表
-- 设计说明:
--   - 主键: record_id (业务序号)，而非自增 id
--   - 理由: 外键关联需要稳定的、有业务含义的标识符
--   - record_id 在数据中已验证唯一
CREATE TABLE `bl_production_record` (
                                        `record_id` VARCHAR(32) NOT NULL COMMENT '生产记录序号 (PR001/PR20250902222741)',
                                        `product_code` VARCHAR(20) NOT NULL COMMENT '产品编号',
                                        `actual_width` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际门幅',
                                        `actual_density` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际密度',
                                        `actual_spacing` DECIMAL(6, 2) DEFAULT NULL COMMENT '实际间隔',
                                        `weight_gsm` DECIMAL(6, 2) DEFAULT NULL COMMENT '克重 (g/㎡)',
                                        `machine_code` VARCHAR(20) DEFAULT NULL COMMENT '生产机台号',
                                        `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否有效 (1:有效, 0:无效)',
                                        `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
                                        `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
                                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
                                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                        PRIMARY KEY (`record_id`),
                                        KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产记录主表';


-- 4. 创建梳栉工艺明细从表
-- 设计说明:
--   - detail_id: 局部自增主键，用于唯一标识每条明细
--   - record_id: 外键，引用 bl_production_record(record_id)
--   - 唯一索引: (record_id, gb_index) 防止同一生产记录的梳栉重复
--   - 字段类型:
--     - yarn_feed: 送经量（纯数值，INT）
--     - pattern_type/threading_type/material_spec/yarn_usage: VARCHAR（含斜杠分隔符）
--     - pattern_height: VARCHAR（原数据格式不统一）
CREATE TABLE `bl_production_gb_detail` (
                                            `detail_id` BIGINT AUTO_INCREMENT NOT NULL COMMENT '明细自增ID',
                                            `record_id` VARCHAR(32) NOT NULL COMMENT '关联的生产记录序号',
                                            `gb_index` TINYINT NOT NULL COMMENT '梳栉序号 (1=GB1, 2=GB2...)',
                                            `yarn_feed` INT DEFAULT NULL COMMENT '送经量',
                                            `pattern_type` VARCHAR(255) DEFAULT NULL COMMENT '头纹/花型类型 (含位置模式如0000/5555)',
                                            `threading_type` VARCHAR(100) DEFAULT NULL COMMENT '穿丝方式 (如: 满穿/穿13空7)',
                                            `material_spec` VARCHAR(255) DEFAULT NULL COMMENT '原料规格 (如: 75D/72F)',
                                            `pattern_height` VARCHAR(255) DEFAULT NULL COMMENT '花高（位置模式字符串，可能较长）',
                                            `yarn_usage` VARCHAR(100) DEFAULT NULL COMMENT '用纱量 (如: 12.5kg)',
                                            `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
                                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
                                            `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                            PRIMARY KEY (`detail_id`),
                                            UNIQUE KEY `uk_record_gb` (`record_id`, `gb_index`),
                                            CONSTRAINT `fk_gb_detail_record` FOREIGN KEY (`record_id`) REFERENCES `bl_production_record` (`record_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产记录-梳栉工艺明细表';
