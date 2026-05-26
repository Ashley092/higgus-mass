package io.higgus.lab.module.storage.controller.HiExcel.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel 单次单元格变更请求
 *
 * 行号、列号均使用数组索引（从 0 开始）
 */
@Slf4j
@Data
public class HiExcelSaveReqVO {

    /** 内容 ID（对应 content_metadata 表） */
    private Long contentId;

    /** 行号（数组索引，从 0 开始） */
    private Integer row;

    /** 列号（数组索引，从 0 开始） */
    private Integer col;

    /** 新的单元格内容（统一存为字符串） */
    private String newContent;

    /** 当前版本号（乐观锁） */
    private Integer version;

    /** 更新人 */
    private Long updater;
}
