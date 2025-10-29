package ex05;

interface UsersList extends Iterable<User> {
    void addUser(User usr);

    User getUserById(Integer id) throws UserNotFoundException;

    User getUserByIndex(Integer index);

    Integer getNumberOfUsers();
}