package io.higgus.lab.module.storage.controller.vo;


import lombok.Data;

import java.math.BigInteger;

@Data
public class CellUpdateReqVO {
    private String fileId;      // Excel 文件 ID
    private String sheetId;     // Sheet ID
    private Integer row;        // 行号
    private Integer col;        // 列号
    private Integer action;    // 涉及到操作的类型；插入？修改样式？
    private String oldValue;    // 修改前的值
    private String newValue;    // 修改后的值
    private String userId;
}
