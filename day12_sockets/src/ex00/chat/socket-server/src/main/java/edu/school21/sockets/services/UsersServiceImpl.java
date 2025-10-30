package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean signUp(User user) {
        Optional<User> u = usersRepository.findByUserName(user.getUserName());
        if (u.isPresent()) {
            throw new RuntimeException("User with name [" + user.getUserName() + "] already exists");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("new user saving error: ", e);
        }

        return true;
    }

    @Override
    public boolean signIn(User user) {
        Optional<User> foundUser = usersRepository.findByUserName(user.getUserName());
        if (foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            user.setId(foundUser.get().getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(User user) {
        usersRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return usersRepository.findByUserName(userName);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public void update(User user) {
        usersRepository.update(user);
    }

    @Override
    public void delete(Long id) {
        usersRepository.delete(id);
    }
}
