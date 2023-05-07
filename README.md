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

[//]: # (## Хранение, репозитории, базы данных)

[//]: # ()

[//]: # (1. [ok-marketplace-repo-tests]&#40;ok-marketplace-repo-tests&#41; - Базовые тесты для репозиториев всех баз данных)

[//]: # (2. [ok-marketplace-repo-inmemory]&#40;ok-marketplace-repo-inmemory&#41; - Репозиторий на базе кэша в памяти для тестирования)

[//]: # (3. [ok-marketplace-repo-postgresql]&#40;ok-marketplace-repo-postgresql&#41; - Репозиторий на базе PostgreSQL)

[//]: # (4. [ok-marketplace-repo-cassandra]&#40;ok-marketplace-repo-cassandra&#41; - Репозиторий на базе Cassandra)

[//]: # (5. [ok-marketplace-repo-gremlin]&#40;ok-marketplace-repo-gremlin&#41; - Репозиторий на базе Apache TinkerPop Gremlin и ArcadeDb)

[//]: # (### Функции &#40;эндпониты&#41;)

[//]: # ()

[//]: # (1. CRUDS &#40;create, read, update, delete, search&#41; для объявлений &#40;ad&#41;)

[//]: # (1. ad.offers &#40;опционально&#41;)

[//]: # ()

[//]: # (### Описание сущности ad)

[//]: # ()

[//]: # (1. Info)

[//]: # (    1. Title)

[//]: # (    2. Description)

[//]: # (    3. Owner)

[//]: # (    4. Visibility)

[//]: # (2. DealSide: Demand/Proposal)

[//]: # (3. ProductType &#40;гаечный ключ, ...&#41;)

[//]: # (4. ProductId - идентификатор модели товара)