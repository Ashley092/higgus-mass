USE `mass`;


DROP TABLE IF EXISTS file_index ;
CREATE TABLE file_index (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `file_md5` VARCHAR(32) NOT NULL COMMENT '文件MD5唯一标识（32位小写）',
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_ext` VARCHAR(20) NOT NULL COMMENT '文件后缀名(如: txt, png, mp4)',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节 Byte）',
    `file_path` VARCHAR(512) NOT NULL COMMENT '文件在服务器磁盘上的实际绝对路径/相对路径',
    `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '逻辑删除标记 (0:正常, 1:已删除)',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
-- 关键索引设计：根据 MD5 查询文件是否存在，以及高并发下的唯一性防重
    UNIQUE KEY `idx_file_md5` (`file_md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件明细索引表';

