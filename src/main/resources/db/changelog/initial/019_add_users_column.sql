-- changeset Aisha: add column reset_password_token to user table
alter table USERS
add column reset_password_token varchar(255)