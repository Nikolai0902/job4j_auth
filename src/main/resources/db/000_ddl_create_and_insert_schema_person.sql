create table if not exists person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000),
    unique (login)
);

insert into person (login, password) values ('parsentev', '123');
insert into person (login, password) values ('ban', '123');
insert into person (login, password) values ('ivan', '123');