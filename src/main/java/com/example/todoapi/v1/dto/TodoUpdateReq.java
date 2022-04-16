package com.example.todoapi.v1.dto;

import com.example.todoapi.v1.enums.TodoPerformStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateReq {
	private String desc;
	private TodoPerformStatus performStatus;
}
