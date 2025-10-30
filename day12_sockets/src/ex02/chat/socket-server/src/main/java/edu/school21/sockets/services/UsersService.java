package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    boolean signUp(User user);

    boolean signIn(User user);

    void save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

    List<User> findAll();

    void update(User user);

    void delete(Long id);
}
