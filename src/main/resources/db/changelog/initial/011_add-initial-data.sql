--changeset Aisha:add_initial_data
INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Иван', 'Петров', 28, 'ivan@example.com', 'hash_password_1', '+79001234567', 'avatar1.jpg', 'applicant'),
       ('Мария', 'Сидорова', 42, 'maria@company.com', 'hash_password_2', '+79009876543', 'avatar2.jpg', 'employer');

--data-categories
INSERT INTO categories (name, parent_id)
VALUES ('it', NULL);

--data-subcategories
INSERT INTO categories (name, parent_id)
VALUES ('разработка', (SELECT id FROM categories WHERE name = 'it' LIMIT 1)),
       ('тестирование', (SELECT id FROM categories WHERE name = 'it' LIMIT 1));

--data-contact-types
INSERT INTO contact_types (type)
VALUES ('email'),
       ('телефон'),
       ('telegram'),
       ('facebook'),
       ('linkedin');

--data-resumes
INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES ((SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1),
        'Java разработчик',
        (SELECT id FROM categories WHERE name = 'разработка' LIMIT 1),
        150000, TRUE, '2025-01-01', '2025-03-05'),
       ((SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1),
        'QA инженер',
        (SELECT id FROM categories WHERE name = 'тестирование' LIMIT 1),
        120000, FALSE, '2025-02-01', '2025-02-20');

--data-education
INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        'Московский Государственный Университет', 'Компьютерные науки', '2012-09-01', '2016-06-01', 'Бакалавр'),
       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        'Санкт-Петербургский Политехнический Университет', 'тестирование ПО', '2017-09-01', '2022-06-01', 'Бакалавр');

--data-work-experience
INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        3, 'ООО "ТехСофт"', 'Java разработчик', 'разработка и поддержка веб-приложений, работа с базами данных'),
       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        2, 'ООО "КвалитиЛаб"', 'QA инженер',
        'Ручное и автоматизированное тестирование, составление тест-кейсов, регрессионное тестирование');

-- для резюме "Java разработчик"
INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'email' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'Java разработчик' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'ivan@example.com');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'телефон' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'Java разработчик' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        '+79001234567');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'telegram' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'Java разработчик' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        '@ivan_petrov');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'facebook' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'Java разработчик' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'https://facebook.com/ivan.petrov');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'linkedin' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'Java разработчик' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'https://linkedin.com/in/ivanpetrov');

-- для резюме "QA инженер"
INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'email' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'QA инженер' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'ivan@example.com');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'телефон' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'QA инженер' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        '+79001234567');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'telegram' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'QA инженер' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        '@ivan_petrov');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'facebook' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'QA инженер' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'https://facebook.com/ivan.petrov');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES ((SELECT id FROM contact_types WHERE type = 'linkedin' LIMIT 1),
        (SELECT id FROM resumes WHERE name = 'QA инженер' AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1) LIMIT 1),
        'https://linkedin.com/in/ivanpetrov');

--data-vacancies
INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Senior Java Developer',
        'Требуется опытный Java разработчик для участия в крупном проекте.',
        (SELECT id FROM categories WHERE name = 'разработка' LIMIT 1),
        180000, 3, 5, TRUE,
        (SELECT id FROM users WHERE email = 'maria@company.com' LIMIT 1),
        '2025-02-22', '2025-03-08'),
       ('QA Engineer',
        'Ищем специалиста по тестированию программного обеспечения.',
        (SELECT id FROM categories WHERE name = 'тестирование' LIMIT 1),
        130000, 1, 3, FALSE,
        (SELECT id FROM users WHERE email = 'maria@company.com' LIMIT 1),
        '2025-01-08', CURRENT_TIMESTAMP());

--data-applicants
INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ((SELECT id
         FROM resumes
         WHERE name = 'Java разработчик'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        (SELECT id FROM vacancies WHERE name = 'Senior Java Developer' LIMIT 1),
        FALSE),
       ((SELECT id
         FROM resumes
         WHERE name = 'QA инженер'
           AND applicant_id = (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
         LIMIT 1),
        (SELECT id FROM vacancies WHERE name = 'QA Engineer' LIMIT 1),
        TRUE);

--data-messages
INSERT INTO messages (responded_applicant_id, timestamp, content)
VALUES ((SELECT id
         FROM responded_applicants
         WHERE resume_id = (SELECT id
                            FROM resumes
                            WHERE name = 'Java разработчик'
                              AND applicant_id =
                                  (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
                            LIMIT 1)
           AND vacancy_id = (SELECT id FROM vacancies WHERE name = 'Senior Java Developer' LIMIT 1)
         LIMIT 1),
        '2025-03-18',
        'Здравствуйте! Я заинтересован в вашей вакансии Senior Java Developer. У меня есть 3 года опыта работы с Java и веб-приложениями.'),
       ((SELECT id
         FROM responded_applicants
         WHERE resume_id = (SELECT id
                            FROM resumes
                            WHERE name = 'QA инженер'
                              AND applicant_id =
                                  (SELECT id FROM users WHERE email = 'ivan@example.com' LIMIT 1)
                            LIMIT 1)
           AND vacancy_id = (SELECT id FROM vacancies WHERE name = 'QA Engineer' LIMIT 1)
         LIMIT 1),
        '2025-03-01',
        'Здравствуйте! Меня заинтересовала ваша вакансия QA Engineer. У меня есть опыт в автоматизированном тестировании и работе с различными инструментами.');