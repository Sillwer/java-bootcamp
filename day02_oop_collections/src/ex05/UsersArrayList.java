package ex05;

import java.util.Iterator;

public class UsersArrayList implements UsersList {
    User[] users;
    int capacity = 10;
    int count = 0;

    UsersArrayList() {
        users = new User[capacity];
    }

    public void addUser(User usr) {
        if (count == capacity) {
            capacity *= 2;
            User[] usersNew = new User[capacity];
            System.arraycopy(users, 0, usersNew, 0, count);
            users = usersNew;
        }

        users[count++] = usr;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        User usr = null;
        for (User u : users) {
            if (u != null && u.getId().equals(id)) {
                usr = u;
                break;
            }
        }

        if (usr == null) {
            throw new UserNotFoundException("Пользователь с id = " + id + " отсутствует.");
        }

        return usr;
    }

    public User getUserByIndex(Integer index) {
        if (index < 0 || index > count - 1) {
            return null;
        }
        return users[index];
    }

    public Integer getNumberOfUsers() {
        return count;
    }


    @Override
    public Iterator<User> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public User next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return users[index++];
            }
        };
    }
}
