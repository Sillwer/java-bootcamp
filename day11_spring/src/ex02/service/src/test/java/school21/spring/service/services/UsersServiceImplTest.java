package school21.spring.service.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.TestApplicationConfig;

import static org.junit.jupiter.api.Assertions.*;

public class UsersServiceImplTest {
    ConfigurableApplicationContext applicationContext;
    UsersService usersService;

    @BeforeEach
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
        usersService = applicationContext.getBean(UsersService.class);
    }

    @AfterEach
    public void destroy() {
        applicationContext.close();
    }

    @Test
    public void existsEmailSignUpForUsersService() {
        assertNull(usersService.signUp("admin@mail.ru"));
    }

    @Test
    public void newEmailSignUpForUsersService() {
        assertEquals("newGeneratedPassword", usersService.signUp("foo@mail.ru"));
    }
}
