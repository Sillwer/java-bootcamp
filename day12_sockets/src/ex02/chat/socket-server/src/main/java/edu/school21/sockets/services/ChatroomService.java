package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;

import java.util.List;
import java.util.Optional;

public interface ChatroomService {
    void createRoom(Chatroom entity);

    Optional<Chatroom> findById(Long id);

    List<Chatroom> findAll();

    void update(Chatroom entity);

    void delete(Long id);

    Optional<Chatroom> findByName(String name);

    Optional<Chatroom> findByOwnerId(Long id);
}
