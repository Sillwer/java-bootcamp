package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message> {
    public List<Message> findByChatRoom(Long id);
}
