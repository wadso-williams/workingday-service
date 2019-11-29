package com.williams.workingdayservice.controller;

import com.williams.workingdayservice.dto.ErrorResponse;
import com.williams.workingdayservice.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

//TODO - Documentation
public class AbstractGlobalExceptionAdvice {
    private static final HttpStatus DEFAULT_ERROR_STATUS;
    private static final HttpStatus USER_INPUT_ERROR_STATUS;
    private static final HttpStatus DEFAULT_VALIDATION_ERROR_STATUS;
    protected final Logger log;
    private Properties validationMessageProperties;

    public AbstractGlobalExceptionAdvice(Logger log) {
        this.log = log;
        this.loadValidationMessageProperties();
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorResponse> defaultHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), DEFAULT_ERROR_STATUS.value());
        return this.logAndGenerateResponse(error, ex);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> accessDeniedHandler(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return this.logAndGenerateResponse(error, ex);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException ex) {
        ErrorResponse error = new ErrorResponse(String.format("Validation failed. %d error(s)", ex.getConstraintViolations().size()), DEFAULT_VALIDATION_ERROR_STATUS.value());
        ex.getConstraintViolations().forEach((v) -> {
            error.addError(this.loadErrorMessageFromViolation(v));
            Map<String, Object> annotationAttributes = v.getConstraintDescriptor() != null ? v.getConstraintDescriptor().getAttributes() : Collections.emptyMap();
            error.setCode(annotationAttributes.containsKey("responseCode") ? HttpStatus.valueOf(Integer.valueOf((String)annotationAttributes.get("responseCode"))).value() : DEFAULT_VALIDATION_ERROR_STATUS.value());
        });
        return this.logAndGenerateResponse(error);
    }

    @ExceptionHandler({MultipartException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), USER_INPUT_ERROR_STATUS.value());
        return this.logAndGenerateResponse(error, ex);
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMissingRequestPart(MissingServletRequestPartException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), USER_INPUT_ERROR_STATUS.value());
        return this.logAndGenerateResponse(error, ex);
    }

    @ExceptionHandler({ApiException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleApiException(ApiException apiException) {
        ErrorResponse error = new ErrorResponse(apiException.getMessage(), DEFAULT_ERROR_STATUS.value());
        return this.logAndGenerateResponse(error, apiException);
    }

    protected ResponseEntity<ErrorResponse> logAndGenerateResponse(ErrorResponse error, Exception ex) {
        this.logRestError(error, ex);
        return new ResponseEntity(error, HttpStatus.valueOf(error.getCode()));
    }

    protected ResponseEntity<ErrorResponse> logAndGenerateResponse(ErrorResponse error) {
        this.logRestError(error);
        return new ResponseEntity(error, HttpStatus.valueOf(error.getCode()));
    }

    private void logRestError(ErrorResponse error) {
        String errorMessage = String.join(" ", error.getErrors());
        if (error.getCode() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            this.log.error("Exception occurred with http code: {}, error: {}, reason: {}", new Object[]{HttpStatus.valueOf(error.getCode()), error.getMessage(), errorMessage});
        } else {
            this.log.warn("Exception occurred with http code: {}, error: {}, reason: {}", new Object[]{HttpStatus.valueOf(error.getCode()), error.getMessage(), errorMessage});
        }

    }

    private void logRestError(ErrorResponse error, Exception ex) {
        String errorMessage = String.join(" ", error.getErrors());
        if (error.getCode() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            this.log.error("Exception occurred with http code: {}, error: {}, reason: {}", new Object[]{HttpStatus.valueOf(error.getCode()), error.getMessage(), errorMessage});
            this.log.error("Exception: ", ex);
        } else {
            this.log.warn("Exception occurred with http code: {}, error: {}, reason: {}", new Object[]{HttpStatus.valueOf(error.getCode()), error.getMessage(), errorMessage});
            this.log.debug("Exception: ", ex);
        }

    }

    private void loadValidationMessageProperties() {
        this.validationMessageProperties = new Properties();

        try {
            this.validationMessageProperties.load(NotNullValidator.class.getResourceAsStream("/org/hibernate/validator/ValidationMessages.properties"));
        } catch (IOException var2) {
            this.log.error("Exception occured while loading hibernate message messages: {}", var2);
        }

    }

    private String loadErrorMessageFromViolation(ConstraintViolation<?> v) {
        if (!this.validationMessageProperties.containsValue(v.getMessage()) || v.getConstraintDescriptor() == null || !v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class) && !v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)) {
            return v.getMessage();
        } else {
            String field = StringUtils.substringAfterLast(v.getPropertyPath().toString(), ".");
            return field + " " + v.getMessage();
        }
    }

    static {
        DEFAULT_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
        USER_INPUT_ERROR_STATUS = HttpStatus.BAD_REQUEST;
        DEFAULT_VALIDATION_ERROR_STATUS = HttpStatus.BAD_REQUEST;
    }
}