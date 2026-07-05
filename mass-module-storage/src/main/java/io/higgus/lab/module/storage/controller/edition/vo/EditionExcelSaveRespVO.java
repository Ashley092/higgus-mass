package io.higgus.lab.module.storage.controller.edition.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class EditionExcelSaveRespVO {

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

    public static EditionExcelSaveRespVO success(Integer newVersion) {
        EditionExcelSaveRespVO resp = new EditionExcelSaveRespVO();
        resp.setSuccess(true);
        resp.setNewVersion(newVersion);
        resp.setErrorType(ErrorType.SUCCESS);
        return resp;
    }

    public static EditionExcelSaveRespVO fail(String message, ErrorType errorType) {
        EditionExcelSaveRespVO resp = new EditionExcelSaveRespVO();
        resp.setSuccess(false);
        resp.setErrorMessage(message);
        resp.setErrorType(errorType);
        return resp;
    }
}
