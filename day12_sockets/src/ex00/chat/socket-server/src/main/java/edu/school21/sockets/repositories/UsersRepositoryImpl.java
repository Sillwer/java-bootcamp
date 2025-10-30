package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository {
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UsersRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void save(User entity) {
        String sqlQuery = "insert into users (name, password) values (:name, :password)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", entity.getUserName());
        params.addValue("password", entity.getPassword());

        namedParameterJdbcTemplate.update(sqlQuery, params, keyHolder, new String[]{"id"});
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> foundUsers = jdbcTemplate.query(
                "select * from users where id = ?",
                (rs, rowNum) -> new User(id, rs.getString("name"), rs.getString("password")),
                id
        );

        if (foundUsers.size() > 1) {
            throw new RuntimeException(
                    String.format("Unexpected count of users (%d) with id %s. Expected 1 or 0", foundUsers.size(), id)
            );
        } else if (foundUsers.size() == 1) {
            return Optional.of(foundUsers.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "select * from users",
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name"), rs.getString("password"))
        );
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("update users set name = ?, password = ? where id = ?",
                entity.getUserName(), entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from users where id = ?", id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        List<User> foundUsers = jdbcTemplate.query(
                "select * from users where name = ?",
                (rs, rowNum) -> new User(rs.getLong("id"), userName, rs.getString("password")),
                userName
        );

        if (foundUsers.size() > 1) {
            throw new RuntimeException(
                    String.format("Unexpected count of users (%d) with name %s. Expected 1 or 0", foundUsers.size(), userName)
            );
        } else if (foundUsers.size() == 1) {
            return Optional.of(foundUsers.get(0));
        } else {
            return Optional.empty();
        }
    }
}
