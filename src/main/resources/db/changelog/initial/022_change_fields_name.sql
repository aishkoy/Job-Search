-- changeset Aisha: 022 change fields name in constant

alter table ROLES
alter column ROLE rename to NAME;

alter table CONTACT_TYPES
alter column TYPE rename to NAME;