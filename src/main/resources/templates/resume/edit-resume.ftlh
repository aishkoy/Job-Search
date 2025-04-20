<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <!-- Шапка с названием -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-edit mr-2"></i>Редактирование резюме
                    </h1>
                    <a href="/resumes/${resume.id}"
                       class="bg-white/20 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-white/30 transition font-medium">
                        <i class="fas fa-eye mr-2"></i>Предпросмотр
                    </a>
                </div>
                <p class="mt-2 text-blue-100 text-sm">
                    Внесите необходимые изменения в ваше резюме
                </p>
            </div>

            <form action="/resumes/${resume.id}/edit" method="post" class="space-y-6">
                <@spring.formHiddenInput "resume.id"/>
                <@spring.formHiddenInput "resumeForm.applicant.id"/>

                <!-- Основная информация -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i>Основная информация
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Левая колонка -->
                        <div>
                            <div class="mb-4">
                                <label for="name" class="block text-sm font-semibold text-gray-700">Название резюме
                                    *</label>
                                <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите название резюме"', "text" />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="category.id" class="block text-sm font-semibold text-gray-700">Категория
                                    *</label>
                                <#assign categoryMap = {}>
                                <#list categories as category>
                                    <#assign categoryMap = categoryMap + {category.id?string: category.name}>
                                </#list>

                                <@spring.formSingleSelect
                                "resumeForm.category.id",
                                categoryMap,
                                'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <!-- Правая колонка -->
                        <div>
                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Статус резюме</label>
                                <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
                                    <div class="flex space-x-4">
                                        <@spring.formRadioButtons
                                        "resumeForm.isActive",
                                        {"true":"Активная", "false":"Неактивная"},
                                        "<br>",
                                        "class='h-4 w-4 text-blue-600 border-gray-300 focus:ring-blue-500'"
                                        />
                                        <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="salary" class="block text-sm font-semibold text-gray-700">Желаемая зарплата
                                    (₽)</label>
                                <@spring.formInput "resumeForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите желаемую зарплату"', "number" />
                                <p class="text-xs text-gray-500 mt-1">Оставьте поле пустым, если не хотите указывать
                                    зарплату</p>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Контактная информация -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-address-card text-blue-600 mr-2"></i>Контактная информация
                    </h3>

                    <div class="space-y-4">
                        <!-- Существующие контакты (если есть) -->
                        <#if resume.contacts?? && resume.contacts?size gt 0>
                            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-4">
                                <#list resume.contacts as contact>
                                    <div class="flex items-center p-3 bg-gray-50 rounded-lg border border-gray-100">
                                        <div class="w-10 h-10 mr-3 flex items-center justify-center rounded-lg shadow-sm
                                            <#if contact.contactType.id == 1>
                                                bg-red-100 text-red-600
                                            <#elseif contact.contactType.id == 2>
                                                bg-green-100 text-green-600
                                            <#elseif contact.contactType.id == 3>
                                                bg-blue-100 text-blue-600
                                            <#elseif contact.contactType.id == 4>
                                                bg-purple-100 text-purple-600
                                            <#else>
                                                bg-blue-100 text-blue-600
                                            </#if>">
                                            <#if contact.contactType.id == 1>
                                                <i class="fas fa-envelope"></i>
                                            <#elseif contact.contactType.id == 2>
                                                <i class="fas fa-phone"></i>
                                            <#elseif contact.contactType.id == 3>
                                                <i class="fab fa-telegram"></i>
                                            <#elseif contact.contactType.id == 4>
                                                <i class="fab fa-facebook"></i>
                                            <#else>
                                                <i class="fab fa-linkedin"></i>
                                            </#if>
                                        </div>
                                        <div class="flex-grow">
                                            <div class="text-xs text-gray-500">
                                                <#if contact.contactType.id == 1>
                                                    Email
                                                <#elseif contact.contactType.id == 2>
                                                    Телефон
                                                <#elseif contact.contactType.id == 3>
                                                    Telegram
                                                <#elseif contact.contactType.id == 4>
                                                    Facebook
                                                <#else>
                                                    LinkedIn
                                                </#if>
                                            </div>
                                            <div class="text-gray-800 font-medium">${contact.contactValue}</div>
                                        </div>
                                        <a href="#" class="text-red-500 hover:text-red-600 ml-2"
                                           title="Удалить контакт">
                                            <i class="fas fa-times"></i>
                                        </a>
                                    </div>
                                </#list>
                            </div>
                        </#if>

                        <!-- Кнопка добавления нового контакта -->
                        <div class="flex justify-center">
                            <button type="button"
                                    class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                <i class="fas fa-plus mr-2"></i> Добавить контакт
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Опыт работы -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-briefcase text-blue-600 mr-2"></i>Опыт работы
                    </h3>

                    <div class="space-y-4">
                        <!-- Существующий опыт работы (если есть) -->
                        <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                            <div class="space-y-4 mb-4">
                                <#list resume.workExperiences as exp>
                                    <div class="border-l-2 border-blue-500 pl-4 py-2 pr-2 bg-gray-50 rounded-r-lg">
                                        <div class="flex justify-between items-start">
                                            <div>
                                                <h4 class="font-medium text-gray-800">${exp.position}</h4>
                                                <p class="text-blue-600">${exp.companyName}</p>
                                            </div>
                                            <div class="flex items-center">
                                                <span class="text-sm bg-blue-100 text-blue-700 px-2 py-1 rounded mr-2">
                                                    ${exp.years} ${getPluralForm(exp.years, "год", "года", "лет")}
                                                </span>
                                                <a href="#" class="text-blue-500 hover:text-blue-600 mr-2"
                                                   title="Редактировать">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>
                                                <a href="#" class="text-red-500 hover:text-red-600" title="Удалить">
                                                    <i class="fas fa-trash-alt"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                        </#if>

                        <!-- Кнопка добавления нового опыта работы -->
                        <div class="flex justify-center">
                            <button type="button"
                                    class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                <i class="fas fa-plus mr-2"></i> Добавить опыт работы
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Образование -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-graduation-cap text-blue-600 mr-2"></i>Образование
                    </h3>

                    <div class="space-y-4">
                        <!-- Существующее образование (если есть) -->
                        <#if resume.educations?? && resume.educations?size gt 0>
                            <div class="space-y-4 mb-4">
                                <#list resume.educations as edu>
                                    <div class="border-l-2 border-blue-500 pl-4 py-2 pr-2 bg-gray-50 rounded-r-lg">
                                        <div class="flex justify-between items-start">
                                            <div>
                                                <h4 class="font-medium text-gray-800">${edu.institution}</h4>
                                                <p class="text-blue-600">${edu.program}</p>
                                                <p class="text-gray-600 text-sm">${edu.degree}</p>
                                            </div>
                                            <div class="flex items-center">
                                                <#if edu.endDate??>
                                                    <span class="text-sm bg-blue-100 text-blue-700 px-2 py-1 rounded mr-2">
                                                        <#if edu.startDate??>
                                                            ${edu.startDate} - ${edu.endDate}
                                                        <#else>
                                                            ${edu.endDate}
                                                        </#if>
                                                    </span>
                                                </#if>
                                                <a href="#" class="text-blue-500 hover:text-blue-600 mr-2"
                                                   title="Редактировать">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>
                                                <a href="#" class="text-red-500 hover:text-red-600" title="Удалить">
                                                    <i class="fas fa-trash-alt"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                        </#if>

                        <!-- Кнопка добавления нового образования -->
                        <div class="flex justify-center">
                            <button type="button"
                                    class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                <i class="fas fa-plus mr-2"></i> Добавить образование
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Кнопки действий -->
                <div class="p-6 bg-gray-50 flex flex-col sm:flex-row justify-between space-y-4 sm:space-y-0">
                    <div class="flex items-center space-x-2">
                        <a href="/resumes/${resume.id}"
                           class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-arrow-left mr-2"></i>Вернуться к просмотру
                        </a>
                        <a href="/profile" class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-user mr-2"></i>В профиль
                        </a>
                    </div>

                    <div class="flex space-x-3">
                        <button type="submit"
                                class="flex items-center justify-center py-2 px-6 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 shadow-sm">
                            <i class="fas fa-save mr-2"></i>Сохранить
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <#function getPluralForm number singular dual plural>
        <#assign mod10 = number % 10 />
        <#assign mod100 = number % 100 />

        <#if mod10 == 1 && mod100 != 11>
            <#return singular />
        <#elseif (mod10 >= 2 && mod10 <= 4) && (mod100 < 10 || mod100 >= 20)>
            <#return dual />
        <#else>
            <#return plural />
        </#if>
    </#function>
</@main.layout>