package io.higgus.lab.module.storage.controller.vo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CellChangeLog {
    private String logId;           // UUID
    private String fileId;
    private String sheetId;
    private Integer row;
    private Integer col;
    private String oldValue;
    private String newValue;
    private Long userId;
    private String userName;
    private Long timestamp;
    private String traceId;         // 用于链路追踪



}
