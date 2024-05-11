CREATE TABLE order_tab (
    id bigint auto_increment primary key,
    order_status tinyint,
    distance int,
    origin_longitude decimal(9,6),
    origin_latitude decimal(9,6),
    dest_longitude decimal(9,6),
    dest_latitude decimal(9,6),
    ctime int,
    mtime int,
    is_del tinyint
);