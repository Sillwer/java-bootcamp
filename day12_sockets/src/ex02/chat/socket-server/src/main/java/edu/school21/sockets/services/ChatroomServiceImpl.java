package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChatroomServiceImpl implements ChatroomService {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Override
    public void createRoom(Chatroom entity) {
        Optional<Chatroom> existsChat = chatroomRepository.findByName(entity.getName());
        if (existsChat.isPresent()) {
            throw new RuntimeException("Chat with name \"" + entity.getName() + "\" already exists");
        }

        chatroomRepository.save(entity);
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        return chatroomRepository.findById(id);
    }

    @Override
    public List<Chatroom> findAll() {
        return chatroomRepository.findAll();
    }

    @Override
    public void update(Chatroom entity) {
        Optional<Chatroom> doubleName = chatroomRepository.findByName(entity.getName());
        if (doubleName.isPresent() && !Objects.equals(doubleName.get().getId(), entity.getId())) {
            throw new RuntimeException("Chat with name \"" + entity.getName() + "\" already exists");
        }

        chatroomRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        chatroomRepository.delete(id);
    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        return chatroomRepository.findByName(name);
    }

    @Override
    public Optional<Chatroom> findByOwnerId(Long id) {
        return chatroomRepository.findById(id);
    }
}
