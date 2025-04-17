<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <body class=" bg-gray-100 min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="container mx-auto px-4 max-w-3xl w-full bg-white shadow-md rounded-xl p-8 space-y-8">
        <h2 class="text-center text-3xl font-extrabold text-gray-900">Создать резюме</h2>

        <form action="/resumes/create" method="post" class="space-y-6">
            <@spring.formHiddenInput "resumeForm.applicant"/>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700">Заголовок</label>
                    <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label for="categoryId" class="block text-sm font-medium text-gray-700">ID категории</label>
                    <@spring.formSingleSelect
                    "resumeForm.category",
                    {
                    "1": "IT",
                    "2": "Разработка",
                    "3": "Тестирование"
                    },
                    'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div>
                    <label for="salary" class="block text-sm font-medium text-gray-700">Зарплата</label>
                    <@spring.formInput "resumeForm.salary", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "number"/>
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>
            </div>

            <div class="bg-gray-50 p-6 rounded-lg">
                <h3 class="text-xl font-semibold text-gray-900 mb-6">Опыт работы</h3>

                <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                <#list resumeForm.workExperiences as workExperience>
                    <div class="space-y-4 border-b border-gray-200 pb-6 mb-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Компания</label>
                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].companyName", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700">Должность</label>
                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].position", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Стаж работы (лет)</label>
                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].years", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "number"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700">Обязанности</label>
                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].responsibilities", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>
                    </div>
                </#list>
                </#if>

                <div class="flex justify-end">
                    <button
                            type="submit"
                            name="action"
                            value="addExperience"
                            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                    >
                        Добавить опыт работы
                    </button>
                </div>
            </div>

            <!-- Education Section -->
            <div class="bg-gray-50 p-6 rounded-lg">
                <h3 class="text-xl font-semibold text-gray-900 mb-6">Образование</h3>

                <#if resumeForm.educations?? && resumeForm.educations?has_content>
                <#list resumeForm.educations as education>
                    <div class="space-y-4 border-b border-gray-200 pb-6 mb-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Учебное заведение</label>
                                <@spring.formInput "resumeForm.educations[${education?index}].institution", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700">Программа обучения</label>
                                <@spring.formInput "resumeForm.educations[${education?index}].program", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
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
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-700">Степень</label>
                            <@spring.formInput "resumeForm.educations[${education?index}].degree", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                            <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                        </div>
                    </div>
                </#list>
                </#if>


                <div class="flex justify-end">
                    <button
                            type="submit"
                            name="action"
                            value="addEducation"
                            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                    >
                        Добавить образование
                    </button>
                </div>
            </div>

            <!-- Contacts Section -->
            <div class="bg-gray-50 p-6 rounded-lg">
                <h3 class="text-xl font-semibold text-gray-900 mb-6">Контакты</h3>

                <#if resumeForm.contacts?? && resumeForm.contacts?has_content>
                <#list resumeForm.contacts as contact>
                    <div class="space-y-4 border-b border-gray-200 pb-6 mb-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Тип контакта</label>
                                <@spring.formSingleSelect
                                "resumeForm.contacts[${contact?index}].contactType",
                                {
                                "1": "email",
                                "2": "телефон",
                                "3": "telegram",
                                "4": "facebook",
                                "5": "linkedin"
                                },
                                'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                />
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700">Контакт</label>
                                <@spring.formInput "resumeForm.contacts[${contact?index}].contactValue", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>
                    </div>
                </#list>
                </#if>

                <div class="flex justify-end">
                    <button
                            type="submit"
                            name="action"
                            value="addContact"
                            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                    >
                        Добавить контакт
                    </button>
                </div>
            </div>

            <div class="flex justify-end">
                <button
                        type="submit"
                        name="action"
                        value="save"
                        class="bg-green-600 text-white px-6 py-3 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                >
                    Создать резюме
                </button>
            </div>
        </form>
    </div>
    </body>
</@main.layout>