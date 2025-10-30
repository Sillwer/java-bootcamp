package school21.spring.service.repositories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school21.spring.service.models.User;

@Component
public class UsersRepositoryJdbcImpl implements UsersRepository<User> {
    DataSource dataSource;

    @Autowired
    public UsersRepositoryJdbcImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlQuery = "select * from users where id = ?;";
        Optional<User> user = Optional.empty();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = Optional.of(new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3)
                ));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "select * from users;";
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3)
                ));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    @Override
    public void save(User entity) {
        String sqlQuery = "insert into users (email, password) values (?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdentifier(rs.getLong(1));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        String sqlQuery = "update users set email = ?, password = ? where id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setLong(3, entity.getIdentifier());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "delete from users where id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sqlQuery = "select * from users where email = ?;";
        Optional<User> user = Optional.empty();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = Optional.of(new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3)
                ));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }
}
