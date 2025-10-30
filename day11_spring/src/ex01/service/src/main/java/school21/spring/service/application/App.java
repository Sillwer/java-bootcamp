package school21.spring.service.application;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");

        String[] repoImplNames = {"UsersRepositoryJdbc", "usersRepositoryJdbcTemplate"};

        for (String repoImplName : repoImplNames) {
            UsersRepository<User> repo = applicationContext.getBean(repoImplName, UsersRepository.class);

            System.out.println("\n>>>>>>>>>>> " + repo.getClass().getSimpleName());

            System.out.println("Find by id = 2: " + repo.findById(2L));

            User newUser = new User(null, "admin@mail.ru");
            repo.save(newUser);
            System.out.println("Saved: " + newUser);

            newUser.setEmail("Prefix_" + newUser.getEmail());
            repo.update(newUser);
            System.out.println("Updated: " + repo.findById(newUser.getIdentifier()));

            System.out.println("Find by email = Prefix_admin@mail.ru: " + repo.findByEmail("Prefix_admin@mail.ru"));

            System.out.println("Find all:");
            for (User u : repo.findAll()) {
                System.out.println("\t" + u);
            }
        }

        System.out.println();
        applicationContext.close();
    }
}
