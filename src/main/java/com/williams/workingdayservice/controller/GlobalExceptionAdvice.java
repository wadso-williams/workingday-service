package com.williams.workingdayservice.controller;

import com.williams.workingdayservice.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionAdvice extends AbstractGlobalExceptionAdvice {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionAdvice.class);
	private final String missingParameter;
	private final String afterDateNotEnoughData;

	@Autowired
	public GlobalExceptionAdvice(@Value("${missing.required.parameter}") String missingParameter,
								 @Value("${after-date-not-enough-data}") String afterDateNotEnoughData) {
		super(LOG);
		this.missingParameter = missingParameter;
		this.afterDateNotEnoughData = afterDateNotEnoughData;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
		StringBuilder message = new StringBuilder();
		if (hmnre.getRootCause() != null) {
			message.append(hmnre.getRootCause().getMessage());
		} else {
			message.append(hmnre.getMessage());
		}

		ErrorResponse errorDto = new ErrorResponse(message.toString(), HttpStatus.BAD_REQUEST.value());
		LOG.error(errorDto.toString());
		return errorDto;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorResponse missingParameterExceptionHandler(MissingServletRequestParameterException exception) {
		ErrorResponse errorDto = new ErrorResponse(String.format(missingParameter, exception.getParameterName()),
				HttpStatus.BAD_REQUEST.value());
		LOG.error(errorDto.toString());
		return errorDto;
	}

	@ExceptionHandler(HttpClientErrorException.Conflict.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public @ResponseBody
	ErrorResponse conflictExceptionHandler() {
		ErrorResponse errorDto = new ErrorResponse(afterDateNotEnoughData, HttpStatus.CONFLICT.value());
		LOG.error(errorDto.toString());
		return errorDto;
	}
}