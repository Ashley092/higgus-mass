package io.higgus.lab.mass.framework.common.pojo;

import io.higgus.lab.mass.framework.common.exception.enums.GlobalErrorCodeConstants;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

}
