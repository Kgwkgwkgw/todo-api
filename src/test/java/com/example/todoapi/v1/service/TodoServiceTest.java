package com.example.todoapi.v1.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoapi.v1.dto.TodoCreateReq;
import com.example.todoapi.v1.dto.TodoUpdateReq;
import com.example.todoapi.v1.entity.Todo;
import com.example.todoapi.v1.enums.TodoPerformStatus;
import com.example.todoapi.v1.exception.NotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

@SpringBootTest
// 아직 junit에서 r2dbc transactional을 제대로 지원하지 못함.
// @Transactional
public class TodoServiceTest {
	@Autowired
	private TodoService todoService;

	TodoCreateReq req1;
	TodoCreateReq req2;
	TodoCreateReq req3;

	@BeforeEach
	public void beforeEach() {
		req1 = TodoCreateReq.builder()
			.desc("개발 공부하기")
			.build();
		req2 = TodoCreateReq.builder()
			.desc("빨래하기")
			.build();
		req3 = TodoCreateReq.builder()
			.desc("영화보기")
			.build();
	}

	@Test
	public void 할일_생성_테스트() {
		Mono<Todo> createdTodo = todoService.createTodo(req1);
		Mono<Todo> foundTodo = createdTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		StepVerifier.create(foundTodo)
			.assertNext(todo -> {
				assertNotNull(todo);
				assertNotNull(todo.getId());
			})
			.verifyComplete();
	}

	@Test
	public void 존재하지_않는_식별자로_업데이트_시_NOT_FOUND_에러_발생() {
		TodoUpdateReq updateReq = TodoUpdateReq.builder()
			.desc("테스트")
			.build();
		Mono<Todo> updatedTodo = todoService.updateTodoById(new Random().nextLong(), updateReq);

		StepVerifier.create(updatedTodo)
			.expectError(NotFoundException.class);
	}

	@Test
	public void 투두_할일_업데이트_성공() {
		Mono<Todo> createdTodo = todoService.createTodo(req1);
		Mono<Todo> foundTodo = createdTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		String toUpdateDesc = "넷플릭스 보기";
		TodoUpdateReq updateReq = TodoUpdateReq.builder()
			.desc(toUpdateDesc)
			.build();
		Mono<Todo> updatedTodo = foundTodo.flatMap(todo -> todoService.updateTodoById(todo.getId(), updateReq));
		Mono<Todo> updatedFoundTodo = updatedTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		StepVerifier.create(updatedFoundTodo)
			.assertNext((todo) -> {
				assertEquals(todo.getDesc(), toUpdateDesc);
			});
	}

	@Test
	public void 투두_상태_업데이트_성공() {
		Mono<Todo> createdTodo = todoService.createTodo(req1);
		Mono<Todo> foundTodo = createdTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		TodoPerformStatus toUpdatePerformStatus = TodoPerformStatus.COMPLETED;
		TodoUpdateReq updateReq = TodoUpdateReq.builder()
			.performStatus(toUpdatePerformStatus)
			.build();
		Mono<Todo> updatedTodo = foundTodo.flatMap(todo -> todoService.updateTodoById(todo.getId(), updateReq));
		Mono<Todo> updatedFoundTodo = updatedTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		StepVerifier.create(updatedFoundTodo)
			.assertNext((todo) -> {
				assertEquals(todo.getPerformStatus(), toUpdatePerformStatus);
			}).verifyComplete();
	}

	@Test
	public void 투두_삭제() {
		Mono<Todo> createdTodo = todoService.createTodo(req1);
		Mono<Todo> foundTodo = createdTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));

		Mono<Todo> removedTodo = foundTodo.flatMap(todo -> {
			return todoService.removeTodoById(todo.getId()).thenReturn(todo);
		});
		Mono<Todo> removedFoundTodo = removedTodo.flatMap(todo -> todoService.readTodoById(todo.getId()));
		StepVerifier.create(removedFoundTodo)
			.expectError(NotFoundException.class);
	}

	@Test
	public void 투두_리스트_업데이트_시간_오름차순_조회() {
		Mono<Todo> created = todoService.createTodo(req1)
			.flatMap(todo -> todoService.createTodo(req2))
			.flatMap(todo -> todoService.createTodo(req3));

		Flux<Todo> foundTodoListByUpdatedDtAsc = created.flatMapMany(
			todo -> todoService.readTodoByTodoPerformStatus(null, false));

		StepVerifier.create(foundTodoListByUpdatedDtAsc)
			.expectNextMatches(todo -> todo.getDesc().equals(req1.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req2.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req3.getDesc()))
			.verifyComplete();
	}

	@Test
	public void 투두_리스트_업데이트_시간_내림차_순_조회() {
		Mono<Todo> created = todoService.createTodo(req1)
			.flatMap(todo -> todoService.createTodo(req2))
			.flatMap(todo -> todoService.createTodo(req3));

		Flux<Todo> foundTodoListByUpdatedDtDesc = created.flatMapMany(
			todo -> todoService.readTodoByTodoPerformStatus(null, true));

		StepVerifier.create(foundTodoListByUpdatedDtDesc)
			.expectNextMatches(todo -> todo.getDesc().equals(req3.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req2.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req1.getDesc()))
			.verifyComplete();
	}

	@Test
	public void 투두_ACTIVE_리스트_조회() {
		Mono<Todo> created = todoService.createTodo(req1)
			.flatMap(todo -> todoService.createTodo(req2))
			.flatMap(todo -> todoService.createTodo(req3));

		Flux<Todo> foundTodoListByUpdatedDtDesc = created.flatMapMany(
			todo -> todoService.readTodoByTodoPerformStatus(null, true));

		StepVerifier.create(foundTodoListByUpdatedDtDesc)
			.expectNextMatches(todo -> todo.getDesc().equals(req3.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req2.getDesc()))
			.expectNextMatches(todo -> todo.getDesc().equals(req1.getDesc()))
			.verifyComplete();
	}

	@Test
	public void 투두_COMPLETED_리스트_조회() {
		Mono<Todo> created1 = todoService.createTodo(req1);
		Mono<Todo> created2 = todoService.createTodo(req2);
		Mono<Todo> created3 = todoService.createTodo(req3);

		Flux<Todo> completedTodoList = Mono.zip(created1, created2, created3).flatMap(tuple -> {
			Long id1 = tuple.getT1().getId();
			Long id2 = tuple.getT2().getId();

			Mono<Todo> updatedTodo1 = todoService.updateTodoById(id1,
				TodoUpdateReq.builder().performStatus(TodoPerformStatus.COMPLETED).build());
			Mono<Todo> updatedTodo2 = todoService.updateTodoById(id2,
				TodoUpdateReq.builder().performStatus(TodoPerformStatus.COMPLETED).build());
			return Mono.zip(updatedTodo1, updatedTodo2);
		}).flatMapMany(tuple -> todoService.readTodoByTodoPerformStatus(TodoPerformStatus.COMPLETED, false));

		StepVerifier.create(completedTodoList)
			.expectNextCount(2L)
			.verifyComplete();
	}
}