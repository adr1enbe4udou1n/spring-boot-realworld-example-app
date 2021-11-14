create sequence hibernate_sequence start 1 increment 1
create table users (id int8 not null, bio text, created_at timestamp not null, email varchar(255) not null, image varchar(255), name varchar(255) not null, password varchar(255), updated_at timestamp not null, primary key (id))
alter table if exists users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email)
