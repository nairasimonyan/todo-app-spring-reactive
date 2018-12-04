package com.github.nsimonyan.todolist.repository;

import com.github.nsimonyan.todolist.model.TodoItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends ReactiveMongoRepository<TodoItem, String> { }
