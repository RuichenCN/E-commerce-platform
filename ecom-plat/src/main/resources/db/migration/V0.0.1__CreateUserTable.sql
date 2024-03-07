CREATE TABLE user (
    user_id  varchar(36) not null,
    username varchar(100) BINARY not null unique,
    password varchar(32) BINARY not null,
    primary key (user_id)
) ENGINE = InnoDB CHARSET = utf8mb4;

INSERT INTO user (user_id, username, password) VALUES ('1', 'test', 'test');