🚀 Bank Card Management System Development

📁 Initial Project Structure
A project structure with directories and placeholder documentation files (README Controller.md, README Service.md, etc.) is already prepared. All implementations must be added to the appropriate directories. After development is complete, the temporary README files must be removed so they are not included in the final build.

📝 Task Description
Develop a backend application in Java (Spring Boot) for managing bank cards:

* Card creation and management

* Card viewing

* Transfers between a user’s own cards


💳 Card Attributes

* Card number (encrypted, displayed as a mask: **** **** **** 1234)

* Cardholder

* Expiration date

* Status: Active, Blocked, Expired

* Balance


✅ Authentication and Authorization

* Spring Security + JWT

* Roles: ADMIN and USER


✅ Features

Administrator:

* Creates, blocks, activates, deletes cards

* Manages users

* Views all cards

User:

* Views own cards (search + pagination)

* Requests card blocking

* Makes transfers between own cards

* Views balance


✅ API

* CRUD operations for cards

* Transfers between own cards

* Filtering and pagination

* Validation and error messages


✅ Security

* Data encryption

* Role-based access control

* Card number masking


✅ Database

* PostgreSQL or MySQL

* Migrations via Liquibase (src/main/resources/db/migration)


✅ Documentation

* Swagger UI / OpenAPI — docs/openapi.yaml

* README.md with launch instructions


✅ Deployment and Testing

* Docker Compose for dev environment

* Liquibase migrations

* Unit tests for key business logic

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

🚀 Разработка Системы Управления Банковскими Картами

📁 Стартовая структура проекта
Проектная структура с директориями и временными описательными файлами (README Controller.md, README Service.md и т.д.) уже подготовлена. Все реализации необходимо добавлять в соответствующие директории. После завершения разработки временные README-файлы должны быть удалены, чтобы они не попали в итоговую сборку.

📝 Описание задачи
Разработать backend-приложение на Java (Spring Boot) для управления банковскими картами:

* Создание и управление картами

* Просмотр карт

* Переводы между своими картами

💳 Атрибуты карты

* Номер карты (зашифрован, отображается маской: **** **** **** 1234)

* Владелец

* Срок действия

* Статус: Активна, Заблокирована, Истек срок

* Баланс

✅ Аутентификация и авторизация

* Spring Security + JWT

* Роли: ADMIN и USER

✅ Возможности

Администратор:

* Создаёт, блокирует, активирует, удаляет карты

* Управляет пользователями

* Видит все карты

Пользователь:

* Просматривает свои карты (поиск + пагинация)

* Запрашивает блокировку карты

* Делает переводы между своими картами

* Смотрит баланс

✅ API

* CRUD для карт

* Переводы между своими картами

* Фильтрация и постраничная выдача

* Валидация и сообщения об ошибках

✅ Безопасность

* Шифрование данных

* Ролевой доступ

* Маскирование номеров карт

✅ Работа с БД

* PostgreSQL или MySQL

* Миграции через Liquibase (src/main/resources/db/migration)

✅ Документация

* Swagger UI / OpenAPI — docs/openapi.yaml

* README.md с инструкцией запуска

✅ Развёртывание и тестирование

* Docker Compose для dev-среды

* Liquibase-миграции

* Юнит-тесты ключевой бизнес-логики
