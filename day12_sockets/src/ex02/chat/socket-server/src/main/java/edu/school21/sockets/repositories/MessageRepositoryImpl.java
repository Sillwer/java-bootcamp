package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class MessageRepositoryImpl implements MessageRepository {
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void save(Message entity) {
        String sqlQuery = "insert into message(content, chatroom_id, author_id, time_stamp) values (:content, :chatroom_id, :author_id, :time_stamp)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("content", entity.getContent());
        params.addValue("chatroom_id", entity.getChatRoomId());
        params.addValue("author_id", entity.getAuthorId());
        params.addValue("time_stamp", entity.getTimestamp());

        namedParameterJdbcTemplate.update(sqlQuery, params, keyHolder, new String[]{"id"});

        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<Message> findById(Long messageId) {
        try {
            Message message = jdbcTemplate.queryForObject(
                    "select * from message where id = ?",
                    (rs, rowN) -> new Message(
                            rs.getLong("id"),
                            rs.getString("content"),
                            rs.getLong("chatroom_id"),
                            rs.getLong("author_id"),
                            rs.getTimestamp("time_stamp")),
                    messageId
            );
            return Optional.of(message);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query(
                "select * from message",
                (rs, rowN) -> new Message(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getLong("chatroom_id"),
                        rs.getLong("author_id"),
                        rs.getTimestamp("time_stamp")));
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update(
                "update message set content = ?, chatroom_id = ?, author_id = ?, time_stamp = ? where id = ?",
                entity.getContent(), entity.getChatRoomId(), entity.getAuthorId(), entity.getTimestamp(),
                entity.getId()
        );
    }

    @Override
    public void delete(Long messageId) {
        jdbcTemplate.update("delete from message where id = ?", messageId);
    }

    @Override
    public List<Message> findByChatRoom(Long chatRoomId) {
        return jdbcTemplate.query(
                "select * from message where chatroom_id = ?",
                (rs, rowN) -> new Message(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getLong("chatroom_id"),
                        rs.getLong("author_id"),
                        rs.getTimestamp("time_stamp")),
                chatRoomId
        );
    }
}
