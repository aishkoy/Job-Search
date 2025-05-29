-- changeset Aisha: 021 add prefer language column

ALTER TABLE users
    ADD COLUMN prefer_lang VARCHAR(10);

UPDATE users
SET prefer_lang = 'ru';

ALTER TABLE users
    ALTER COLUMN prefer_lang SET NOT NULL;
