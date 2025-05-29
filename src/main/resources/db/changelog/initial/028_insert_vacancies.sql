--changeset Aisha: 028 insert vacancies

-- Вакансии от Елены Волковой (elena.volkova@company1.kg) - IT компания
insert into VACANCIES(NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, USER_ID, CREATED_AT, UPDATED_AT)
values
    ('Главный бухгалтер', 'Ведение учета, составление отчетности, работа с налоговыми органами. Требуется опыт работы в IT сфере.', (select id from categories where name = 'бухучет'), 50000, 3, 10, true, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Финансовый аналитик', 'Анализ финансовых показателей компании, подготовка бюджетов, планирование инвестиций.', (select id from categories where name = 'финансовый анализ'), 55000, 2, 7, true, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SMM-менеджер', 'Ведение социальных сетей компании, создание контента, взаимодействие с подписчиками.', (select id from categories where name = 'SMM'), 40000, 1, 5, true, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Digital маркетолог', 'Разработка и реализация digital стратегии, работа с рекламными кампаниями в интернете.', (select id from categories where name = 'digital маркетинг'), 45000, 2, 6, true, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Банковский консультант', 'Консультирование клиентов по банковским продуктам, работа с корпоративными клиентами.', (select id from categories where name = 'банковское дело'), 38000, 1, 4, true, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Стажер-бухгалтер', 'Помощь в ведении первичной документации, обучение основам бухучета.', (select id from categories where name = 'бухучет'), 22000, 0, 1, false, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Младший аналитик', 'Подготовка аналитических отчетов, работа с базами данных.', (select id from categories where name = 'финансовый анализ'), 25000, 0, 2, false, (select id from users where email = 'elena.volkova@company1.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Вакансии от Олега Новикова (oleg.novikov@techcorp.kg) - Tech корпорация
insert into VACANCIES(NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, USER_ID, CREATED_AT, UPDATED_AT)
values
    ('Руководитель отдела рекламы', 'Управление командой, разработка рекламных стратегий, контроль бюджетов.', (select id from categories where name = 'реклама'), 70000, 5, 15, true, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Head of Digital Marketing', 'Стратегическое планирование digital активностей, управление командой маркетологов.', (select id from categories where name = 'digital маркетинг'), 80000, 7, 15, true, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Старший SMM-специалист', 'Разработка SMM стратегии, создание контент-планов, аналитика соцсетей.', (select id from categories where name = 'SMM'), 50000, 3, 8, true, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Креативный директор', 'Разработка креативных концепций, руководство креативной командой.', (select id from categories where name = 'реклама'), 85000, 8, 20, true, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Performance маркетолог', 'Настройка и оптимизация рекламных кампаний, работа с аналитикой.', (select id from categories where name = 'digital маркетинг'), 60000, 3, 7, true, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Маркетинг-стажер', 'Помощь в проведении маркетинговых исследований, ведение социальных сетей.', (select id from categories where name = 'SMM'), 20000, 0, 1, false, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Контент-менеджер', 'Создание и публикация контента, работа с блогом компании.', (select id from categories where name = 'digital маркетинг'), 32000, 1, 3, false, (select id from users where email = 'oleg.novikov@techcorp.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Вакансии от Татьяны Беловой (tatyana.belova@finance.kg) - Финансовая компания
insert into VACANCIES(NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, USER_ID, CREATED_AT, UPDATED_AT)
values
    ('Директор по финансам', 'Управление финансовой деятельностью компании, стратегическое планирование.', (select id from categories where name = 'финансовый анализ'), 120000, 10, 25, true, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Руководитель отдела кредитования', 'Управление кредитным портфелем, разработка кредитных продуктов.', (select id from categories where name = 'банковское дело'), 85000, 7, 15, true, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Ведущий бухгалтер', 'Ведение учета по участкам, составление налоговых деклараций.', (select id from categories where name = 'бухучет'), 55000, 5, 12, true, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Кредитный аналитик', 'Анализ кредитоспособности заемщиков, подготовка заключений по кредитам.', (select id from categories where name = 'банковское дело'), 50000, 2, 6, true, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Главный финансовый аналитик', 'Комплексный анализ финансового состояния, бюджетирование и планирование.', (select id from categories where name = 'финансовый анализ'), 75000, 6, 12, true, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Помощник бухгалтера', 'Ведение первичной документации, помощь в составлении отчетов.', (select id from categories where name = 'бухучет'), 28000, 0, 2, false, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Стажер банковского дела', 'Изучение банковских продуктов, помощь кредитным специалистам.', (select id from categories where name = 'банковское дело'), 18000, 0, 1, false, (select id from users where email = 'tatyana.belova@finance.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Вакансии от Игоря Орлова (igor.orlov@marketing.kg) - Маркетинговое агентство
insert into VACANCIES(NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, USER_ID, CREATED_AT, UPDATED_AT)
values
    ('Директор по маркетингу', 'Разработка маркетинговой стратегии компании, управление маркетинговым бюджетом.', (select id from categories where name = 'digital маркетинг'), 90000, 8, 20, true, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Арт-директор', 'Создание визуальных концепций рекламных кампаний, руководство дизайн-командой.', (select id from categories where name = 'реклама'), 65000, 5, 12, true, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Senior SMM Manager', 'Стратегическое планирование SMM активностей для крупных клиентов.', (select id from categories where name = 'SMM'), 55000, 4, 10, true, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Аккаунт-менеджер', 'Ведение клиентов, координация проектов, презентация решений.', (select id from categories where name = 'реклама'), 48000, 2, 6, true, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Digital стратег', 'Разработка комплексных digital стратегий для клиентов агентства.', (select id from categories where name = 'digital маркетинг'), 70000, 5, 10, true, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Junior дизайнер', 'Создание графических материалов для рекламных кампаний.', (select id from categories where name = 'реклама'), 25000, 0, 2, false, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SMM-стажер', 'Помощь в ведении социальных сетей клиентов, создание простого контента.', (select id from categories where name = 'SMM'), 20000, 0, 1, false, (select id from users where email = 'igor.orlov@marketing.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Вакансии от Натальи Зайцевой (natalya.zaitseva@medical.kg) - Медицинский центр
insert into VACANCIES(NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, USER_ID, CREATED_AT, UPDATED_AT)
values
    ('Главный врач', 'Управление медицинским центром, контроль качества медицинских услуг.', (select id from categories where name = 'терапия'), 100000, 15, 30, true, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Врач-стоматолог', 'Прием пациентов, лечение и профилактика заболеваний полости рта.', (select id from categories where name = 'стоматология'), 70000, 3, 15, true, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Детский врач-педиатр', 'Наблюдение и лечение детей от рождения до 18 лет.', (select id from categories where name = 'педиатрия'), 60000, 3, 12, true, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Врач-терапевт', 'Первичный прием пациентов, диагностика и лечение общих заболеваний.', (select id from categories where name = 'терапия'), 55000, 2, 10, true, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Заведующий стоматологическим отделением', 'Организация работы стоматологического отделения, контроль качества.', (select id from categories where name = 'стоматология'), 85000, 8, 20, true, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Медицинская сестра', 'Помощь врачам в проведении процедур, уход за пациентами.', (select id from categories where name = 'терапия'), 30000, 1, 5, false, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Интерн-стоматолог', 'Обучение под руководством опытных врачей, участие в приеме пациентов.', (select id from categories where name = 'стоматология'), 25000, 0, 1, false, (select id from users where email = 'natalya.zaitseva@medical.kg'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);