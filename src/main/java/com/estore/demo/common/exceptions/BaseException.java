package com.estore.demo.common.exceptions;

import com.estore.demo.common.domain.ErrorListVO;
import com.estore.demo.common.domain.ErrorVO;

import java.util.ArrayList;
import java.util.List;

/*
Parent exception class of all custom checked exceptions classes
 */
public abstract class BaseException extends RuntimeException {

    private static final String _INTERNAL_SERVER_ERROR = "internal.server.error";

    private static final ErrorVO[] NO_ERRORS = {};

    private static final long serialVersionUID = 8494810097885068773L;

    private final List<ErrorVO> errors = new ArrayList<ErrorVO>();

    public BaseException() {
        super();
        errors.add(new ErrorVO(_INTERNAL_SERVER_ERROR));
    }

    public BaseException(Throwable cause) {
        super(cause);
        errors.add(new ErrorVO(_INTERNAL_SERVER_ERROR));
    }

    public BaseException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        errors.add(new ErrorVO(errorCode));
    }

    public BaseException(String errorCode) {
        super("");
        errors.add(new ErrorVO(errorCode));
    }

    public BaseException(String errorCode, Object... arguments) {
        errors.add(new ErrorVO(errorCode, arguments));
    }


    public BaseException(ErrorVO errorVO) {
        errors.add(errorVO);
    }

    public BaseException(List<ErrorVO> errorVOs) {
        errors.addAll(errorVOs);
    }

    public BaseException(ErrorListVO errorListVO) {
        errors.addAll(errorListVO.getAllErrors());
    }

    public List<ErrorVO> getAllErrors() {
        return errors;
    }

    public int size() {
        return errors == null ? 0 : errors.size();
    }
}
