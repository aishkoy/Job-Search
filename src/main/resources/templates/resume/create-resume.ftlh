<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-plus-circle mr-2"></i>Создание резюме
                    </h1>
                </div>
                <p class="mt-2 text-blue-100 text-sm">
                    Заполните информацию о себе, чтобы найти подходящую работу
                </p>
            </div>

            <form action="/resumes/create" method="post" class="space-y-6">
                <@spring.formHiddenInput "resumeForm.applicant.id"/>

                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i>Основная информация
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <div class="mb-4">
                                <label for="name" class="block text-sm font-semibold text-gray-700">
                                    Заголовок резюме <span class="text-red-500">*</span>
                                </label>
                                <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Например: Frontend-разработчик"', "text"/>
                                <p class="text-xs text-gray-500 mt-1">Это название будет отображаться в результатах поиска</p>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="category.id" class="block text-sm font-semibold text-gray-700">
                                    Категория <span class="text-red-500">*</span>
                                </label>
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

                        <div>
                            <div class="mb-4">
                                <label for="salary" class="block text-sm font-semibold text-gray-700">
                                    Ожидаемая зарплата (с)
                                </label>
                                <@spring.formInput "resumeForm.salary", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите желаемую зарплату" type="number" min="0"', "number"/>
                                <p class="text-xs text-gray-500 mt-1">Оставьте поле пустым, если не хотите указывать зарплату</p>
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

                    <#if resumeForm.contacts?? && resumeForm.contacts?has_content>
                        <div class="space-y-4 mb-6">
                            <#list resumeForm.contacts as contact>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800">Контакт #${contact?index + 1}</h4>
                                        <div>
                                            <button type="submit" name="action" value="removeContact${contact?index}"
                                                    class="text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div>
                                            <label class="block text-sm font-medium text-gray-700">Тип контакта</label>
                                            <#assign contactTypeMap = {}>
                                            <#list contactTypes as contactType>
                                                <#assign contactTypeMap = contactTypeMap + {contactType.id?string: contactType.type}>
                                            </#list>

                                            <@spring.formSingleSelect
                                            "resumeForm.contacts[${contact?index}].contactType.id",
                                            contactTypeMap,
                                            'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                            />
                                        </div>

                                        <div>
                                            <label class="block text-sm font-medium text-gray-700">Значение</label>
                                            <@spring.formInput "resumeForm.contacts[${contact?index}].contactValue", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите контактные данные"', "text"/>
                                            <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="submit" name="action" value="addContact"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> Добавить контакт
                        </button>
                    </div>
                </div>

                <!-- Опыт работы -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-briefcase text-blue-600 mr-2"></i>Опыт работы
                    </h3>

                    <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                        <div class="space-y-4 mb-6">
                            <#list resumeForm.workExperiences as workExperience>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800">Место работы #${workExperience?index + 1}</h4>
                                        <div>
                                            <button type="submit" name="action" value="removeExperience${workExperience?index}"
                                                    class="text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>

                                    <div class="space-y-4">
                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Компания</label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].companyName", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Название компании"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Должность</label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].position", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Ваша должность"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>

                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Стаж работы (лет)</label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].years", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" type="number" min="0" placeholder="Количество лет"', "number"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Обязанности</label>
                                                <@spring.formTextarea "resumeForm.workExperiences[${workExperience?index}].responsibilities", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="2" placeholder="Опишите ваши обязанности и достижения"'/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="submit" name="action" value="addExperience"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> Добавить опыт работы
                        </button>
                    </div>
                </div>

                <!-- Образование -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-graduation-cap text-blue-600 mr-2"></i>Образование
                    </h3>

                    <#if resumeForm.educations?? && resumeForm.educations?has_content>
                        <div class="space-y-4 mb-6">
                            <#list resumeForm.educations as education>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800">Образование #${education?index + 1}</h4>
                                        <div>
                                            <button type="submit" name="action" value="removeEducation${education?index}"
                                                    class="text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>

                                    <div class="space-y-4">
                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Учебное заведение</label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].institution", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Название учебного заведения"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Программа обучения</label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].program", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Специальность/программа"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>

                                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Дата начала</label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].startDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Дата окончания</label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].endDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700">Степень</label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].degree", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Бакалавр, Магистр и т.д."', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="submit" name="action" value="addEducation"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> Добавить образование
                        </button>
                    </div>
                </div>

                <!-- Кнопки действий -->
                <div class="p-6 bg-gray-50 flex flex-col sm:flex-row justify-between space-y-4 sm:space-y-0">
                    <div class="flex items-center space-x-2">
                        <a href="/profile" class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-arrow-left mr-2"></i>Вернуться в профиль
                        </a>
                    </div>

                    <div class="flex space-x-3">
                        <button type="submit" name="action" value="save"
                                class="flex items-center justify-center py-2 px-6 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 shadow-sm">
                            <i class="fas fa-check mr-2"></i>Создать резюме
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</@main.layout>