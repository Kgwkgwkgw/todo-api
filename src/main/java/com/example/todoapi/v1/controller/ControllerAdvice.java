package com.example.todoapi.v1.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.todoapi.v1.dto.ApiErrorRes;
import com.example.todoapi.v1.enums.ApiError;
import com.example.todoapi.v1.exception.NotFoundException;

@RestControllerAdvice
public class ControllerAdvice {
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrorRes serverExceptionHandler(DataIntegrityViolationException ex) {
		return ApiErrorRes.builder().errorCode(ApiError.DUPLICATE).build();
	}
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorRes serverExceptionHandler(NotFoundException ex) {
		return ApiErrorRes.builder().errorCode(ApiError.NOT_FOUND).build();
	}
}
