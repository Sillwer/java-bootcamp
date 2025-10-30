package school21.spring.service.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import school21.spring.service.models.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository<User>, AutoCloseable {
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "select * from users where id = ?;",
                    (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email")),
                    id);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from users;",
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email")));
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("insert into users (email) values (?)",
                    new String[] { "id" });
            statement.setString(1, entity.getEmail());
            return statement;
        },
                keyHolder);

        entity.setIdentifier(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("update users set email = ? where id = ?", entity.getEmail(), entity.getIdentifier());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from users where id = ?", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            List<User> users = jdbcTemplate.query("select * from users where email = ?",
                    (rs, dw) -> new User(rs.getLong("id"), rs.getString("email")),
                    email);
            return Optional.of(users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public void close() {
        if (dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
