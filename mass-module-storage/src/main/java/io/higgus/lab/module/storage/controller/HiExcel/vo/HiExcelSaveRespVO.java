package io.higgus.lab.module.storage.controller.HiExcel.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class HiExcelSaveRespVO {

    private Boolean success;

    private Integer newVersion;

    private String errorMessage;

    private ErrorType errorType;

    public enum ErrorType {
        SUCCESS,
        VERSION_CONFLICT,
        FILE_NOT_FOUND,
        UNKNOWN_ERROR
    }

    public static HiExcelSaveRespVO success(Integer newVersion) {
        HiExcelSaveRespVO resp = new HiExcelSaveRespVO();
        resp.setSuccess(true);
        resp.setNewVersion(newVersion);
        resp.setErrorType(ErrorType.SUCCESS);
        return resp;
    }

    public static HiExcelSaveRespVO fail(String message, ErrorType errorType) {
        HiExcelSaveRespVO resp = new HiExcelSaveRespVO();
        resp.setSuccess(false);
        resp.setErrorMessage(message);
        resp.setErrorType(errorType);
        return resp;
    }
}
