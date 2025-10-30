package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;

import java.util.Optional;

public interface ChatroomRepository extends CrudRepository<Chatroom> {
    Optional<Chatroom> findByName(String name);

    Optional<Chatroom> findByOwnerId(Long id);
}
