package com.example.todoapi.v1.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapi.v1.dto.TodoCreateReq;
import com.example.todoapi.v1.dto.TodoUpdateReq;
import com.example.todoapi.v1.entity.Todo;
import com.example.todoapi.v1.enums.TodoPerformStatus;
import com.example.todoapi.v1.service.TodoService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/todo")
@RequiredArgsConstructor
public class TodoController {
	private final TodoService todoService;

	@PostMapping
	public Mono<Todo> createTodo(@RequestBody TodoCreateReq req) {
		return todoService.createTodo(req);
	}

	@GetMapping
	public Flux<Todo> readTodoByTodoPerformStatus(
		@RequestParam(required = false, defaultValue = "ACTIVE") TodoPerformStatus performStatus,
		@RequestParam(defaultValue = "true") boolean isUpdatedAtDesc) {
		return todoService.readTodoByTodoPerformStatus(performStatus, isUpdatedAtDesc);
	}
u
	@DeleteMapping("/{id}")
	public Mono<Void> removeTodoById(@PathVariable Long id) {
		return this.todoService.removeTodoById(id);
	}

	@PatchMapping("/set-all-todo-status")
	public Mono<Void> setAllTodoStatusByPerformStatus(
		@RequestParam(required = false, defaultValue = "ACTIVE") TodoPerformStatus performStatus) {
		return this.todoService.setAllTodoStatusByPerformStatus(performStatus)
			.flatMap(integer -> Mono.empty());
	}

	@PatchMapping("/{id}")
	public Mono<Todo> updateTodoById(
		@PathVariable Long id,
		@RequestBody TodoUpdateReq todoUpdateReq) {
		return this.todoService.updateTodoById(id, todoUpdateReq);
	}
}
