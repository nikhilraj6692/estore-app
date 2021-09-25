package com.estore.demo.common.exceptions;

/*
Checked exception class to throw Authorization exception
 */
@SuppressWarnings("serial")
public class AccessDeniedException extends BaseException {

    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(String errorCode) {
        super(errorCode);
    }

    public AccessDeniedException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public AccessDeniedException(String errorCode, Object[] arguments) {
        super(errorCode, arguments);
    }
}
