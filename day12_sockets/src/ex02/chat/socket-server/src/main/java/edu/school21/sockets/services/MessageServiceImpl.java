package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void save(Message entity) {
        messageRepository.save(entity);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(Message entity) {
        messageRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        messageRepository.delete(id);
    }

    @Override
    public List<Message> findByChatRoom(Long id) {
        return messageRepository.findByChatRoom(id);
    }
}
