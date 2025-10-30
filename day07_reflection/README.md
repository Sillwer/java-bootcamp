# Java basics - Day 07
### Reflection

*Takeaways: Today you will develop your own frameworks that use the reflection mechanism.*

# Contents
1. [Exercise 00 – Work with Classes](#exercise-00--work-with-classes)
2. [Exercise 01 – Annotations – SOURCE](#exercise-01--annotations--source)
3. [Exercise 02 – ORM](#exercise-02--orm)

### General Rules
- There is only one Java version for you, 1.8. Make sure you have the compiler and interpreter for this version installed on your machine.
- The code is more often read than written. Maintain code formatting based on Oracle standards.
- Use System.out for output.


### Exercise 00 – Work with Classes

Exercise 00: Work with Classes ||
---|---
Turn-in directory	| ex00
Files to turn-in |	Reflection-folder


Now you need to implement a Maven project that interacts with classes in your application. We need to create at least two classes, each with:
- private fields (supported types are String, Integer, Double, Boolean, Long);
- public methods;
- an empty constructor;
- a constructor with one parameter;
- toString() method.

You do not need to implement any get/set methods in this task. Newly created classes must be in a separate **classes** package (this package can be in other packages). Let's assume that the application has User and Car classes. The User class is described below:
```java
public class User {
   private String firstName;
   private String lastName;
   private int height;

   public User() {
       this.firstName = "Default first name";
       this.lastName = "Default last name";
       this.height = 0;
   }

   public User(String firstName, String lastName, int height) {
       this.firstName = firstName;
       this.lastName = lastName;
       this.height = height;
   }

   public int grow(int value) {
       this.height += value;
       return height;
   }

   @Override
   public String toString() {
       return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
               .add("firstName='" + firstName + "'")
               .add("lastName='" + lastName + "'")
               .add("height=" + height)
               .toString();
   }
}
```

The implemented application works as follows:
- Provide information about a class in a class package.
- Allow a user to create objects of a specified class with specific field values.
- Display information about the created class object.
- Call class methods.

An example of how the program works:

```
Classes:
  - User
  - Car
---------------------
Enter class name:
-> User
---------------------
fields:
	String firstName
	String lastName
	int height
methods:
	int grow(int)
---------------------
Let’s create an object.
firstName:
-> UserName
lastName:
-> UserSurname
height:
-> 185
Object created: User[firstName='UserName', lastName='UserSurname', height=185]
---------------------
Enter name of the field for changing:
-> firstName
Enter String value:
-> Name
Object updated: User[firstName='Name', lastName='UserSurname', height=185]
---------------------
Enter name of the method for call:
-> grow(int)
Enter int value:
-> 10
Method returned:
195
```

- If a method has more than one parameter, you must set values for each parameter.
- If the method is of void type, no return value line is displayed.
- In a program session, you can interact with only a single class, modify a single field of its object, and call a single method.
- You can use the throws operator.

### Exercise 01 – Annotations – SOURCE

Exercise 01: Annotations – SOURCE ||
---|---
Turn-in directory |	ex01
Files to turn-in |	Annotations-folder

Annotations allow you to store metadata directly in your code. Now your goal is to implement HtmlProcessor class (derived fromAbstractProcessor) that will process classes with special @HtmlForm and @Htmlnput annotations and generate HTML form code inside the target/classes folder after running mvn clean compile command. Let's say we have a UserForm class:
```java
@HtmlForm(fileName = “user_form.html”, action = “/users”, method = “post”)
public class UserForm {
	@HtmlInput(type = “text”, name = “first_name”, placeholder = “Enter First Name”)
	private String firstName;

	@HtmlInput(type = “text”, name = “last_name”, placeholder = “Enter Last Name”)
	private String lastName;
	
	@HtmlInput(type = “password”, name = “password”, placeholder = “Enter Password”)
	private String password;
}
```
Then, it shall be used as a base to generate "user_form.html" file with the following contents:

```HTML
<form action = "/users" method = "post">
	<input type = "text" name = "first_name" placeholder = "Enter First Name">
	<input type = "text" name = "last_name" placeholder = "Enter Last Name">
	<input type = "password" name = "password" placeholder = "Enter Password">
	<input type = "submit" value = "Send">
</form>
```

- @HtmlForm and @HtmlInput annotations are only available during compilation.
- The project structure is at the discretion of the developer.
- To handle annotations correctly, we recommend using special settings of maven-compiler-plugin and auto-service dependency on com.google.auto.service.

### Exercise 02 – ORM

Exercise 02: ORM ||
---|---
Turn-in directory	| ex02
Files to turn-in	| ORM-folder

We have already mentioned that the Hibernate ORM framework for databases is based on reflection. The ORM concept allows to automatically map relational links to object-oriented links. This approach makes the application completely independent from DBMS. You need to implement a trivial version of such ORM framework.

Let's assume that we have a set of model classes. Each class has no dependencies on other classes, and its fields can accept only the following value types String, Integer, Double, Boolean, Long. Let's specify a certain set of annotations for the class and its members, for example, User class:

```java
@OrmEntity(table = “simple_user”)
public class User {
  @OrmColumnId
  private Long id;
  @OrmColumn(name = “first_name”, length = 10)
  private String firstName;
  @OrmColumn(name = “first_name”, length = 10)
  private String lastName;
  @OrmColumn(name “age”)
  private Integer age;
  
  // setters/getters
}
```

Your OrmManager class will generate and execute SQL code during initialization of all classes marked with @OrmEntity annotation. This code will contain a CREATE TABLE command to create a table with the name specified in the annotation. Each field of the class marked with @OrmColumn annotation will become a column in this table. The field marked with the @OrmColumnId annotation indicates that an auto-incrementing identifier must be created. OrmManager also supports the following set of operations (for each of them, the corresponding SQL code is also generated at Runtime):

```java
public void save(Object entity)

public void update(Object entity)

public <T> T findById(Long id, Class<T> aClass)
```

- OrmManager should ensure that generated SQL is output to the console during execution.
- During initialization, OrmManager should remove generated tables.
- Update method replaces values in columns specified in the entity, even if the object field value is null.
