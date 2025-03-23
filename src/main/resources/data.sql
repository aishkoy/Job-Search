CREATE TABLE IF NOT EXISTS users
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    surname      VARCHAR(255),
    age          INTEGER,
    email        VARCHAR(255),
    password     TEXT,
    phone_number VARCHAR(55),
    avatar       TEXT,
    account_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS contact_types
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categories
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255),
    parent_id INTEGER,

    CONSTRAINT parent_id_fk FOREIGN KEY (parent_id) REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS resumes
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    applicant_id INTEGER,
    name         VARCHAR(255),
    category_id  INTEGER,
    salary       REAL,
    is_active    BOOLEAN,
    created_date TIMESTAMP,
    update_time  TIMESTAMP,


    CONSTRAINT resumes_applicant_id_fk FOREIGN KEY (applicant_id) REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT resumes_category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS contacts_info
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    type_id   INTEGER,
    resume_id INTEGER,
    "value"   VARCHAR(255),

    CONSTRAINT contacts_type_id_fk FOREIGN KEY (type_id) REFERENCES contact_types (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT contacts_resume_id_fk FOREIGN KEY (resume_id) REFERENCES resumes (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS vacancies
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    description  TEXT,
    category_id  INTEGER,
    salary       REAL,
    exp_from     INTEGER,
    exp_to       INTEGER,
    is_active    BOOLEAN,
    author_id    INTEGER,
    created_date TIMESTAMP,
    update_time  TIMESTAMP,

    CONSTRAINT vacancies_category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT vacancies_author_id_fk FOREIGN KEY (author_id) REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS responded_applicants
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id    INTEGER,
    vacancy_id   INTEGER,
    confirmation BOOLEAN,

    CONSTRAINT resp_app_resume_id_fk FOREIGN KEY (resume_id) REFERENCES resumes (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT resp_app_vacancy_id_fk FOREIGN KEY (vacancy_id) REFERENCES vacancies (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS messages
(
    id                     INTEGER AUTO_INCREMENT PRIMARY KEY,
    responded_applicant_id INTEGER,
    timestamp              TIMESTAMP,
    content                TEXT,

    CONSTRAINT messages_responded_applicant_id_fk FOREIGN KEY (responded_applicant_id) REFERENCES responded_applicants (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS education_info
(
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id   INTEGER,
    institution TEXT,
    program     TEXT,
    start_date  DATE,
    end_date    DATE,
    degree      VARCHAR(255),

    CONSTRAINT edu_resume_id_fk FOREIGN KEY (resume_id) REFERENCES resumes (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS work_experience_info
(
    id               INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id        INTEGER,
    years            INTEGER,
    company_name     VARCHAR(255),
    position         TEXT,
    responsibilities TEXT,

    CONSTRAINT work_exp_resume_id_fk FOREIGN KEY (resume_id) REFERENCES resumes (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Иван', 'Петров', 28, 'ivan@example.com', 'hash_password_1', '+79001234567', 'avatar1.jpg', 'applicant'),
       ('Мария', 'Сидорова', 42, 'maria@company.com', 'hash_password_2', '+79009876543', 'avatar2.jpg', 'employer');

-- Категории вакансий/резюме
INSERT INTO categories (name, parent_id)
VALUES ('IT', NULL),
       ('разработка', 1),
       ('тестирование', 1);

-- Типы контактов
INSERT INTO contact_types (type)
VALUES ('email'),
       ('телефон'),
       ('telegram');

-- Резюме
INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES ((SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1),
        'Java разработчик',
        (SELECT id FROM categories WHERE name = 'разработка' ORDER BY id LIMIT 1),
        150000, TRUE, '2025-01-01', '2025-03-05'),

       ((SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1),
        'QA инженер',
        (SELECT id FROM categories WHERE name = 'тестирование' ORDER BY id LIMIT 1),
        120000, FALSE, '2025-02-01', '2025-02-20');

-- Контактная информация
INSERT INTO contacts_info (type_id, resume_id, "value")
VALUES ((SELECT id FROM contact_types WHERE type = 'email' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        'ivan@example.com'),

       ((SELECT id FROM contact_types WHERE type = 'телефон' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '+79001234567'),

       ((SELECT id FROM contact_types WHERE type = 'telegram' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '@ivan_petrov'),

       ((SELECT id FROM contact_types WHERE type = 'email' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        'ivan@example.com'),

       ((SELECT id FROM contact_types WHERE type = 'телефон' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '+79001234567'),

       ((SELECT id FROM contact_types WHERE type = 'telegram' ORDER BY id LIMIT 1),
        (SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '@ivan_petrov');

-- Образование
INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        'Московский Государственный Университет', 'Компьютерные науки', '2012-09-01', '2016-06-01', 'Бакалавр'),

       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        'Санкт-Петербургский Политехнический Университет', 'тестирование ПО', '2017-09-01', '2022-06-01', 'Бакалавр');

-- Опыт работы
INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        3, 'ООО "ТехСофт"', 'Java разработчик', 'разработка и поддержка веб-приложений, работа с базами данных'),

       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        2, 'ООО "КвалитиЛаб"', 'QA инженер',
        'Ручное и автоматизированное тестирование, составление тест-кейсов, регрессионное тестирование');

-- Вакансии
INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date,
                       update_time)
VALUES ('Senior Java Developer',
        'Требуется опытный Java разработчик для участия в крупном проекте.',
        (SELECT id FROM categories WHERE name = 'разработка' ORDER BY id LIMIT 1),
        180000, 3, 5, TRUE,
        (SELECT id FROM users WHERE email = 'maria@company.com' ORDER BY id LIMIT 1),
        '2025-02-22', '2025-03-08'),

       ('QA Engineer',
        'Ищем специалиста по тестированию программного обеспечения.',
        (SELECT id FROM categories WHERE name = 'тестирование' ORDER BY id LIMIT 1),
        130000, 1, 3, FALSE,
        (SELECT id FROM users WHERE email = 'maria@company.com' ORDER BY id LIMIT 1),
        '2025-01-08', NOW());

-- Отклики на вакансии
INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        (SELECT id FROM vacancies WHERE name = 'Senior Java Developer' ORDER BY id LIMIT 1),
        FALSE),

       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        (SELECT id FROM vacancies WHERE name = 'QA Engineer' ORDER BY id LIMIT 1),
        TRUE);

-- Сообщения
INSERT INTO messages (responded_applicant_id, timestamp, content)
VALUES ((SELECT id
         FROM responded_applicants
         WHERE resume_id = (SELECT id
                            FROM resumes
                            WHERE name = 'Java разработчик'
                              AND applicant_id =
                                  (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
                            ORDER BY id 
                            LIMIT 1)
           AND vacancy_id = (SELECT id FROM vacancies WHERE name = 'Senior Java Developer' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '2025-03-18',
        'Здравствуйте! Я заинтересован в вашей вакансии Senior Java Developer. У меня есть 3 года опыта работы с Java и веб-приложениями.'),

       ((SELECT id
         FROM responded_applicants
         WHERE resume_id = (SELECT id
                            FROM resumes
                            WHERE name = 'QA инженер'
                              AND applicant_id =
                                  (SELECT id FROM users WHERE email = 'ivan@example.com' ORDER BY id LIMIT 1)
                            ORDER BY id 
                            LIMIT 1)
           AND vacancy_id = (SELECT id FROM vacancies WHERE name = 'QA Engineer' ORDER BY id LIMIT 1)
         ORDER BY id 
         LIMIT 1),
        '2025-03-01',
        'Здравствуйте! Меня заинтересовала ваша вакансия QA Engineer. У меня есть опыт в автоматизированном тестировании и работе с различными инструментами.');