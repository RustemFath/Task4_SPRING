CREATE TABLE IF NOT EXISTS users
(
    id bigserial
    CONSTRAINT pk_users PRIMARY KEY,
    username       varchar(100),
    fio            varchar(250)
);

CREATE TABLE IF NOT EXISTS logins
(
    id bigserial
    CONSTRAINT pk_logins PRIMARY KEY,
    access_date    timestamp,
    user_id        bigint references users(id),
    application    varchar(50)
);