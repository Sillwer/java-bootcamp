INSERT INTO users (login, password) VALUES
('user_1', 'password_1'),
('user_2', 'password_2'),
('user_3', 'password_3'),
('user_4', 'password_4'),
('user_5', 'password_5');

INSERT INTO chat_room (name, owner_id) VALUES
('room_A', 1),
('room_B', 3),
('room_C', 4),
('room_D', 4),
('room_E', 4);

INSERT INTO chat_room_member (room_id, user_id) VALUES
-- room_A 3 users
(1,1),
(1,2),
(1,3),
-- room_B 2 users
(2,3),
(2,5),
-- room_C 3 users
(3,1),
(3,4),
(3,5);

INSERT INTO message (author_id, chat_rooms_id, msg_text, date_time) VALUES
-- room_A
(1, 1, 'hello there =)', '2025-04-08 01:13:00'),
(2, 1, 'hello, user_1', '2025-04-08 01:15:00'),
(3, 1, 'hi, user_1', '2025-04-08 01:15:00'),
-- room_B
(3, 2, 'did you farm at last sale?', '2025-04-08 01:17:00'),
(5, 2, 'yes, 10 points', '2025-04-08 01:18:00'),
(3, 2, 'me too 10', '2025-04-08 01:18:00'),
-- room_C
(1, 3, 'who will go to the cinema today?', '2025-04-08 01:21:00'),
(4, 3, 'me', '2025-04-08 01:22:00'),
(1, 3, 'Who is with us?', '2025-04-08 01:25:00'),
(5, 3, 'i will go', '2025-04-08 01:30:00');
