drop database if exists resumaid;

create database resumaid;

use resumaid;

create table users (
    id char(8) not null,
    username varchar(16) not null,
    email varchar(128) not null,
    password varchar(60) not null, -- For BCrypt algorithm
    primary key(id)
);

create table bookings (
    id char(128) not null,
    userid varchar(8) not null,
    starttime datetime not null,
    endtime datetime not null,
    meetinglink varchar(128) not null,
    primary key(id),
    constraint fk_user_id foreign key(userid)
        references users(id)
);

-- grant all privileges on resumaid.* to 'fred'@'localhost';

-- flush privileges;