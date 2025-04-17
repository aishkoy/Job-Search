<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 bg-white p-6 rounded-xl shadow-md">
            <div>
                <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    Создание новой вакансии
                </h2>
                <p class="mt-2 text-center text-sm text-gray-600">
                    Заполните все необходимые поля
                </p>
            </div>

            <form action="/vacancies/create" method="post" class="mt-8 space-y-6">
                <input type="hidden" name="authorId" value="${user.id}"/>

                <div class="rounded-md -space-y-px">
                    <div class="mb-4">
                        <label for="name" class="block text-sm font-medium text-gray-700">Название вакансии *</label>
                        <@spring.formInput "vacancyForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Введите название вакансии"' />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>

                    <div class="mb-4">
                        <label for="description" class="block text-sm font-medium text-gray-700">Описание
                            вакансии</label>
                        <@spring.formTextarea "vacancyForm.description", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="5" placeholder="Опишите требования и обязанности"' />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>

                    <div class="mb-4">
                        <label for="categoryId" class="block text-sm font-medium text-gray-700">Категория *</label>
                        <@spring.formSingleSelect
                        "vacancyForm.category",
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
                        <label for="salary" class="block text-sm font-medium text-gray-700">Зарплата (в USD) *</label>
                        <@spring.formInput "vacancyForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="Укажите зарплату"' />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                    </div>

                    <div class="mb-4 grid grid-cols-2 gap-4">
                        <div>
                            <label for="expFrom" class="block text-sm font-medium text-gray-700">Мин. опыт (лет)
                                *</label>
                            <@spring.formInput "vacancyForm.expFrom", 'type="number" min="1" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="От"' />
                            <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                        </div>

                        <div>
                            <label for="expTo" class="block text-sm font-medium text-gray-700">Макс. опыт (лет)
                                *</label>
                            <@spring.formInput "vacancyForm.expTo", 'type="number" min="1" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" placeholder="До"' />
                            <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                        </div>
                    </div>
                </div>

                <div class="flex items-center justify-between">
                    <a href="/vacancies" class="text-sm text-blue-600 hover:text-blue-500">
                        Вернуться к списку вакансий
                    </a>
                    <button type="submit"
                            class="group relative flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        Создать вакансию
                    </button>
                </div>
            </form>
        </div>
    </div>
</@main.layout>