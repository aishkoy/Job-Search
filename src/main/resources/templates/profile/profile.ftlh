<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль пользователя</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-50">

<div class="bg-white shadow">
    <div class="container mx-auto px-4 py-8">
        <div class="flex flex-col md:flex-row items-center">

            <div class="md:w-1/4 flex justify-center mb-6 md:mb-0">
                <img src="/api/users/${user.id}/avatar" alt="Фото профиля"
                     class="w-40 h-40 rounded-full object-cover border-4 border-white shadow-lg">
            </div>

            <div class="md:w-3/4 md:pl-8">
                <h1 class="text-3xl font-bold text-gray-800">${user.name} ${user.surname!''}</h1>

                <#if user.roleId == 2>
                    <p class="text-gray-600 mt-2">Возраст: ${user.age!'Возраст не указан'}</p>
                </#if>

                <div class="flex flex-wrap gap-4 mt-4">
                    <#if user.email??>
                        <div class="flex items-center text-gray-600">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                 stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                            </svg>
                            ${user.email}
                        </div>
                    </#if>
                    <#if user.phoneNumber??>
                        <div class="flex items-center text-gray-600">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                 stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                            </svg>
                            ${user.phoneNumber}
                        </div>
                    </#if>
                </div>

                <div class="mt-6 flex flex-wrap gap-4">
                    <a href="/profile/edit"
                       class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                        </svg>
                        Редактировать профиль
                    </a>

                    <a href="/vacancies"
                       class="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                        </svg>
                        Все вакансии
                    </a>
                    <#if user.roleId == 2>
                        <a href="#"
                           class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition flex items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                 stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                            </svg>
                            Создать резюме
                        </a>
                    <#else>
                        <a href="#"
                           class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition flex items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                 stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                            </svg>
                            Создать вакансию
                        </a>

                        <a href="/resumes"
                           class="px-4 py-2 bg-teal-600 text-white rounded-md hover:bg-teal-700 transition flex items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                 stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                            </svg>
                            Все резюме
                        </a>
                    </#if>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="container mx-auto px-4 py-8">
    <#if user.roleId == 2>
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Мои резюме</h2>

        <#if resumes?? && resumes?size gt 0>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <#list resumes as resume>
                    <div class="bg-white rounded-lg shadow hover:shadow-lg transition transform hover:-translate-y-1 ${resume.isActive?string('', 'opacity-60')}">
                        <div class="p-6">
                            <div class="flex justify-between items-start mb-4">
                                <h3 class="text-xl font-semibold text-gray-800">${resume.name}</h3>
                                <span class="px-2 py-1 rounded-full text-xs font-medium ${resume.isActive?string('bg-green-100 text-green-800', 'bg-gray-100 text-gray-800')}">
                                        ${resume.isActive?string('Активно', 'Не активно')}
                                    </span>
                            </div>

                            <#if categories?? && categories[resume.categoryId?string]??>
                                <p class="text-gray-600 flex items-center mb-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                         viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                                    </svg>
                                    ${categories[resume.categoryId?string]}
                                </p>
                            <#else>
                                <p class="text-gray-600 flex items-center mb-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                         viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                                    </svg>
                                    Категория не указана
                                </p>
                            </#if>

                            <p class="text-gray-600 flex items-center mb-4">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                <#if resume.salary??>
                                    ${resume.salary} руб.
                                <#else>
                                    Зарплата не указана
                                </#if>
                            </p>

                            <p class="text-sm text-gray-500">
                                Создано: ${resume.createdDate?string("dd.MM.yyyy")}
                            </p>

                            <div class="mt-4 flex justify-between">
                                <a href="/resumes/${resume.id}" class="text-blue-600 hover:text-blue-800 font-medium">Подробнее</a>

                                <div class="flex gap-2">
                                    <a href="/resumes/edit/${resume.id}"
                                       class="text-yellow-600 hover:text-yellow-800">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                        </svg>
                                    </a>
                                    <a href="/resumes/delete/${resume.id}" class="text-red-600 hover:text-red-800">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </a>
                                </div>

                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="bg-white rounded-lg shadow p-6 text-center">
                <p class="text-gray-600 mb-4">У вас пока нет резюме.</p>
                <a href="/resumes/create"
                   class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition">Создать резюме</a>
            </div>
        </#if>
    <#else>
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Мои вакансии</h2>

        <#if vacancies?? && vacancies?size gt 0>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <#list vacancies as vacancy>
                    <div class="bg-white rounded-lg shadow hover:shadow-lg transition transform hover:-translate-y-1 ${vacancy.isActive?string('', 'opacity-60')}">
                        <div class="p-6">
                            <div class="flex justify-between items-start mb-4">
                                <h3 class="text-xl font-semibold text-gray-800">${vacancy.name}</h3>
                                <span class="px-2 py-1 rounded-full text-xs font-medium ${vacancy.isActive?string('bg-green-100 text-green-800', 'bg-gray-100 text-gray-800')}">
                                        ${vacancy.isActive?string('Активно', 'Не активно')}
                                    </span>
                            </div>

                            <p class="text-gray-600 flex items-center mb-2">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                <#if vacancy.salary??>
                                    ${vacancy.salary} руб.
                                <#else>
                                    Зарплата не указана
                                </#if>
                            </p>

                            <p class="text-gray-600 flex items-center mb-4">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none"
                                     viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                                </svg>
                                Опыт в годах:
                                <#if vacancy.expFrom?? && vacancy.expTo??>
                                    от ${vacancy.expFrom} до ${vacancy.expTo}
                                </#if>
                            </p>

                            <p class="text-sm text-gray-500">
                                Создано: ${vacancy.createdDate?string("dd.MM.yyyy")}
                            </p>

                            <div class="mt-4 flex justify-between">
                                <a href="/vacancies/${vacancy.id}" class="text-blue-600 hover:text-blue-800 font-medium">Подробнее</a>

                                <div class="flex gap-2">
                                    <a href="/vacancies/edit/${vacancy.id}"
                                       class="text-yellow-600 hover:text-yellow-800">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                        </svg>
                                    </a>
                                    <a href="/vacancies/delete/${vacancy.id}" class="text-red-600 hover:text-red-800">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="bg-white rounded-lg shadow p-6 text-center">
                <p class="text-gray-600 mb-4">У вас пока нет вакансий.</p>
                <a href="/vacancy/create"
                   class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition">Создать вакансию</a>
            </div>
        </#if>
    </#if>
</div>
</body>
</html>