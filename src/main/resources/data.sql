CREATE TABLE IF NOT EXISTS users(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    age INTEGER,
    email VARCHAR(255),
    password TEXT,
    phone_number VARCHAR(55),
    avatar TEXT,
    account_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS contact_types(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categories(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    parent_id INTEGER,

    CONSTRAINT parent_id_fk FOREIGN KEY(parent_id) REFERENCES categories(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS resumes(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    applicant_id INTEGER,
    name VARCHAR(255),
    category_id INTEGER,
    salary REAL,
    is_active BOOLEAN,
    created_date TIMESTAMP,
    update_time TIMESTAMP,


    CONSTRAINT resumes_applicant_id_fk  FOREIGN KEY(applicant_id) REFERENCES users(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT resumes_category_id_fk  FOREIGN KEY(category_id) REFERENCES categories(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS contacts_info(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    type_id INTEGER,
    resume_id INTEGER,
    "value" VARCHAR(255),

    CONSTRAINT contacts_type_id_fk  FOREIGN KEY(type_id) REFERENCES contact_types(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT contacts_resume_id_fk  FOREIGN KEY(resume_id) REFERENCES resumes(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS vacancies(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    category_id INTEGER,
    salary REAL,
    exp_from INTEGER,
    exp_to INTEGER,
    is_active BOOLEAN,
    author_id INTEGER,
    created_date TIMESTAMP,
    update_time TIMESTAMP,

    CONSTRAINT vacancies_category_id_fk  FOREIGN KEY(category_id) REFERENCES categories(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT vacancies_author_id_fk  FOREIGN KEY(author_id) REFERENCES users(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS responded_applicants(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id INTEGER,
    vacancy_id INTEGER,
    confirmation BOOLEAN,

    CONSTRAINT resp_app_resume_id_fk FOREIGN KEY(resume_id) REFERENCES resumes(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT resp_app_vacancy_id_fk FOREIGN KEY(vacancy_id) REFERENCES vacancies(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS messages(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    responded_applicant_id INTEGER,
    timestamp TIMESTAMP,
    content TEXT,

    CONSTRAINT messages_responded_applicant_id_fk FOREIGN KEY(responded_applicant_id) REFERENCES responded_applicants(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS education_info(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id INTEGER,
    institution TEXT,
    program TEXT,
    start_date DATE,
    end_date DATE,
    degree VARCHAR(255),

    CONSTRAINT edu_resume_id_fk  FOREIGN KEY(resume_id) REFERENCES resumes(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS work_experience_info(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    resume_id INTEGER,
    years INTEGER,
    company_name VARCHAR(255),
    position TEXT,
    responsibilities TEXT,

    CONSTRAINT work_exp_resume_id_fk  FOREIGN KEY(resume_id) REFERENCES resumes(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES
    ('Иван', 'Петров', 28, 'ivan@example.com', 'hash_password_1', '+79001234567', 'avatar1.jpg', 'applicant'),
    ('Мария', 'Сидорова', 42, 'maria@company.com', 'hash_password_2', '+79009876543', 'avatar2.jpg', 'employer');

-- Категории вакансий/резюме
INSERT INTO categories (name, parent_id)
VALUES
    ('IT', NULL),
    ('Разработка', 1),
    ('Тестирование', 1);

-- Типы контактов
INSERT INTO contact_types (type)
VALUES
    ('Email'),
    ('Телефон'),
    ('Telegram');

-- Два резюме для соискателя
INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES
    (1, 'Java разработчик', 2, 150000, TRUE, '2025-01-01', '2025-03-05'),
    (1, 'QA инженер', 3, 120000, TRUE, '2025-02-01', '2025-02-20');

-- Контактная информация для резюмешек
INSERT INTO contacts_info (type_id, resume_id, "value")
VALUES
    (1, 1, 'ivan@example.com'),
    (2, 1, '+79001234567'),
    (3, 1, '@ivan_petrov'),
    (1, 2, 'ivan@example.com'),
    (2, 2, '+79001234567'),
    (3, 2, '@ivan_petrov');

-- Образование для резюмешек
INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES
    (1, 'Московский Государственный Университет', 'Компьютерные науки', '2012-09-01', '2016-06-01', 'Бакалавр'),
    (2, 'Санкт-Петербургский Политехнический Университет', 'Тестирование ПО', '2017-09-01', '2022-06-01', 'Бакалавр');

-- Опыт работы для резюмешек
INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES
    (1, 3, 'ООО "ТехСофт"', 'Java разработчик', 'Разработка и поддержка веб-приложений, работа с базами данных'),
    (2, 2, 'ООО "КвалитиЛаб"', 'QA инженер', 'Ручное и автоматизированное тестирование, составление тест-кейсов, регрессионное тестирование');


-- Две вакансии работодателя
INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES
    ('Senior Java Developer', 'Требуется опытный Java разработчик для участия в крупном проекте.', 2, 180000, 3, 5, TRUE, 2, '2025-02-22', '2025-03-08'),
    ('QA Engineer', 'Ищем специалиста по тестированию программного обеспечения.', 3, 130000, 1, 3, FALSE, 2, '2025-01-08', NOW());

-- Соискатель откликнулся на вакансии
INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES
    (1, 1, FALSE),
    (2, 2, TRUE);

-- Сообщения соискателя
INSERT INTO messages (responded_applicant_id, timestamp, content)
VALUES (1, '2025-03-18', 'Здравствуйте! Я заинтересован в вашей вакансии Senior Java Developer. У меня есть 3 года опыта работы с Java и веб-приложениями.'),
       (2, '2025-03-01', 'Здравствуйте! Меня заинтересовала ваша вакансия QA Engineer. У меня есть опыт в автоматизированном тестировании и работе с различными инструментами.');
