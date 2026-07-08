
USE `mass`;

-- 内容快照表（关联 content_metadata，但不设外键）
CREATE TABLE `content_snapshot` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '快照ID',
    `content_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的内容ID',
    `snapshot_version` INT UNSIGNED NOT NULL COMMENT '快照版本号',
    `storage_key` VARCHAR(255) NOT NULL COMMENT 'MinIO中的Object Key',
    `file_size` BIGINT UNSIGNED DEFAULT 0 COMMENT '文件大小(Byte)',
    `file_md5` CHAR(32) DEFAULT NULL COMMENT '文件MD5值(上传完成后计算)',
    `edition_log_count` INT UNSIGNED DEFAULT 0 COMMENT '该快照包含的编辑日志数',
    `is_current` TINYINT NOT NULL DEFAULT 0 COMMENT '是否为当前版本: 0否, 1是',
    `creator` BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_content_version` (`content_id`, `snapshot_version`),
    KEY `idx_content_id` (`content_id`),
    KEY `idx_is_current` (`content_id`, `is_current`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容快照表';

-- 修改 content_metadata 表（新增两个字段）
ALTER TABLE `content_metadata`
    ADD COLUMN `current_snapshot_version` INT UNSIGNED DEFAULT 0 COMMENT '当前快照版本号',
    ADD COLUMN `current_storage_key` VARCHAR(255) DEFAULT NULL COMMENT '当前快照的MinIO地址';
