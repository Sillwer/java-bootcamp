package school21.spring.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {
    UsersRepository<User> usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplateImpl") UsersRepository<User> usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signUp(String email) {
        Optional<User> existsUser = usersRepository.findByEmail(email);
        if (existsUser.isPresent()) {
            return null;
        }

        String password = "newGeneratedPassword";

        User newUser = new User(null, email, password);
        usersRepository.save(newUser);
        if (newUser.getIdentifier() == null) {
            return null;
        }

        return newUser.getPassword();
    }
}
