package com.example.todoapi.v1.repository;

import static org.springframework.data.domain.Sort.Order.*;
import static org.springframework.data.relational.core.query.Criteria.*;
import static org.springframework.data.relational.core.query.Query.*;
import static org.springframework.data.relational.core.query.Update.*;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.todoapi.v1.entity.Todo;
import com.example.todoapi.v1.enums.TodoPerformStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TodoQueryRepository {
	private final R2dbcEntityTemplate template;

	public Flux<Todo> findByTodoPerformStatus(List<TodoPerformStatus> performStatusList, boolean isUpdatedAtDesc) {
		// 동적 쿼리가 되지 않는 것은 좀 아쉬운 점이다..
		return template.select(Todo.class)
			.matching(query(where("perform_status").in(performStatusList)).sort(
				isUpdatedAtDesc ? Sort.by(Sort.Direction.DESC, "updated_at") :
					Sort.by(Sort.Direction.ASC, "updated_at"))).all();
	}

	public Mono<Integer> updateAllTodoByPerformStatus(TodoPerformStatus performStatus) {
		return template.update(Todo.class)
			.apply(update("perform_status", performStatus));
	}
}
