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

            <form id="form" action="/resumes/${resume.id}/edit" method="post" class="space-y-8">
                <@spring.formHiddenInput "resume.id"/>
                <@spring.formHiddenInput "resumeForm.applicant.id"/>

                <div class="space-y-6">
                    <h3 class="text-xl font-semibold text-gray-800 border-b pb-2">Основная информация</h3>

                    <div class="mb-4">
                        <label for="name" class="block text-sm font-medium text-gray-700">Название резюме *</label>
                        <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите название резюме"', "text" />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>


                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700 mb-2">Статус резюме</label>

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

                    <div>
                        <label for="category.id" class="block text-sm font-medium text-gray-700">Категория</label>
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

                    <div class="mb-4">
                        <label for="salary" class="block text-sm font-medium text-gray-700">Желаемая зарплата (* в сомах)</label>
                        <@spring.formInput "resumeForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите зарплату"', "number" />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>
                </div>

                <div class="flex items-center justify-between">
                    <a href="/profile" class="text-sm text-blue-600 hover:text-blue-500">
                        Вернуться в профиль
                    </a>
                </div>

                <div class="flex space-x-2">
                    <label for="form" class="block text-sm font-medium text-gray-700">
                        <button type="submit"
                                class="group relative flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            Сохранить изменения
                        </button>
                    </label>
                </div>
            </form>
        </div>
    </div>
</@main.layout>