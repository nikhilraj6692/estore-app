package com.estore.demo.common.exceptions;

import com.estore.demo.common.domain.ErrorVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/*
POJO to throw exception in a well defined form
 */
@XmlRootElement
public class EnterpriseErrorVO {

    private List<ErrorVO> errors;

    private Integer errorNumber;

    public List<ErrorVO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorVO> errors) {
        this.errors = errors;
    }

    public final Integer getErrorNumber() {
        return errorNumber;
    }
    
    public final void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

}
