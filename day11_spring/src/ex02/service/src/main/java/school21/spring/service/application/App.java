package school21.spring.service.application;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.ApplicationConfig;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;
import school21.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;

public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        UsersRepository<User> repo = applicationContext.getBean(UsersRepositoryJdbcTemplateImpl.class);

        System.out.println("Find all:");
        for (User u : repo.findAll()) {
            System.out.println("\t" + u);
        }

        System.out.println();
        applicationContext.close();
    }
}
