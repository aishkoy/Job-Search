-- changeset Aisha: 023 rename tables and columns

alter table RESPONDED_APPLICANTS rename to RESPONSES;
alter table MESSAGES alter column RESPONDED_APPLICANT_ID rename to response_id;
alter table MESSAGES add column is_read boolean not null default false;
alter table MESSAGES add column is_applicant boolean not null default true;
