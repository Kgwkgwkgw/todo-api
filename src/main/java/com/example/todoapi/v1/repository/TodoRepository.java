package com.example.todoapi.v1.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.todoapi.v1.entity.Todo;

public interface TodoRepository extends ReactiveCrudRepository<Todo, Long> {
}
