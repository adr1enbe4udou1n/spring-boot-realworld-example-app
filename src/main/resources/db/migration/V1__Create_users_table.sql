create sequence hibernate_sequence start 1 increment 1;
create table users (
    id int8 not null,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255),
    bio text,
    image varchar(255),
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
alter table if exists users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
