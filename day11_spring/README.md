# Java basics - day 11
### Spring

# Contents
1. [Exercise 00 — Spring Context](#exercise-00--spring-context)
2. [Exercise 01 — JdbcTemplate](#exercise-01--jdbctemplate)
3. [Exercise 02 — AnnotationConfig](#exercise-02--annotationconfig)

### General Rules
- There is only one Java version for you, 1.8. Make sure you have the compiler and interpreter for this version installed on your machine.
- The code is more often read than written. Maintain code formatting based on Oracle standards.
- Use System.out for output.

### Exercise 00 — Spring Context

Exercise 00: Spring Context ||
---|---
Turn-in directory | ex00
Files to turn-in |  Spring-folder

Let's implement a loosely coupled system consisting of a set of components (beans) and conforming to IoC/DI principles.

Let's say there is a Printer interface designed to display a specific message.

This class has two implementations: PrinterWithDateTimeImpl and PrinterWithPrefixImpl. The first class prints messages by specifying the output date/time using LocalDateTime, while the second class can be used to set a text prefix for a message.

In turn, both printer implementations have a dependency on the Renderer interface, which sends messages to the console. Renderer also has two implementations: RendererStandardImpl (outputs a message via the standard System.out) and RendererErrImpl (outputs messages via System.err).

Renderer also has a dependency on the PreProcessor interface, which preprocesses messages. The implementation of PreProcessorToUpperImpl translates all letters to upper case, while the implementation of PreProcessorToLower translates all letters to lower case.

The UML diagram of the classes is shown below:

![Diagram of classes](misc/images/Diagram.png)

An example of code using these classes in a standard way:
```java
public class Main {
   public static void main(String[] args) {
       PreProcessor preProcessor = new PreProcessorToUpperImpl();
       Renderer renderer = new RendererErrImpl(preProcessor);
       PrinterWithPrefixImpl printer = new PrinterWithPrefixImpl(renderer);
       printer.setPrefix("Prefix");
       printer.print("Hello!");
   }
}
```
Running this code will deliver the following result:

```
PREFIX HELLO!
```
You need to describe context.xml file for Spring, where all settings for each component and links between them will be specified.

Using these components with Spring looks as follows:
```java
public class Main {
   public static void main(String[] args) {
       ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
       Printer printer = context.getBean(“printerWithPrefix”, Printer.class);
       printer.print("Hello!");
   }
}
```

### Exercise 01 — JdbcTemplate

Exercise 01: JdbcTemplate ||
---|---
Turn-in directory | ex01
Files to turn-in |  Service-folder

JdbcTemplate and its extension NamedParameterJdbcTemplate are convenient mechanisms for working with databases. These classes eliminate the need to write template code for query execution and processing, as well as the need to catch exceptions during check.

In addition, they provide a convenient RowMapper concept for ResultSet processing and converting resulting tables to objects.

Now you need to implement the User model with the following fields
- Identifier;
- Email.

You also need to implement the `CrudRepository<T>` interface with the following methods
- `Optional<T>` findById(Long id)
- `List<T>` findAll()
- void save(T entity)
- void update(T entity)
- void delete(Long id)

The UsersRepository interface declared as UsersRepository extends CrudRepository<User> must contain the following method:
- `Optional<T>` findByEmail(String email)

In addition, two implementations of UsersRepository are required: UsersRepositoryJdbcImpl (uses standard Statements mechanisms) and UsersRepositoryJdbcTemplateImpl (based on JdbcTemplate/NamedParameterJdbcTemplate). Both classes accept DataSource object as constructor argument.

In the context.xml file, beans with different identifiers must be declared for both repository types, as well as two beans of DataSource type: DriverManagerDataSource and HikariDataSource.

In addition, the data for connecting to the DB must be specified in the db.properties file and included in the context.xml using `${db.url}` placeholders.

Example of db.properties:
```
db.url=jdbc:postgresql://localhost:5432/database
db.user=postgres
db.password=qwerty007
db.driver.name=org.postgresql.Driver
```

In Main class, operation of findAll method shall be demonstrated using both repositories:
```
ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
UsersRepository usersRepository = context.getBean("usersRepositoryJdbc", UsersRepository.class);
System.out.println(usersRepository.findAll());
usersRepository = context.getBean("usersRepositoryJdbcTemplate", UsersRepository.class);
System.out.println(usersRepository.findAll());
```

**Project structure**:
- Service
    - src
        - main
            - java
                - school21.spring.service
                    - models
                        - User
                    - repositories
                        - CrudRepository
                        - UsersRepository
                        - UsersRepositoryJdbcImpl
                        - UsersRepositoryJdbcTemplateImpl
                    - application
                        - Main
            - resources
                -   db.properties
                -   context.xml
    -   pom.xml

### Exercise 02 — AnnotationConfig

Exercise 02: AnnotationConfig ||
---|---
Turn-in directory | ex02
Files to turn-in |  Service-folder

Now you need to configure the configuration mechanisms of Spring applications using annotations. To do this, use the configuration class marked as @Configuration. Within this class, you need to describe beans for connecting to DataSource DB using @Bean annotation. As in the previous task, the connection data must be located in the db.properties file. You also need to make sure that context.xml does not exist.

Also implement UsersService/UsersServiceImpl interface/class pair with a dependency on UsersRepository declared in it. Insertion of the correct repository bean must be implemented using @Autowired annotation (similarly, you must bind DataSource inside repositories). Collisions in automatic binding are resolved with @Qualifier annotation.

Beans for UsersService and UsersRepository are defined using @Component annotation.

In UsersServiceImpl, implement a String signUp(String email) method that registers a new user and stores their details in the DB. This method returns a temporary password assigned to the user by the system (this information should also be stored in the database).

To verify that your service works correctly, implement an integration test for UsersServiceImp using an in-memory database (H2 or HSQLDB). The context configuration for the test environment (DataSource for the in-memory database) is described in a separate TestApplicationConfig class. This test checks whether a temporary password was returned in the signUp method.

**Project structure**:
- Service
    - src
        - main
            - java
                - school21.spring.service
                    - config
                        - ApplicationConfig
                    - models
                        - User
                    - repositories
                        - CrudRepository
                        - UsersRepository
                        - UsersRepositoryJdbcImpl
                        - UsersRepositoryJdbcTemplateImpl
                    - services
                        - UsersService
                        - UsersServiceImpl
                    - application
                        - Main
            - resources
                -   db.properties
        - test
            - java
                - school21.spring.service
                    - config
                        - TestApplicationConfig
                    - services
                        - UsersServiceImplTest
    -   pom.xml
