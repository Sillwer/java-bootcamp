\set ON_ERROR_STOP on

create database socket_chat;
\c socket_chat;

BEGIN;
create table users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(15) UNIQUE,
    password TEXT
);
create index users_name ON users(name);

create table chatroom (
    id SERIAL PRIMARY KEY,
    name varchar(30) UNIQUE,
    owner INTEGER REFERENCES users(id)
);
create index chatroom_owner ON chatroom(owner);
create index chatroom_name ON chatroom(name);

create table message (
    id SERIAL PRIMARY KEY,
    content TEXT,
    chatroom_id INTEGER REFERENCES chatroom(id),
    author_id INTEGER REFERENCES users(id),
    time_stamp timestamp
);
create index message_chatroom_id ON message(chatroom_id);
create index message_author_id ON message(author_id);
create index message_time_stamp on message(time_stamp);

COMMIT;
