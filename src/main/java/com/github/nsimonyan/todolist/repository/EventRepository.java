package com.github.nsimonyan.todolist.repository;

import com.github.nsimonyan.todolist.model.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, String> {
    @Tailable
    Flux<Event> findWithTailableCursorBy();
}
