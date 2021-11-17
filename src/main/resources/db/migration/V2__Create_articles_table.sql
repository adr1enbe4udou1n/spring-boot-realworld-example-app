create table articles (
    id int8 not null,
    author_id int8,
    title varchar(255) not null,
    slug varchar(255) not null,
    description text not null,
    body text not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
create table comments (
    id int8 not null,
    article_id int8,
    author_id int8,
    body text not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
create table tags (
    id int8 not null,
    name varchar(255) not null,
    primary key (id)
);

create table article_favorite (
    article_id int8 not null,
    user_id int8 not null,
    primary key (user_id, article_id)
);
create table article_tag (
    article_id int8 not null,
    tag_id int8 not null,
    primary key (tag_id, article_id)
);

alter table if exists articles add constraint UK_sn7al9fwhgtf98rvn8nxhjt4f unique (slug);
alter table if exists tags add constraint UK_t48xdq560gs3gap9g7jg36kgc unique (name);
alter table if exists articles add constraint FKe02fs2ut6qqoabfhj325wcjul foreign key (author_id) references users;
alter table if exists comments add constraint FKk4ib6syde10dalk7r7xdl0m5p foreign key (article_id) references articles;
alter table if exists comments add constraint FKn2na60ukhs76ibtpt9burkm27 foreign key (author_id) references users;

alter table if exists article_favorite add constraint FKn1usm6hwr86i29w1t0h9q0huk foreign key (article_id) references articles;
alter table if exists article_favorite add constraint FK8m1q6r31lmkejfp8l8eka6n93 foreign key (user_id) references users;
alter table if exists article_tag add constraint FKorgtes2l2k1xoykum0ati73r5 foreign key (article_id) references users;
alter table if exists article_tag add constraint FK3nvn435qf5rn1e9ph51e3r55h foreign key (tag_id) references tags;
