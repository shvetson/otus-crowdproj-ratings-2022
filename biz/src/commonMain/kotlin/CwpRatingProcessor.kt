package com.crowdproj.rating.biz

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import com.crowdproj.rating.biz.general.*
import com.crowdproj.rating.biz.permission.accessValidation
import com.crowdproj.rating.biz.permission.chainPermissions
import com.crowdproj.rating.biz.permission.frontPermissions
import com.crowdproj.rating.biz.permission.searchTypes
import com.crowdproj.rating.biz.repo.*
import com.crowdproj.rating.biz.validation.*
import com.crowdproj.rating.biz.worker.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.common.model.*

class CwpRatingProcessor(private val settings: CwpRatingCorSettings = CwpRatingCorSettings()) {
    suspend fun exec(ctx: CwpRatingContext) =
        BusinessChain.exec(ctx.apply { settings = this@CwpRatingProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<CwpRatingContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание рейтинга", CwpRatingCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadRatingTypeId("Имитация ошибки валидации типа рейтинга")
                    stubValidationBadObjectTypeId("Имитация ошибки валидации типа объекта")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingValidating") { ratingValidating = ratingRequest.deepCopy() }

                    worker("Очистка поля id рейтинга") { ratingValidating.id = CwpRatingId.NONE }
                    worker("Очистка поля тип рейтинга") {
                        ratingValidating.scoreTypeId =
                            CwpRatingScoreTypeId(ratingValidating.scoreTypeId.asString().trim())
                    }
                    worker("Очистка поля объект рейтинга") {
                        ratingValidating.objectId = CwpRatingObjectId(ratingValidating.objectId.asString().trim())
                    }
                    worker("Очистка поля тип объекта рейтинга") {
                        ratingValidating.objectTypeId =
                            CwpRatingObjectTypeId(ratingValidating.objectTypeId.asString().trim())
                    }

                    validateScoreTypeIdNotEmpty("Проверка, что поле тип рейтинга не пустое")
                    validateObjectIdNotEmpty("Проверка, что поле объект рейтинга не пустое")
                    validateObjectTypeIdNotEmpty("Проверка, что поле тип объекта рейтинга не пустое")

                    validateScoreTypeIdProperFormat("Проверка формата поля тип рейтинга")
                    validateObjectIdProperFormat("Проверка формата поля объект рейтинга")
                    validateObjectTypeIdProperFormat("Проверка формата поля тип объекта рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
                chainPermissions("Проверка разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Проверка прав доступа")
                    repoCreate("Создание рейтинга в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда") // удобно для работы с UI приложения
                prepareResult("Подготовка ответа")
            }

            operation("Получить рейтинг", CwpRatingCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingValidating") { ratingValidating = ratingRequest.deepCopy() }

                    worker("Очистка поля id рейтинга") {
                        ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim())
                    }
                    validateIdNotEmpty("Проверка, что поле id рейтинга не пустое")
                    validateIdProperFormat("Проверка формата поля id рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
                chainPermissions("Проверка разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение рейтинга в БД")
                    accessValidation("Проверка прав доступа")
                    worker {
                        title = "Подготовка ответа на Read"
                        on { state == CwpRatingState.RUNNING }
                        handle { ratingRepoDone = ratingRepoRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }

            operation("Изменить рейтинг", CwpRatingCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadRatingTypeId("Имитация ошибки валидации типа рейтинга")
                    stubValidationBadObjectTypeId("Имитация ошибки валидации типа объекта")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingValidating") { ratingValidating = ratingRequest.deepCopy() }

                    worker("Очистка поля id рейтинга") {
                        ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim())
                    }
                    worker("Очистка поля тип рейтинга") {
                        ratingValidating.scoreTypeId =
                            CwpRatingScoreTypeId(ratingValidating.scoreTypeId.asString().trim())
                    }
                    worker("Очистка поля объект рейтинга") {
                        ratingValidating.objectId = CwpRatingObjectId(ratingValidating.objectId.asString().trim())
                    }
                    worker("Очистка поля тип объекта рейтинга") {
                        ratingValidating.objectTypeId =
                            CwpRatingObjectTypeId(ratingValidating.objectTypeId.asString().trim())
                    }

                    validateIdNotEmpty("Проверка, что поле id рейтинга не пустое")
                    validateScoreTypeIdNotEmpty("Проверка, что поле тип рейтинга не пустое")
                    validateObjectIdNotEmpty("Проверка, что поле объект рейтинга не пустое")
                    validateObjectTypeIdNotEmpty("Проверка, что поле тип объекта рейтинга не пустое")

                    validateIdProperFormat("Проверка формата поля id рейтинга")
                    validateScoreTypeIdProperFormat("Проверка формата поля тип рейтинга")
                    validateObjectIdProperFormat("Проверка формата поля объект рейтинга")
                    validateObjectTypeIdProperFormat("Проверка формата поля тип объекта рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
                chainPermissions("Проверка разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение рейтинга из БД")
                    accessValidation("Проверка прав доступа")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление рейтинга в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }

            operation("Удалить рейтинг", CwpRatingCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingValidating") { ratingValidating = ratingRequest.deepCopy() }

                    worker("Очистка поля id рейтинга") {
                        ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim())
                    }
                    validateIdNotEmpty("Проверка, что поле id рейтинга не пустое")
                    validateIdProperFormat("Проверка формата поля id рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
                chainPermissions("Проверка разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение рейтинга из БД")
                    accessValidation("Проверка прав доступа")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление рейтинга в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }

            operation("Поиск рейтингов", CwpRatingCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingFilterValidating") {
                        ratingFilterValidating = ratingFilterRequest.copy()
                    }
                    finishRatingFilterValidation("Завершение проверок")
                }
                chainPermissions("Проверка разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")
                repoSearch("Поиск рейтинга в БД по фильтру")
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}