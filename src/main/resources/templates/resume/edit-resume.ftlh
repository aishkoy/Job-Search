<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-md">
            <div>
                <h2 class="text-center text-3xl font-extrabold text-gray-900 mb-4">
                    Редактирование резюме
                </h2>
                <p class="text-center text-sm text-gray-600 mb-8">
                    Внесите необходимые изменения в ваше резюме
                </p>
            </div>

            <form action="/resumes/${resume.id}/edit" method="post" class="space-y-8">
                <@spring.formHiddenInput "resume.id"/>
                <@spring.formHiddenInput "resumeForm.applicantId"/>

                <div class="space-y-6">
                    <h3 class="text-xl font-semibold text-gray-800 border-b pb-2">Основная информация</h3>

                    <div class="mb-4">
                        <label for="name" class="block text-sm font-medium text-gray-700">Название резюме *</label>
                        <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите название резюме"', "text" />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>

                    <div>
                        <label for="categoryId" class="block text-sm font-medium text-gray-700">Категория</label>
                        <@spring.formSingleSelect
                        "resumeForm.categoryId",
                        {
                        "1": "IT",
                        "2": "Разработка",
                        "3": "Тестирование"
                        },
                        'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                        />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>

                    <div class="mb-4">
                        <label for="salary" class="block text-sm font-medium text-gray-700">Желаемая зарплата (в USD)
                            *</label>
                        <@spring.formInput "resumeForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите зарплату"', "number" />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>
                </div>

                <!-- Образование -->
                <div class="space-y-6">
                    <h3 class="text-xl font-semibold text-gray-800 border-b pb-2">Образование</h3>

                    <#if resumeForm.educations?? && resumeForm.educations?size gt 0>
                        <#list resumeForm.educations as education>
                            <div class="education-item border rounded-md p-4 mb-4">
                                <@spring.formHiddenInput "resumeForm.educations[${education_index}].id"/>
                                <@spring.formHiddenInput "resumeForm.educations[${education_index}].resumeId"/>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-2">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Учебное заведение
                                            *</label>
                                        <@spring.formInput "resumeForm.educations[${education_index}].institution", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Название учебного заведения"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Специальность *</label>
                                        <@spring.formInput "resumeForm.educations[${education_index}].program", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Ваша специальность"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Степень *</label>
                                        <@spring.formInput "resumeForm.educations[${education_index}].degree", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Ваша степень"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-2">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Дата начала *</label>
                                        <@spring.formInput "resumeForm.educations[${education_index}].startDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Дата окончания</label>
                                        <@spring.formInput "resumeForm.educations[${education_index}].endDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>

                                <button type="submit" name="action" value="removeEducation-${education_index}"
                                        class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                                    Удалить
                                </button>
                            </div>
                        </#list>
                    </#if>

                    <button type="submit" name="action" value="addEducation"
                            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-blue-700 bg-blue-100 hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        + Добавить образование
                    </button>
                </div>

                <!-- Опыт работы -->
                <div class="space-y-6">
                    <h3 class="text-xl font-semibold text-gray-800 border-b pb-2">Опыт работы</h3>

                    <#if resumeForm.workExperiences?? && resumeForm.workExperiences?size gt 0>
                        <#list resumeForm.workExperiences as workExperience>
                            <div class="work-experience-item border rounded-md p-4 mb-4">
                                <@spring.formHiddenInput "resumeForm.workExperiences[${workExperience_index}].id"/>
                                <@spring.formHiddenInput "resumeForm.workExperiences[${workExperience_index}].resumeId"/>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-2">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Компания *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExperience_index}].companyName", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Название компании"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Должность *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExperience_index}].position", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Ваша должность"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-2">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Продолжительность работы
                                            (в годах) *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExperience_index}].years", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Количество лет"', "number" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>

                                <div class="mb-2">
                                    <label class="block text-sm font-medium text-gray-700">Обязанности</label>
                                    <@spring.formTextarea "resumeForm.workExperiences[${workExperience_index}].responsibilities", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="3" placeholder="Опишите ваши обязанности"' />
                                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                </div>

                                <button type="submit" name="action" value="removeExperience-${workExperience_index}"
                                        class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                                    Удалить
                                </button>
                            </div>
                        </#list>
                    </#if>

                    <button type="submit" name="action" value="addExperience"
                            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-blue-700 bg-blue-100 hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        + Добавить опыт работы
                    </button>
                </div>

                <!-- Контактная информация -->
                <div class="space-y-6">
                    <h3 class="text-xl font-semibold text-gray-800 border-b pb-2">Контактная информация</h3>

                    <#if resumeForm.contacts?? && resumeForm.contacts?size gt 0>
                        <#list resumeForm.contacts as contact>
                            <div class="contact-item border rounded-md p-4 mb-4">
                                <@spring.formHiddenInput "resumeForm.contacts[${contact_index}].id"/>
                                <@spring.formHiddenInput "resumeForm.contacts[${contact_index}].resumeId"/>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-2">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Тип контакта *</label>
                                        <@spring.formSingleSelect
                                        "resumeForm.contacts[${contact_index}].typeId",
                                        {
                                        "1": "email",
                                        "2": "телефон",
                                        "3": "telegram",
                                        "4": "facebook",
                                        "5": "linkedin"
                                        },
                                        'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                        />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>

                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Контактные данные *</label>
                                        <@spring.formInput "resumeForm.contacts[${contact_index}].contactValue", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите контактные данные"', "text" />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>

                                <button type="submit" name="action" value="removeContact-${contact_index}"
                                        class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                                    Удалить
                                </button>
                            </div>
                        </#list>
                    </#if>

                    <button type="submit" name="action" value="addContact"
                            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-blue-700 bg-blue-100 hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        + Добавить контактные данные
                    </button>
                </div>

                <div class="flex items-center justify-between">
                    <a href="/profile" class="text-sm text-blue-600 hover:text-blue-500">
                        Вернуться в профиль
                    </a>
                    <div class="flex space-x-2">
                        <button type="submit" name="action" value="save"
                                class="group relative flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            Сохранить изменения
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</@main.layout>