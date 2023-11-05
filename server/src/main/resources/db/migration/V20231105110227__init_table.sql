CREATE TABLE t_users (
    id varchar(36) primary key,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    bio text,
    image varchar(300)
);

CREATE Index ON t_users (username, email);


CREATE TABLE t_tags (
    id varchar(36) primary key,
    name varchar(255) not null
);
CREATE Index ON t_tags (name);

CREATE TABLE t_articles (
    id varchar(36) primary key,
    title varchar(255) not null,
    description varchar(255) not null,
    body text,
    author_id varchar(36) not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP
);

CREATE Index ON t_articles (title, author_id);

CREATE TABLE t_article_tag (
   article_id varchar(36) not null,
   tag_id varchar(36) not null
);

CREATE UNIQUE Index ON t_article_tag (article_id, tag_id);
