package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;

public class UsersServiceImpl {
    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    boolean authenticate(String login, String password) throws EntityNotFoundException, AlreadyAuthenticatedException {
        try {
            User user = usersRepository.findByLogin(login);

            if (user.getAuthorized()) {
                throw new AlreadyAuthenticatedException("User with login = " + login + " has been authenticated in the system");
            }

            if (!password.equals(user.getPassword())) {
                return false;
            }

            user.setAuthorized(true);
            usersRepository.update(user);
            return true;

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with login = " + login + " not found");
        }
    }
}
