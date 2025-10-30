package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    void save(Message entity);

    Optional<Message> findById(Long id);

    List<Message> findAll();

    void update(Message entity);

    void delete(Long id);

    public List<Message> findByChatRoom(Long id);
}
