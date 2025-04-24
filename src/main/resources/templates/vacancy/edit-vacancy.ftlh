<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <!-- Шапка с названием -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-edit mr-2"></i>Редактирование вакансии
                    </h1>
                    <a href="/vacancies/${vacancy.id}"
                       class="bg-white/20 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-white/30 transition font-medium">
                        <i class="fas fa-eye mr-2"></i>Предпросмотр
                    </a>
                </div>
                <p class="mt-2 text-blue-100 text-sm">
                    Внесите необходимые изменения в информацию о вакансии
                </p>
            </div>

            <form action="/vacancies/${vacancy.id}/edit" method="post" class="space-y-6">
                <@main.csrf/>
                <input type="hidden" name="id" value="${vacancy.id}"/>
                <input type="hidden" name="employer.id" value="${vacancyForm.employer.id}"/>

                <!-- Основная информация -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i>Общая информация
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Левая колонка -->
                        <div>
                            <div class="mb-4">
                                <label for="name" class="block text-sm font-semibold text-gray-700">Название вакансии *</label>
                                <@spring.formInput "vacancyForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите название вакансии"' />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="category.id" class="block text-sm font-semibold text-gray-700">Категория *</label>
                                <#assign categoryMap = {}>
                                <#list categories as category>
                                    <#assign categoryMap = categoryMap + {category.id?string: category.name}>
                                </#list>

                                <@spring.formSingleSelect
                                "vacancyForm.category.id",
                                categoryMap,
                                'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="salary" class="block text-sm font-semibold text-gray-700">Зарплата (c) *</label>
                                <@spring.formInput "vacancyForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите зарплату"' />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <!-- Правая колонка -->
                        <div>
                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Статус вакансии</label>
                                <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
                                    <div class="flex space-x-4">
                                        <@spring.formRadioButtons
                                        "vacancyForm.isActive",
                                        {"true":"Активная", "false":"Неактивная"},
                                        "<br>",
                                        "class='h-4 w-4 text-blue-600 border-gray-300 focus:ring-blue-500'"
                                        />
                                        <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Требуемый опыт работы (лет) *</label>
                                <div class="grid grid-cols-2 gap-4">
                                    <div>
                                        <label for="expFrom" class="block text-xs text-gray-500">Минимальный</label>
                                        <@spring.formInput "vacancyForm.expFrom", 'type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="От"' />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>

                                    <div>
                                        <label for="expTo" class="block text-xs text-gray-500">Максимальный</label>
                                        <@spring.formInput "vacancyForm.expTo", 'type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="До"' />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Описание вакансии -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-file-alt text-blue-600 mr-2"></i>Описание вакансии
                    </h3>

                    <div class="mb-4">
                        <label for="description" class="block text-sm font-semibold text-gray-700 mb-2">Подробное описание</label>
                        <@spring.formTextarea "vacancyForm.description", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="6" placeholder="Опишите требования, обязанности и условия работы"' />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                        <p class="text-sm text-gray-500 mt-2">
                            <i class="fas fa-info-circle mr-1"></i>Подробно опишите вакансию, чтобы привлечь подходящих кандидатов
                        </p>
                    </div>
                </div>

                <!-- Кнопки действий -->
                <div class="p-6 bg-gray-50 flex flex-col sm:flex-row justify-between space-y-4 sm:space-y-0">
                    <div class="flex items-center space-x-2">
                        <a href="/vacancies/${vacancy.id}" class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-arrow-left mr-2"></i>Вернуться к просмотру
                        </a>
                    </div>

                    <div class="flex space-x-3">
                        <button type="submit" class="flex items-center justify-center py-2 px-6 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 shadow-sm">
                            <i class="fas fa-save mr-2"></i>Сохранить
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</@main.layout>