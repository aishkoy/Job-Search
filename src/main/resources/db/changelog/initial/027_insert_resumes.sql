--changeset Aisha: 027 insert resumes

-- Резюме для Алексея Иванова (alexey.ivanov@gmail.com)
insert into RESUMES(USER_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_AT, UPDATED_AT)
values
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Главный бухгалтер', (select id from categories where name = 'бухучет'), 45000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Финансовый аналитик', (select id from categories where name = 'финансовый анализ'), 50000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Банковский служащий', (select id from categories where name = 'банковское дело'), 40000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Старший бухгалтер', (select id from categories where name = 'бухучет'), 48000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Кредитный аналитик', (select id from categories where name = 'банковское дело'), 46000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Помощник бухгалтера', (select id from categories where name = 'финансы'), 25000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'alexey.ivanov@gmail.com'), 'Стажер-аналитик', (select id from categories where name = 'финансовый анализ'), 20000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Резюме для Марии Петровой (maria.petrova@gmail.com)
insert into RESUMES(USER_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_AT, UPDATED_AT)
values
    ((select id from users where email = 'maria.petrova@gmail.com'), 'SMM-менеджер', (select id from categories where name = 'SMM'), 35000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Digital маркетолог', (select id from categories where name = 'маркетинг'), 42000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Специалист по рекламе', (select id from categories where name = 'реклама'), 38000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Ведущий SMM-специалист', (select id from categories where name = 'SMM'), 44000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Руководитель digital отдела', (select id from categories where name = 'digital маркетинг'), 55000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Младший SMM', (select id from categories where name = 'SMM'), 22000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'maria.petrova@gmail.com'), 'Контент-менеджер', (select id from categories where name = 'digital маркетинг'), 30000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Резюме для Дмитрия Сидорова (dmitry.sidorov@gmail.com)
insert into RESUMES(USER_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_AT, UPDATED_AT)
values
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Врач-терапевт', (select id from categories where name = 'терапия'), 55000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Детский врач', (select id from categories where name = 'педиатрия'), 50000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Стоматолог', (select id from categories where name = 'стоматология'), 60000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Ведущий терапевт', (select id from categories where name = 'медицина'), 65000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Детский стоматолог', (select id from categories where name = 'стоматология'), 62000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Медицинский ассистент', (select id from categories where name = 'терапия'), 28000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'dmitry.sidorov@gmail.com'), 'Интерн-педиатр', (select id from categories where name = 'педиатрия'), 25000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Резюме для Анны Козловой (anna.kozlova@gmail.com)
insert into RESUMES(USER_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_AT, UPDATED_AT)
values
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Банковский кредитный специалист', (select id from categories where name = 'банковское дело'), 48000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Ведущий финансовый аналитик', (select id from categories where name = 'финансовый анализ'), 55000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Старший бухгалтер', (select id from categories where name = 'бухучет'), 42000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Руководитель отдела кредитования', (select id from categories where name = 'банковское дело'), 65000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Главный финансовый аналитик', (select id from categories where name = 'финансы'), 70000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Банковский консультант', (select id from categories where name = 'банковское дело'), 32000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'anna.kozlova@gmail.com'), 'Младший аналитик', (select id from categories where name = 'финансовый анализ'), 28000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Резюме для Сергея Морозова (sergey.morozov@gmail.com)
insert into RESUMES(USER_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_AT, UPDATED_AT)
values
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Руководитель отдела рекламы', (select id from categories where name = 'реклама'), 65000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Digital маркетинг директор', (select id from categories where name = 'digital маркетинг'), 70000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'SMM-стратег', (select id from categories where name = 'SMM'), 45000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Креативный директор', (select id from categories where name = 'реклама'), 75000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Head of Digital Marketing', (select id from categories where name = 'маркетинг'), 80000, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Специалист по рекламе', (select id from categories where name = 'реклама'), 35000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ((select id from users where email = 'sergey.morozov@gmail.com'), 'Маркетинг-координатор', (select id from categories where name = 'digital маркетинг'), 32000, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);