-- changeset Aisha: 020 delete admin role from roles

-- delete admins
delete from users where ROLE_ID = (select id from roles where role = 'ADMIN');

-- delete role
delete from roles where ROLE = 'ADMIN'