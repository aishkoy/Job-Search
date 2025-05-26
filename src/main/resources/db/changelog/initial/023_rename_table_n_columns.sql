-- changeset Aisha: 023 rename tables and columns and modify messages structure for H2 database

ALTER TABLE RESPONDED_APPLICANTS RENAME TO RESPONSES;

ALTER TABLE MESSAGES ALTER COLUMN RESPONDED_APPLICANT_ID RENAME TO response_id;

ALTER TABLE MESSAGES ADD COLUMN is_read BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE MESSAGES ADD COLUMN is_applicant boolean default true not null;

ALTER TABLE MESSAGES ADD COLUMN user_id BIGINT;
MERGE INTO MESSAGES m
    USING (
        SELECT
            msg.id AS msg_id,
            CASE
                WHEN msg.is_applicant = TRUE THEN
                    (SELECT r.user_id FROM RESUMES r
                                               JOIN RESPONSES resp ON r.id = resp.resume_id
                     WHERE resp.id = msg.response_id)
                ELSE
                    (SELECT v.user_id FROM VACANCIES v
                                               JOIN RESPONSES resp ON v.id = resp.vacancy_id
                     WHERE resp.id = msg.response_id)
                END AS determined_user_id
        FROM MESSAGES msg
    ) AS source
ON (m.id = source.msg_id)
WHEN MATCHED THEN
    UPDATE SET m.user_id = source.determined_user_id;

ALTER TABLE MESSAGES ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE MESSAGES ADD CONSTRAINT fk_messages_user FOREIGN KEY (user_id) REFERENCES USERS(id);

ALTER TABLE MESSAGES DROP COLUMN is_applicant;