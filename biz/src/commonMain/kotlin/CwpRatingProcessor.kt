package com.crowdproj.rating.biz

import com.crowdproj.rating.biz.group.operation
import com.crowdproj.rating.biz.group.stubs
import com.crowdproj.rating.biz.validation.*
import com.crowdproj.rating.biz.worker.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.cor.rootChain
import com.crowdproj.rating.cor.worker

class CwpRatingProcessor {
    suspend fun exec(ctx: CwpRatingContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<CwpRatingContext> {
            initStatus("Инициализация статуса")

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
                    worker("Очистка поля тип рейтинга") { ratingValidating.scoreTypeId = CwpRatingScoreTypeId(ratingValidating.scoreTypeId.asString().trim()) }
                    worker("Очистка поля объект рейтинга") { ratingValidating.objectId = CwpRatingObjectId(ratingValidating.objectId.asString().trim()) }
                    worker("Очистка поля тип объекта рейтинга") { ratingValidating.objectTypeId = CwpRatingObjectTypeId(ratingValidating.objectTypeId.asString().trim()) }

                    validateScoreTypeIdNotEmpty("Проверка, что поле тип рейтинга не пустое")
                    validateObjectIdNotEmpty("Проверка, что поле объект рейтинга не пустое")
                    validateObjectTypeIdNotEmpty("Проверка, что поле тип объекта рейтинга не пустое")

                    validateScoreTypeIdProperFormat("Проверка формата поля тип рейтинга")
                    validateObjectIdProperFormat("Проверка формата поля объект рейтинга")
                    validateObjectTypeIdProperFormat("Проверка формата поля тип объекта рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
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

                    worker("Очистка поля id рейтинга") { ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка, что поле id рейтинга не пустое")
                    validateIdProperFormat("Проверка формата поля id рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
//                computeAdState("Вычисление состояния рейтинга")
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

                    worker("Очистка поля id рейтинга") { ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim()) }
                    worker("Очистка поля тип рейтинга") { ratingValidating.scoreTypeId = CwpRatingScoreTypeId(ratingValidating.scoreTypeId.asString().trim()) }
                    worker("Очистка поля объект рейтинга") { ratingValidating.objectId = CwpRatingObjectId(ratingValidating.objectId.asString().trim()) }
                    worker("Очистка поля тип объекта рейтинга") { ratingValidating.objectTypeId = CwpRatingObjectTypeId(ratingValidating.objectTypeId.asString().trim()) }

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

                    worker("Очистка поля id рейтинга") { ratingValidating.id = CwpRatingId(ratingValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка, что поле id рейтинга не пустое")
                    validateIdProperFormat("Проверка формата поля id рейтинга")

                    finishRatingValidation("Завершение проверок")
                }
            }

            operation("Поиск рейтингов", CwpRatingCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в ratingFilterValidating") { ratingFilterValidating = ratingFilterRequest.copy() }
                    finishRatingValidation("Завершение проверок")
                }
            }
        }.build()
    }
}