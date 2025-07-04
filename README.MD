# JobSearch - Платформа для поиска работы

JobSearch - это современная веб-платформа, которая соединяет работодателей и соискателей, предоставляя удобные инструменты для поиска работы и талантов.

## 🚀 Основные функции

### Для соискателей:
- **Создание и управление резюме** - подробные профили с опытом работы и навыками
- **Поиск вакансий** - удобный поиск по различным критериям
- **Просмотр компаний** - информация о потенциальных работодателях
- **Система чатов** - прямое общение с работодателями
- **Персональный профиль** - управление личной информацией
- **Аналитика** - отслеживание откликов и активности

### Для работодателей:
- **Размещение вакансий** - создание и управление объявлениями о работе
- **Поиск кандидатов** - просмотр резюме потенциальных сотрудников
- **Управление компанией** - корпоративный профиль
- **Система чатов** - общение с кандидатами
- **Аналитика** - отслеживание откликов и активности

## 🛠 Технологический стек

- **Backend**: Java Spring Boot
- **Frontend**: FreeMarker Templates + Tailwind CSS
- **База данных**: H2
- **Безопасность**: Spring Security
- **Интернационализация**: Spring i18n (русский, английский, греческий)
- **UI компоненты**: Font Awesome Icons

## 📋 Требования

- Java 21 или выше
- Maven 3.6+

## ⚙️ Установка и запуск

### 1. Клонирование репозитория
```bash
git clone https://github.com/your-username/jobsearch.git
cd jobsearch
```

### 2. Настройка базы данных

Создайте базу данных и обновите настройки в `application.properties`:

```properties
spring.datasource.url: jdbc:h2:./db/job-search;AUTO_SERVER=TRUE
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Сборка и запуск
```bash
mvn clean install
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8888`

## 🗄️ Структура базы данных

### Основные таблицы:
- `users` - пользователи системы
- `resumes` - резюме соискателей
- `vacancies` - вакансии от работодателей
- `categories` - категории вакансий/резюме
- `messages` - система чатов
- `responses` - отклики на вакансии

## 🎨 Интерфейс
### Цветовая схема
- Основной цвет: Blue (#2563eb)
- Вторичные цвета: Gray, Green, Purple
- Темная тема для футера и некоторых элементов

## 🌐 Интернационализация

Поддерживаемые языки:
- 🇷🇺 Русский (по умолчанию)
- 🇬🇧 English
- 🇬🇷 Ελληνικά

Переключение языка доступно в верхней части страницы.

## 🔐 Безопасность

- **Аутентификация**: Spring Security с формой входа
- **Авторизация**: Role-based access control (RBAC)
- **CSRF защита**: включена для всех форм
- **Валидация данных**: серверная и клиентская валидация
- **Безопасные пароли**: хеширование с использованием BCrypt

### Роли пользователей:
- `ROLE_APPLICANT` - соискатели
- `ROLE_EMPLOYER` - работодатели

## 📱 API Endpoints

### Публичные страницы:
- `GET /` - главная страница
- `GET /auth/login` - страница входа
- `GET /auth/register` - страница регистрации

### Для соискателей:
- `GET /vacancies` - список вакансий
- `GET /vacancies/{id}` - детали вакансии
- `GET /resumes/create` - создание резюме
- `GET /applicants/{id}` - профиль соискателя

### Для работодателей:
- `GET /resumes` - список резюме
- `GET /resumes/{id}` - детали резюме
- `GET /vacancies/create` - создание вакансии
- `GET /employers/{id}` - профиль работодателя

### Система чатов:
- `GET /chat` - список чатов
- `GET /chat/room/{id}` - конкретный чат
- `POST /chat/send` - отправка сообщения

## 📦 Развертывание

### Docker :
```dockerfile
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8888
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Создайте Pull Request


## 📞 Контакты

- **Email**: a.orozbekovaa@gmail.com
- **Телефон**: +996 553 37 20 07

## 🔄 Changelog

### v1.0.0 (Текущая версия)
- ✅ Базовая функциональность для соискателей и работодателей
- ✅ Система регистрации и аутентификации
- ✅ Мультиязычность
- ✅ Система чатов
- ✅ Поиск и фильтрация

### Планируемые обновления:
- 📧 Email уведомления
- 📊 Расширенная аналитика
- 🔍 Улучшенный поиск с AI
- 📱 Мобильное приложение
- 💳 Система платежей для премиум функций

