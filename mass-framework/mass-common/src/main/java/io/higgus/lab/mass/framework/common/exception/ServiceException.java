package io.higgus.lab.mass.framework.common.exception;

public class ServiceException extends RuntimeException {



    private Long code;

    private String message;

    /**
     * 空构造，支持 Jackson 框架对其的操作。
     */

    ServiceException() {}
}
