package com.example.todoapi.v1.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoapi.v1.dto.TodoCreateReq;
import com.example.todoapi.v1.dto.TodoUpdateReq;
import com.example.todoapi.v1.entity.Todo;
import com.example.todoapi.v1.enums.TodoPerformStatus;
import com.example.todoapi.v1.exception.NotFoundException;
import com.example.todoapi.v1.repository.TodoQueryRepository;
import com.example.todoapi.v1.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {
	private final TodoQueryRepository todoQueryRepository;
	private final TodoRepository todoRepository;

	public Mono<Todo> createTodo(TodoCreateReq req) {
		Todo todo = new Todo(req);
		Mono<Todo> saved = todoRepository.save(todo);
		return saved;
	}

	// performStatus가 null일 때, 전체 조회함
	public Flux<Todo> readTodoByTodoPerformStatus(TodoPerformStatus performStatus, boolean isUpdatedAtDesc) {
		List<TodoPerformStatus> toSearchPerformStatusList = null;
		if (performStatus == null) {
			toSearchPerformStatusList = List.of(TodoPerformStatus.ACTIVE, TodoPerformStatus.COMPLETED);
		} else {
			toSearchPerformStatusList = List.of(performStatus);
		}
		return this.todoQueryRepository.findByTodoPerformStatus(toSearchPerformStatusList, isUpdatedAtDesc);
	}

	public Mono<Void> removeTodoById(Long id) {
		return this.todoRepository.deleteById(id);
	}

	public Mono<Todo> readTodoById(Long id) {
		return this.todoRepository.findById(id);
	}

	public Mono<Integer> setAllTodoStatusByPerformStatus(TodoPerformStatus performStatus) {
		return this.todoQueryRepository.updateAllTodoByPerformStatus(performStatus);
	}

	public Mono<Todo> updateTodoById(Long id, TodoUpdateReq todoUpdateReq) {
		return this.todoRepository.findById(id)
			.flatMap(todo -> {
					todo.update(todoUpdateReq);
					return this.todoRepository.save(todo);
				}
			)
			.switchIfEmpty(Mono.error(NotFoundException::new));
	}
}
