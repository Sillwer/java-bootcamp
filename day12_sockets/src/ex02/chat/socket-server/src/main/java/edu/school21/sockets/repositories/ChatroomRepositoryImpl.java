package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class ChatroomRepositoryImpl implements ChatroomRepository {
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChatroomRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        try {
            Chatroom chatroom = jdbcTemplate.queryForObject(
                    "select * from chatroom where name = ?",
                    (rs, rowN) -> new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("owner")),
                    name
            );
            return Optional.of(chatroom);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Chatroom> findByOwnerId(Long id) {
        try {
            Chatroom chatroom = jdbcTemplate.queryForObject(
                    "select * from chatroom where owner = ?",
                    (rs, rowN) -> new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("owner")),
                    id
            );
            return Optional.of(chatroom);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Chatroom entity) {
        String sqlQuery = "insert into chatroom(name, owner) values (:name, :owner)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", entity.getName());
        params.addValue("owner", entity.getOwnerId());

        namedParameterJdbcTemplate.update(sqlQuery, params, keyHolder, new String[]{"id"});
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        try {
            Chatroom chatroom = jdbcTemplate.queryForObject(
                    "select * from chatroom where id = ?",
                    (rs, rowN) -> new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("owner")),
                    id
            );
            return Optional.of(chatroom);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Chatroom> findAll() {
        return jdbcTemplate.query(
                "select * from chatroom",
                (rs, rowN) -> new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("owner")));
    }

    @Override
    public void update(Chatroom entity) {
        jdbcTemplate.update("update chatroom set name = ?, owner = ? where id = ?",
                entity.getName(), entity.getOwnerId(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from chatroom where id = ?", id);
    }
}
