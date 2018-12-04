package com.github.nsimonyan.todolist.socket;

import com.github.nsimonyan.todolist.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component("EventsStreamSocketHandler")
public class EventsStreamSocketHandler implements WebSocketHandler {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(eventRepository.findWithTailableCursorBy()
          .map(event -> event.getCreatedAt() + " - " + event.getText() )
          .map(webSocketSession::textMessage))
          .and(webSocketSession.receive()
          .map(WebSocketMessage::getPayloadAsText).log());
    }
}
