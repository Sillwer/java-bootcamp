package edu.school21.chat.repositories;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {

    DataSource dataSource = null;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(Long id) {
        Optional<Message> msg = Optional.empty();
        if (dataSource == null) {
            return msg;
        }

        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(30)) {
                throw new SQLException("Problem with connection");
            }

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from message where id = " + id);
            if (!rs.next()) {
                return msg;
            }

            Long authorId = rs.getLong("author_id");
            Long roomId = rs.getLong("chat_rooms_id");
            String test = rs.getString("msg_text");
            Timestamp timestamp = rs.getTimestamp("date_time");

            User author = null;
            rs = statement.executeQuery("select * from users where id = " + authorId);
            if (rs.next()) {
                author = new User(authorId, rs.getString("login"), rs.getString("password"), null, null);
            }

            Chatroom chatroom = null;
            rs = statement.executeQuery("select * from chat_room where id = " + roomId);
            if (rs.next()) {
                chatroom = new Chatroom(roomId, rs.getString("name"), null, null);
            }

            Message message = new Message(id, author, chatroom, test, timestamp);

            msg = Optional.of(message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return msg;
    }
}
