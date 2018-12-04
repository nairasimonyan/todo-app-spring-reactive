package com.github.nsimonyan.todolist;

import com.github.nsimonyan.todolist.model.TodoItem;
import com.github.nsimonyan.todolist.repository.TodoItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class TodoListApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Test
	public void testCreateTodoItem() {
		String todoText = "Learn AKKA Framework";
		boolean isDone = false;

		TodoItem todo = new TodoItem(todoText, isDone);

		webTestClient.post().uri("/todo")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(todo), TodoItem.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.text").isEqualTo(todoText)
				.jsonPath("$.done").isEqualTo(isDone);
	}

	@Test
	public void testGetAllTodoItems() {
		webTestClient.get().uri("/todo")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(TodoItem.class);
	}

	@Test
	public void testGetSingleTodoItem() {
		TodoItem todo = todoItemRepository.save(new TodoItem("Hello, World!", false)).block();

		webTestClient.get()
				.uri("/todo/{id}", Collections.singletonMap("id", todo.getId()))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void testUpdateTodo() {
		TodoItem todo = todoItemRepository.save(new TodoItem("Initial Task", false)).block();

		TodoItem newTodoData = new TodoItem("I did it after all...", true);

		webTestClient.put()
				.uri("/todo/{id}", Collections.singletonMap("id", todo.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(newTodoData), TodoItem.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.text").isEqualTo("I did it after all...")
				.jsonPath("$.done").isEqualTo(true);
	}

	@Test
	public void testDeleteTodo() {
		TodoItem todo = todoItemRepository.save(new TodoItem("To be deleted", true)).block();

		webTestClient.delete()
				.uri("/todo/{id}", Collections.singletonMap("id",  todo.getId()))
				.exchange()
				.expectStatus().isOk();
	}

}
