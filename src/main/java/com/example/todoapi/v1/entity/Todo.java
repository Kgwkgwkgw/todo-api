package com.example.todoapi.v1.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import com.example.todoapi.v1.dto.TodoCreateReq;
import com.example.todoapi.v1.dto.TodoUpdateReq;
import com.example.todoapi.v1.enums.TodoPerformStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Todo {
	@Id
	private Long id;
	private String desc;
	private TodoPerformStatus performStatus;
	private LocalDateTime updatedAt;

	public Todo(TodoCreateReq todoCreateReq) {
		this.desc = todoCreateReq.getDesc();
		this.performStatus = TodoPerformStatus.ACTIVE;
		this.updatedAt = LocalDateTime.now();
	}
	public void update(TodoUpdateReq todoUpdateReq) {
		if (todoUpdateReq != null) {
			if (todoUpdateReq.getDesc() != null) {
				this.desc = todoUpdateReq.getDesc();
			}
			if (todoUpdateReq.getPerformStatus() != null) {
				this.performStatus = todoUpdateReq.getPerformStatus();
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Todo todo = (Todo)o;
		return Objects.equals(id, todo.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
