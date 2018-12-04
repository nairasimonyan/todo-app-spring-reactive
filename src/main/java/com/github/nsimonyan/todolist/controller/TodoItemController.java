package com.github.nsimonyan.todolist.controller;

import com.github.nsimonyan.todolist.model.TodoItem;
import com.github.nsimonyan.todolist.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class TodoItemController {

    @Autowired
    private TodoListService service;

    @GetMapping("/todo")
    public Flux<TodoItem> getAllTodoItems() {
        return service.getAllTodoItems();
    }

    @PostMapping("/todo")
    public Mono<TodoItem> createTodoItems(@Valid @RequestBody TodoItem todo) {
        return service.createTodoItems(todo);
    }

    @GetMapping("/todo/{id}")
    public Mono<ResponseEntity<TodoItem>> getTodoItemById(@PathVariable(value = "id") String todoId) {
        return service.getTodoItemById(todoId)
                .map(savedTodoItem -> ResponseEntity.ok(savedTodoItem))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/todo/{id}")
    public Mono<ResponseEntity<TodoItem>> updateTodoItem(@PathVariable(value = "id") String todoId, @Valid @RequestBody TodoItem todo) {
        return service.updateTodoItem(todoId, todo)
                .map(updatedTodoItem -> new ResponseEntity<>(updatedTodoItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/todo/{id}")
    public Mono<ResponseEntity<Void>> deleteTodoItem(@PathVariable(value = "id") String todoId) {
        return service.deleteTodoItem(todoId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}