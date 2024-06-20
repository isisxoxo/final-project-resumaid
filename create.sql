drop database if exists resumaid;

create database resumaid;

use resumaid;

create table users (
    id char(8) not null,
    username varchar(16) not null,
    email varchar(128) not null,
    password varchar(60) not null, -- For BCrypt algorithm
    primary key(id)
)

grant all privileges on resumaid.* to 'fred'@'localhost';

flush privileges;