--changeset Aisha: 025 insert additional categories

-- Основные категории
insert into CATEGORIES(name, PARENT_ID)
values ('финансы', null);

-- Подкатегории для "финансы"
insert into CATEGORIES(name, PARENT_ID)
values ('бухучет', (select id from categories where name = 'финансы')),
       ('банковское дело', (select id from categories where name = 'финансы')),
       ('финансовый анализ', (select id from categories where name = 'финансы'));

insert into CATEGORIES(name, PARENT_ID)
values ('маркетинг', null);

-- Подкатегории для "маркетинг"
insert into CATEGORIES(name, PARENT_ID)
values ('digital маркетинг', (select id from categories where name = 'маркетинг')),
       ('SMM', (select id from categories where name = 'маркетинг')),
       ('реклама', (select id from categories where name = 'маркетинг'));

insert into CATEGORIES(name, PARENT_ID)
values ('медицина', null);

-- Подкатегории для "медицина"
insert into CATEGORIES(name, PARENT_ID)
values ('терапия', (select id from categories where name = 'медицина')),
       ('педиатрия', (select id from categories where name = 'медицина')),
       ('стоматология', (select id from categories where name = 'медицина'));