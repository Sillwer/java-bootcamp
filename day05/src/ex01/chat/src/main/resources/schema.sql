\set ON_ERROR_STOP on

create database chat_s21;

\c chat_s21;

create table users (
    id          SERIAL PRIMARY KEY,
    login       VARCHAR UNIQUE,
    password    VARCHAR
);

create table chat_room (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR UNIQUE,
    owner_id    INT REFERENCES users(id)
);

create table message (
    id              SERIAL PRIMARY KEY,
    author_id       INT REFERENCES users(id),
    chat_rooms_id   INT REFERENCES chat_room(id),
    msg_text        text,
    date_time       timestamp
);

-- Хотя бы как-то компенсируем недостатки схемы
CREATE INDEX idx_chat_room_owner_id ON chat_room(owner_id);
CREATE INDEX idx_message_chat_rooms_id ON message(chat_rooms_id);

create table chat_room_member (
    room_id INT REFERENCES chat_room(id),
    user_id INT REFERENCES users(id),
    UNIQUE (user_id, room_id)
)
