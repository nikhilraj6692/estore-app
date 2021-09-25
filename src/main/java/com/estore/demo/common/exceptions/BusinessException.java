package com.estore.demo.common.exceptions;

import com.estore.demo.common.domain.ErrorListVO;
import com.estore.demo.common.domain.ErrorVO;

import java.util.List;

/*
Checked exception class to throw Service or Validation exceptions
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 2579889832488145692L;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(String errorCode) {
        super(errorCode);

    }

    public BusinessException(String errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public BusinessException(ErrorVO errorVO) {
        super(errorVO);
    }

    public BusinessException(List<ErrorVO> errorVOs) {
        super(errorVOs);
    }

    public BusinessException(ErrorListVO errorListVO) {
        super(errorListVO);
    }
}
