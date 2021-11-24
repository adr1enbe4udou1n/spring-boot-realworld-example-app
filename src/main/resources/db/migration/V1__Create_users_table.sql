create sequence hibernate_sequence start 1 increment 1;
create table follower_user (following_id int8 not null, follower_id int8 not null, primary key (following_id, follower_id));
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
alter table if exists follower_user add constraint FK69r22uoit0gd4aiiy7knsvdje foreign key (follower_id) references users on delete cascade;
alter table if exists follower_user add constraint FKny04t54t69f2krvt7xt40yiw0 foreign key (following_id) references users on delete cascade;
