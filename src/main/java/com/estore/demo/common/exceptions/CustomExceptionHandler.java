package com.estore.demo.common.exceptions;

import com.estore.demo.common.domain.ErrorVO;
import com.estore.demo.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/*
Spring managed exception handler to handle all kind of exceptions and throw exception with wrapped EnterpriseErrorVO containing
user friendly messages
 */
@ControllerAdvice
public class CustomExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    /*
    handles exception of different types
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest request, Exception exception) {
        EnterpriseErrorVO enterpriseErrorVO = new EnterpriseErrorVO();
        if (exception instanceof AccessDeniedException) {
            return getResponseForAccessDeniedException(enterpriseErrorVO, (AccessDeniedException) exception);
        } else if (exception instanceof ConstraintViolationException) {
            return getResponseForConstraintViolationException(enterpriseErrorVO, (ConstraintViolationException) exception);
        } else if (exception instanceof MethodArgumentNotValidException) {
            return getResponseForMethodArgumentNotValidException(enterpriseErrorVO, (MethodArgumentNotValidException) exception);
        } else if (exception instanceof BaseException) {
            return getResponseForBaseException(enterpriseErrorVO, (BaseException) exception);
        } else {
            return getResponseForGenericException(enterpriseErrorVO, exception);
        }
    }

    /*
    handles Hibernate validations
     */
    private ResponseEntity getResponseForMethodArgumentNotValidException(EnterpriseErrorVO enterpriseErrorVO, MethodArgumentNotValidException exception) {
        List<ErrorVO> errorList = new ArrayList<>(1);

        ErrorVO errorVO = new ErrorVO();
        errorVO.setMessage(messageSource.getMessage("payload.error", null, Locale.getDefault()));
        errorList.add(errorVO);

        enterpriseErrorVO.setErrors(errorList);

        ResponseEntity response = ResponseEntity.status(422).body(enterpriseErrorVO);
        return response;
    }

    /*
    handles Hibernate validations
     */
    private ResponseEntity getResponseForConstraintViolationException(EnterpriseErrorVO enterpriseErrorVO,
                                                                      ConstraintViolationException exception) {
        List<ErrorVO> errorList = new ArrayList<>();

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                String message = constraintViolation.getMessage();
                String formattedMsg = messageSource.getMessage(message, null, message, Locale.getDefault());
                ErrorVO errorVO = new ErrorVO();
                errorVO.setMessage(formattedMsg);
                errorList.add(errorVO);
            }
        } else {
            String message = exception.getMessage();
            String formattedMsg = messageSource.getMessage(message, null, message, Locale.getDefault());
            ErrorVO errorVO = new ErrorVO();
            errorVO.setMessage(formattedMsg);
            errorList.add(errorVO);
        }
        enterpriseErrorVO.setErrors(errorList);
        ResponseEntity response = ResponseEntity.unprocessableEntity().body(enterpriseErrorVO);
        return response;
    }

    /*
    handles Authorization related exceptions
     */
    private ResponseEntity getResponseForAccessDeniedException(EnterpriseErrorVO enterpriseErrorVO,
                                                               AccessDeniedException exception) {

        Object[] formattedArgs;
        StringBuilder defaultMessage = new StringBuilder();

        defaultMessage.append(messageSource.getMessage("http.forbidden.error", null, Locale.getDefault()));

        if (exception.size() > 0) {
            for (ErrorVO error : exception.getAllErrors()) {
                Object[] args = error.getArguments();
                if (args != null && args.length > 0) {
                    formattedArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        Object obj = args[i];
                        formattedArgs[i] = messageSource.getMessage(obj.toString(), null, obj.toString(), Locale.getDefault());
                    }
                } else {
                    error.setErrorCode(ApplicationConstants.ACCESS_DENIED_EXCEPTION_CODE);
                    error.setMessage(defaultMessage.toString());
                }
            }
        }


        if (exception.size() <= 0) {
            List<ErrorVO> errorList = new ArrayList<>(1);
            ErrorVO errorVO = new ErrorVO();
            errorVO.setMessage(defaultMessage.toString());
            errorVO.setErrorCode(ApplicationConstants.ACCESS_DENIED_EXCEPTION_CODE);
            errorList.add(errorVO);
            enterpriseErrorVO.setErrors(errorList);
        } else {
            enterpriseErrorVO.setErrors(exception.getAllErrors());
        }

        ResponseEntity response = ResponseEntity.status(403).body(enterpriseErrorVO);
        return response;
    }

    /*
    handles generic valiation or service exceptions needed to restrict desired functionality
     */
    private ResponseEntity getResponseForBaseException(EnterpriseErrorVO enterpriseErrorVO, BaseException exception) {
        Object[] formattedArgs = null;

        if (!CollectionUtils.isEmpty(exception.getAllErrors())) {
            for (ErrorVO error : exception.getAllErrors()) {
                Object[] args = error.getArguments();
                if (args != null && args.length > 0) {
                    formattedArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        Object obj = args[i];
                        formattedArgs[i] = messageSource.getMessage(obj.toString(), null, obj.toString(), Locale.getDefault());
                    }

                }

                if (error.getMessage() == null) {
                    error.setMessage(messageSource.getMessage(error.getErrorCode(), formattedArgs, error.getErrorCode(),
                            Locale.getDefault()));
                }

            }
        }
        enterpriseErrorVO.setErrors(exception.getAllErrors());
        ResponseEntity response = ResponseEntity.unprocessableEntity().body(enterpriseErrorVO);
        return response;
    }

    /*
    generic exception in case all exceptions are passed
     */
    private ResponseEntity getResponseForGenericException(EnterpriseErrorVO enterpriseErrorVO, Throwable th) {
        List<ErrorVO> errorList = new ArrayList<>(1);

        // In case of any generic exception show a custom technical error to user
        ErrorVO errorVO = new ErrorVO();
        errorVO.setMessage(messageSource.getMessage("internal.server.error", null, Locale.getDefault()));
        errorList.add(errorVO);

        enterpriseErrorVO.setErrors(errorList);

        ResponseEntity response = ResponseEntity.status(500).body(enterpriseErrorVO);
        return response;

    }
}
