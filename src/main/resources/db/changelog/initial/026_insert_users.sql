--changeset Aisha: 026 insert users

-- 5 соискателей (APPLICANT)
insert into USERS(NAME, SURNAME, AGE, EMAIL, PASSWORD, PHONE_NUMBER, ENABLED, ROLE_ID, PREFER_LANG)
values
    ('Алексей', 'Иванов', 25, 'alexey.ivanov@gmail.com', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996555123456', true, (select id from roles where name = 'APPLICANT'), 'ru'),
    ('Мария', 'Петрова', 28, 'maria.petrova@gmail.com', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996555234567', true, (select id from roles where name = 'APPLICANT'), 'ru'),
    ('Дмитрий', 'Сидоров', 23, 'dmitry.sidorov@gmail.com', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996555345678', true, (select id from roles where name = 'APPLICANT'), 'ru'),
    ('Анна', 'Козлова', 30, 'anna.kozlova@gmail.com', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996555456789', true, (select id from roles where name = 'APPLICANT'), 'ru'),
    ('Сергей', 'Морозов', 26, 'sergey.morozov@gmail.com', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996555567890', true, (select id from roles where name = 'APPLICANT'), 'ru');

-- 5 работодателей (EMPLOYER)
insert into USERS(NAME, SURNAME, AGE, EMAIL, PASSWORD, PHONE_NUMBER, ENABLED, ROLE_ID, PREFER_LANG)
values
    ('Елена', 'Волкова', 35, 'elena.volkova@company1.kg', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996312111111', true, (select id from roles where name = 'EMPLOYER'), 'ru'),
    ('Олег', 'Новиков', 42, 'oleg.novikov@techcorp.kg', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996312222222', true, (select id from roles where name = 'EMPLOYER'), 'ru'),
    ('Татьяна', 'Белова', 38, 'tatyana.belova@finance.kg', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996312333333', true, (select id from roles where name = 'EMPLOYER'), 'ru'),
    ('Игорь', 'Орлов', 45, 'igor.orlov@marketing.kg', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996312444444', true, (select id from roles where name = 'EMPLOYER'), 'ru'),
    ('Наталья', 'Зайцева', 40, 'natalya.zaitseva@medical.kg', '$2a$12$QplEod5/XykNz0b2QKykru2cLvtimCcEjEyDTihaGwk5qyI7yjeSm', '+996312555555', true, (select id from roles where name = 'EMPLOYER'), 'ru');