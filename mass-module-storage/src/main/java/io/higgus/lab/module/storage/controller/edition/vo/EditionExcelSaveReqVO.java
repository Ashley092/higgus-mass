package io.higgus.lab.module.storage.controller.edition.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel 单次单元格变更请求
 *
 * 行号、列号均使用数组索引（从 0 开始）
 */
@Slf4j
@Data
public class EditionExcelSaveReqVO {

    /** 内容 ID（对应 content_metadata 表） */
    private String contentId;

    /** 行号（数组索引，从 0 开始） */
    private Integer rowIndex;

    /** 列号（数组索引，从 0 开始） */
    private Integer colIndex;

    /** 新的单元格内容（统一存为字符串） */
    private String newValue;

//    /** 当前版本号（乐观锁）
//     *  不会啊？前端为什么会知道乐观锁呢？？这个是有问题的。
//     * */
//    private Integer version;

    /** 更新人 */
    private Long updater;
}
