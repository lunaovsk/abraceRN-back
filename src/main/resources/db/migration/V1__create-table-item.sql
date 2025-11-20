create table item (
    id bigint not null auto_increment,
    item_name varchar(255) not null,
    category varchar(100) not null,
    type varchar(100),
    size varchar(50),
    gender varchar(50),
    quantity int not null,
    expiration_at date,
    create_at datetime,
    status bit(1),
    primary key (id)
);