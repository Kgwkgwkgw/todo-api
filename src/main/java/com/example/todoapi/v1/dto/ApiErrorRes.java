package com.example.todoapi.v1.dto;

import java.util.Map;

import com.example.todoapi.v1.enums.ApiError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiErrorRes {
	private ApiError errorCode;
	private Map<String, Object> extra;
}
