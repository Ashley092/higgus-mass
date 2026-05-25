
USE `mass`;

DROP TABLE IF EXISTS `collaboration_space`;
DROP TABLE IF EXISTS `collaboration_item`;
DROP TABLE IF EXISTS `content_metadata`;

CREATE TABLE `collaboration_space` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '空间ID',
    `name` VARCHAR(64) NOT NULL COMMENT '空间/团队名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '空间描述',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0正常, 1禁用/归档',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第一层：协作空间/团队表';

CREATE TABLE `collaboration_item` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '项目/渠道ID',
    `space_id` BIGINT UNSIGNED NOT NULL COMMENT '所属空间ID',
    `name` VARCHAR(64) NOT NULL COMMENT '项目/渠道/知识库名称',
    `type` TINYINT NOT NULL COMMENT '类型: 1项目(Project), 2渠道(Channel), 3知识库(Wiki)',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_space_id` (`space_id`) COMMENT '方便查询某个空间下的所有项目'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第二层：项目/渠道/知识库表';

CREATE TABLE `content_metadata` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '内容元数据ID',
    `item_id` BIGINT UNSIGNED NOT NULL COMMENT '所属第二层(项目/渠道/Wiki)ID',
    `title` VARCHAR(128) NOT NULL COMMENT '内容标题/文件名(用户看到的名字)',
    `content_type` TINYINT NOT NULL COMMENT '内容本体类型: 1文件(File), 2任务(Task), 3在线文档(Doc), 4表格(Sheet)',

    -- 文件特有字段 (当 content_type = 1 时有效，其他类型可为默认值或NULL)
    `storage_key` VARCHAR(255) DEFAULT NULL COMMENT 'MinIO中的Object Key(建议用UUID/Hash，避免中文乱码)',
    `file_size` BIGINT UNSIGNED DEFAULT 0 COMMENT '文件大小(Byte)',
    `file_extension` VARCHAR(16) DEFAULT NULL COMMENT '文件后缀名(如: pdf, docx, png)',
    `file_md5` CHAR(32) DEFAULT NULL COMMENT '文件MD5值(未来用于实现秒传和去重)',

    -- 协作与并发控制
    `version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '乐观锁版本号(用于未来并发操作控制)',
    `creator` BIGINT UNSIGNED NOT NULL COMMENT '创建者/上传者ID',
    `updater` BIGINT UNSIGNED NOT NULL COMMENT '最后修改者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_item_id_type` (`item_id`, `content_type`) COMMENT '提升某个项目下按类型筛选内容的性能',
    KEY `idx_file_md5` (`file_md5`) COMMENT '未来用于秒传查询'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三层：具体内容本体表(核心存储模块)';