-- ================================================
-- 协作编辑操作日志表（事件溯源）
-- ================================================

CREATE TABLE IF NOT EXISTS `edition_log` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '日志ID',
    `content_id`   BIGINT          NOT NULL                COMMENT '内容ID（关联content_metadata表）',
    `version`       INT             NOT NULL                COMMENT '版本号（从1开始递增）',
    `row_index`     INT             NOT NULL                COMMENT '行号（数组索引，从0开始）',
    `col_index`     INT             NOT NULL                COMMENT '列号（数组索引，从0开始）',
    `old_value`     VARCHAR(4000)   DEFAULT NULL            COMMENT '旧值（修改前的单元格内容）',
    `new_value`     VARCHAR(4000)   DEFAULT NULL            COMMENT '新值（修改后的单元格内容）',
    `updater`       BIGINT          NOT NULL                COMMENT '更新人ID',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
    PRIMARY KEY (`id`),
    -- 按 content_id + version 查单条（版本回滚用）
    UNIQUE KEY `uk_content_version` (`content_id`, `version`),
    -- 按 content_id + create_time 查列表（历史记录用）
    INDEX `idx_content_createtime` (`content_id`, `create_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='协作编辑操作日志';
