package com.crowdproj.rating.biz

import com.crowdproj.rating.biz.group.operation
import com.crowdproj.rating.biz.group.stubs
import com.crowdproj.rating.biz.validation.finishRatingValidation
import com.crowdproj.rating.biz.validation.validation
import com.crowdproj.rating.biz.worker.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand
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
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = MkplAdId.NONE }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")
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
//                validation {
//                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
//                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
//                    validateIdNotEmpty("Проверка на непустой id")
//                    validateIdProperFormat("Проверка формата id")
//
//                    finishAdValidation("Успешное завершение процедуры валидации")
//                }
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
//                validation {
//                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
//                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
//                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
//                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
//                    validateIdNotEmpty("Проверка на непустой id")
//                    validateIdProperFormat("Проверка формата id")
//                    validateTitleNotEmpty("Проверка на непустой заголовок")
//                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
//                    validateDescriptionNotEmpty("Проверка на непустое описание")
//                    validateDescriptionHasContent("Проверка на наличие содержания в описании")
//
//                    finishAdValidation("Успешное завершение процедуры валидации")
//                }
            }

            operation("Удалить рейтинг", CwpRatingCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
//                validation {
//                    worker("Копируем поля в adValidating") {
//                        adValidating = adRequest.deepCopy() }
//                    worker("Очистка id") { adValidating.id = MkplAdId(adValidating.id.asString().trim()) }
//                    validateIdNotEmpty("Проверка на непустой id")
//                    validateIdProperFormat("Проверка формата id")
//                    finishAdValidation("Успешное завершение процедуры валидации")
//                }
            }

            operation("Поиск рейтингов", CwpRatingCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
//                validation {
//                    worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }
//
//                    finishAdFilterValidation("Успешное завершение процедуры валидации")
//                }
            }
        }.build()
    }
}