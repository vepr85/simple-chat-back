drop schema if exists app cascade;

create schema app;

create table app.user
(
    id            bigint generated always as identity
        constraint pk_user_id primary key,
    email         text not null
        constraint uk_user_email unique,
    password_hash text not null
);

create table app.chat
(
    id         bigint generated always as identity
        constraint pk_chat_id primary key,
    creator_id bigint not null
        constraint fk_chat_user_creator_id__user_id references app.user
);

create table app.chat_user
(
    chat_id bigint not null
        constraint fk_chat_user_chat_id__chat_id references app.chat,
    user_id bigint not null
        constraint fk_chat_user_user_id__user_id references app.user,

    constraint pk_chat_user_id primary key (chat_id, user_id)
);

create table app.message
(
    id          bigint generated always as identity
        constraint pk_message_id primary key,
    chat_id     bigint      not null
        constraint fk_message_chat_id__chat_id references app.chat,
    sender_id   bigint      not null
        constraint fk_message_sender_id__user_id references app.user,
    receiver_id bigint      not null
        constraint fk_message_receiver_id__user_id references app.user,
    message     text        not null,
    created_at  timestamptz not null
);


insert into app.user(email, password_hash) values('test1@test.com', 'test1');
insert into app.user(email, password_hash) values('test2@test.com', 'test2');
insert into app.user(email, password_hash) values('test3@test.com', 'test3');