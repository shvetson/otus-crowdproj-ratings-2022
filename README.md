# Ratings
**A microservice for managing ratings (user, article, product, etc) on marketplace.**

Marketplace - это площадка, на которой пользователи выставляют предложения и потребности, дают оценку товару / услуге, 
принимают решения о покупке / сотрудничестве на основании рейтинга, используют обратную связь для развития своего продукта.

Задача площадки - предоставить рейтинг и количество голосов, участвующих в оценке товара / услуги, всем заинтересованным участникам.

## Документация

1. Маркетинг
   1. [Заинтересанты](./docs/01-marketing/01-stakeholders.md)
   2. [Целевая аудитория](./docs/01-marketing/02-target-audience.md)
   3. [Модель поведения](./docs/01-marketing/03-user-behaivor.md)
   4. [Пользовательские истории](./docs/01-marketing/04-user-stories.md)
   5. [Анализ экономики](./docs/01-marketing/05-economy.md)
2. DevOps
   1. [Схема инфраструктуры](./docs/02-devops/01-infrastruture.md)
   2. [Схема мониторинга](./docs/02-devops/02-monitoring.md)
3. Тесты
4. Архитектура
   1. [Компонентная схема](./docs/04-architecture/01-arch.md)
   2. [Интеграционная схема](./docs/04-architecture/02-integration.md)
   3. [Описание API](./docs/04-architecture/03-api.md)

## Структура проекта

1. Транспортные модели, API
   1. [specs](./specs/specs-ratings-v1.yaml) Описание API в форме OpenAPI-спецификаций.
   2. [api-v1](./api-v1) Генерация первой версии транспортных моделей.
   3. [api-log](./api-log) - Генерация моделей логирования.
   4. [common](./common) Модуль с общими классами для модулей проекта, в частности, там располагаются внутренние модели и контекст.
   5. [mappers-v1](./mappers-v1) Маппинг между внутренними моделями и моделями API v1.
   6. [mappers-log](./mappers-log) - Маппинг между внутренними моделями и моделями логирования.

2. Фреймворки и транспорты

   1. [spring](./spring) - Приложение на Spring Framework.
   2. [ktor](./ktor) - Приложение на Ktor.
   3. [rabbit](./rabbit) - Микросервис на RabbitMQ.
   4. [kafka](./kafka) - Микросервис на Kafka.

3. Мониторинг и логирование

   1. [deploy](deploy) - Инструменты мониторинга и деплоя.
   2. [lib-logging-common](./lib-logging-common) - Общие объявления для логирования.
   3. [logging-kermit](./lib-logging-kermit) - Библиотека логирования на базе библиотеки Kermit.
   4. [lib-logging-logback](./lib-logging-logback) - Библиотека логирования на базе библиотеки Logback.

4. Модули бизнес-логики

   1. [stubs](./stubs) - Стабы для ответов сервиса.
   2. [lib-cor](./lib-cor) - Библиотека цепочки обязанностей для бизнес-логики.
   3. [biz](./biz) - Модуль бизнес-логики приложения.

5. Хранение, репозитории, базы данных

   1. [repo-tests](./repo-tests) - Базовые тесты для репозиториев всех баз данных.
   2. [repo-in-memory](./repo-in-memory) - Репозиторий на базе кэша в памяти для тестирования.
   3. [repo-postgresql](./repo-postgresql) - Репозиторий на базе PostgreSQL.
   4. [repo-cassandra](./repo-cassandra) - Репозиторий на базе Cassandra.
   5. [repo-gremlin](./repo-gremlin) - Репозиторий на базе Apache TinkerPop Gremlin и ArcadeDb.

6. Аутентификация и авторизация
   1. [auth](./auth) - Настройка (базовая) авторизации.