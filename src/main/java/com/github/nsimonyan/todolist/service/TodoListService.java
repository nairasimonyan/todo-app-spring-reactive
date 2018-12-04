package com.github.nsimonyan.todolist.service;

import com.github.nsimonyan.todolist.model.Event;
import com.github.nsimonyan.todolist.model.TodoItem;
import com.github.nsimonyan.todolist.repository.EventRepository;
import com.github.nsimonyan.todolist.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TodoListService {
    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private EventRepository eventRepository;

    public Flux<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    public Mono<TodoItem> createTodoItems(TodoItem todo) {
        return createEvent("CREATED TODO ITEM: " + todo.getText()).
                then(todoItemRepository.save(todo));
    }

    public Mono<TodoItem> getTodoItemById(@PathVariable(value = "id") String todoId) {
        return todoItemRepository.findById(todoId);
    }

    public Mono<TodoItem> updateTodoItem(String todoId, TodoItem todo) {
        return todoItemRepository.findById(todoId)
                .flatMap(existingTodoItem -> {
                    String eventText = "";

                    if(!existingTodoItem.getText().equals(todo.getText())) {
                        existingTodoItem.setText(todo.getText());
                        eventText = "RENAMED TODO ITEM TO " + todo.getText();
                    }

                    if(existingTodoItem.isDone() != todo.isDone()) {
                        existingTodoItem.setDone(todo.isDone());

                        if(!eventText.isEmpty()) eventText += " AND ";

                        eventText += todo.isDone() ? "COMPLETED " : "NOT COMPLETED ";
                        eventText +=  "TODO ITEM: " + todo.getText();
                    }

                    return createEvent(eventText).
                            then(todoItemRepository.save(existingTodoItem));
                });
    }

    public Mono<Void> deleteTodoItem(String todoId) {

        return todoItemRepository.findById(todoId)
                .flatMap(existingTodoItem ->
                        createEvent("DELETED TODO ITEM: "+ existingTodoItem.getText()).
                                then(todoItemRepository.delete(existingTodoItem))

                );
    }

    private Mono<Event> createEvent(String eventText) {
        return eventRepository.save( new Event(eventText));
    }
}
