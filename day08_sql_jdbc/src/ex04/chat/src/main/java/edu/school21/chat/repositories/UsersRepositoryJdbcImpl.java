package edu.school21.chat.repositories;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    DataSource dataSource = null;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        HashMap<Long, User> users = new HashMap<>();

        String prepareQuery = "with "
                + "users_page as ( "
                + "	 select * from users "
                + "	 order by id"
                + " 	limit ? offset ? "
                + "),  room_member as ( "
                + "	 select chat_room_member.room_id as room_id, user_id "
                + "	 from chat_room_member "
                + " 	where user_id in (select id from users_page) "
                + "),  rooms_with_owner as ( "
                + "	 select ch.id as room_id, name as room_name, user_id, owner_id "
                + "	 from chat_room as ch "
                + "    join room_member as m on ch.id = m.room_id "
                + ") "
                + "select id, login, password, room_id, room_name, owner_id "
                + "from users_page as u "
                + "  left join rooms_with_owner as r	on u.id = r.user_id; ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(prepareQuery)) {
            if (!connection.isValid(30)) {
                throw new SQLException("Bad connection");
            }

            statement.setInt(1, size);
            statement.setInt(2, size * page);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long userId = rs.getLong("id");
                User user = users.get(userId);

                if (user == null) {
                    user = new User(rs.getLong("id"),
                            rs.getString("login"), rs.getString("password"),
                            new HashMap<>(), new HashMap<>());
                    users.put(userId, user);
                }

                HashMap<Long, Chatroom> socializesChatRooms = user.getSocializesChatRooms();
                HashMap<Long, Chatroom> ownChatRooms = user.getOwnChatRooms();

                Long roomId = rs.getLong("room_id");
                User owner = null;
                if (rs.getLong("owner_id") == userId) {
                    owner = user;
                }
                Chatroom room = socializesChatRooms.get(roomId);
                if (room == null) {
                    room = new Chatroom(roomId, rs.getString("room_name"), owner, null);
                }

                socializesChatRooms.put(roomId, room);
                if (rs.getLong("owner_id") == userId) {
                    ownChatRooms.put(roomId, room);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<User>(users.values());
    }
}
