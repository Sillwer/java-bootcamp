package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    static final Long ID_CORRECT = 1L;
    static final Long ID_WRONG = 666L;
    static final String USERNAME_CORRECT = "Mallarae";
    static final String USERNAME_WRONG = "CatgirlWife";
    static final String PASSWORD_CORRECT = "12345678";
    static final String PASSWORD_WRONG = "PV0j~K@siFkc8fL*";

    static User USER_CORRECT;
    static User USER_WRONG_ID;


    UsersRepository mockUsersRepository;
    UsersServiceImpl usersService;

    @BeforeEach
    void init() throws EntityNotFoundException, AlreadyAuthenticatedException {
        mockUsersRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(mockUsersRepository);

        USER_CORRECT = new User(ID_CORRECT, USERNAME_CORRECT, PASSWORD_CORRECT, false);
        USER_WRONG_ID = new User(ID_WRONG, USERNAME_CORRECT, PASSWORD_CORRECT, false);

        when(mockUsersRepository.findByLogin(USERNAME_WRONG)).thenThrow(EntityNotFoundException.class);
        when(mockUsersRepository.findByLogin(USERNAME_CORRECT)).thenReturn(USER_CORRECT);

        doThrow(EntityNotFoundException.class).when(mockUsersRepository).update(USER_WRONG_ID);
        doNothing().when(mockUsersRepository).update(USER_CORRECT);
    }

    @Test
    void wrongLoginForUsersServiceImplTest() throws EntityNotFoundException {
        assertThrowsExactly(EntityNotFoundException.class,
                () -> usersService.authenticate(USERNAME_WRONG, ""));

        verify(mockUsersRepository).findByLogin(USERNAME_WRONG);
    }

    @Test
    void alreadyAuthorizedForUsersServiceImplTest() throws EntityNotFoundException {
        User user = USER_CORRECT;
        user.setAuthorized(true);

        assertThrowsExactly(AlreadyAuthenticatedException.class,
                () -> usersService.authenticate(USERNAME_CORRECT, PASSWORD_CORRECT));

        verify(mockUsersRepository).findByLogin(user.getLogin());
    }

    @Test
    void wrongPasswordForUsersServiceImplTest() throws EntityNotFoundException, AlreadyAuthenticatedException {
        User user = USER_CORRECT;

        assertFalse(usersService.authenticate(user.getLogin(), PASSWORD_WRONG));

        verify(mockUsersRepository).findByLogin(user.getLogin());
    }

    @Test
    void cantUpdateDatabaseForUsersServiceImplTest() throws EntityNotFoundException, AlreadyAuthenticatedException {
        // заглушка: на корректное имя выдаем пользователя с несуществующим ID
        when(mockUsersRepository.findByLogin(USERNAME_CORRECT)).thenReturn(USER_WRONG_ID);

        assertThrowsExactly(EntityNotFoundException.class,
                () -> usersService.authenticate(USERNAME_CORRECT, PASSWORD_CORRECT));

        verify(mockUsersRepository).findByLogin(USERNAME_CORRECT);
        verify(mockUsersRepository).update(USER_WRONG_ID);
    }

    @Test
    void okForUsersServiceImplTest() throws EntityNotFoundException, AlreadyAuthenticatedException {
        User user = USER_CORRECT;

        assertTrue(usersService.authenticate(USERNAME_CORRECT, PASSWORD_CORRECT));

        verify(mockUsersRepository).findByLogin(user.getLogin());
        verify(mockUsersRepository).update(user);
    }
}
