package com.estore.demo.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Wrapper POJO thrown with exception, containing user friendly message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class ErrorListVO implements Serializable {

    private List<ErrorVO> errors = new ArrayList<ErrorVO>();

    public void add(String errorCode) {
        errors.add(new ErrorVO(errorCode));
    }

    public void add(String errorCode, Object... arguments) {
        errors.add(new ErrorVO(errorCode, arguments));
    }

    public void add(ErrorVO errorVO) {
        errors.add(errorVO);
    }

    public void add(List<ErrorVO> errorVOs) {
        errors.addAll(errorVOs);
    }

    public List<ErrorVO> getAllErrors() {
        return Collections.unmodifiableList(errors);
    }

}
