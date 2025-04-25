<#import "../layout.ftlh" as main>
<@main.layout>
    <#assign role = user.role.id />
    <#assign known = SPRING_SECURITY_CONTEXT?? >
    <#if known>
        <#assign
        currentUserId = SPRING_SECURITY_CONTEXT.authentication.principal.getUserId()
        >
    </#if>
    <!-- Информация о пользователе -->
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-md overflow-hidden mb-6">
            <div class="bg-blue-600 text-white p-4">
                <h1 class="text-2xl font-bold flex items-center">
                    <i class="fas fa-user-circle mr-2"></i>Личный кабинет
                </h1>
            </div>

            <div class="p-6 grid grid-cols-1 md:grid-cols-4 gap-6">
                <div class="flex flex-col items-center">
                    <div class="relative">
                        <img src="/api/users/${user.id}/avatar" alt="Фото профиля"
                             class="w-32 h-32 rounded-full object-cover border-4 border-white shadow-lg">
                        <div class="absolute bottom-0 right-0">
                            <form method="POST" action="${url}/${user.id}/avatar" enctype="multipart/form-data">
                                <@main.csrf/>
                                <label for="avatar-upload"
                                       class="bg-blue-600 text-white rounded-full w-8 h-8 flex items-center justify-center hover:bg-blue-700 transition shadow-lg cursor-pointer">
                                    <i class="fas fa-camera"></i>
                                </label>
                                <input id="avatar-upload" name="file" type="file" accept="image/jpeg, image/png"
                                       class="hidden"
                                       onchange="this.form.submit()">
                            </form>
                        </div>
                    </div>
                    <p class="mt-4 text-sm text-gray-500 text-center">Нажмите на иконку,<br>чтобы обновить фото</p>
                </div>

                <!-- Информация о пользователе занимает 3 колонки сетки -->
                <div class="md:col-span-3">
                    <div class="flex flex-col md:flex-row justify-between items-start mb-4">
                        <div>
                            <h2 class="text-2xl font-bold text-gray-800">${user.name} ${user.surname!''}</h2>
                            <p class="text-blue-600 font-medium mt-1">

                                <#if role == 2>
                                    <i class="fas fa-user mr-1"></i> Соискатель
                                <#else>
                                    <i class="fas fa-building mr-1"></i> Работодатель
                                </#if>
                            </p>
                        </div>

                        <#if currentUserId?? && currentUserId == user.id>
                            <a href="/${url}/${user.id}/edit"
                               class="mt-2 md:mt-0 bg-blue-600 text-white rounded-md px-4 py-2 hover:bg-blue-700 transition flex items-center">
                                <i class="fas fa-edit mr-2"></i>Редактировать профиль
                            </a>
                        </#if>
                    </div>

                    <!-- Контактные данные с фиксированной шириной карточек -->
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <#if role == 2>
                            <div class="bg-gray-50 rounded-lg p-3 flex items-center">
                                <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                                    <i class="fas fa-birthday-cake"></i>
                                </div>
                                <div class="min-w-0">
                                    <div class="text-xs text-gray-500">Возраст</div>
                                    <div class="text-gray-800 font-medium truncate">${user.age!'Не указан'}</div>
                                </div>
                            </div>
                        </#if>

                        <div class="bg-gray-50 rounded-lg p-3 flex items-center">
                            <div class="w-10 h-10 bg-red-100 text-red-600 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                                <i class="fas fa-envelope"></i>
                            </div>
                            <div class="min-w-0">
                                <div class="text-xs text-gray-500">Email</div>
                                <div class="text-gray-800 font-medium truncate">${user.email}</div>
                            </div>
                        </div>


                        <div class="bg-gray-50 rounded-lg p-3 flex items-center">
                            <div class="w-10 h-10 bg-green-100 text-green-600 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                                <i class="fas fa-phone"></i>
                            </div>
                            <div class="min-w-0">
                                <div class="text-xs text-gray-500">Телефон</div>
                                <div class="text-gray-800 font-medium truncate">${user.phoneNumber}</div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <!-- Резюме или вакансии с улучшенной сеткой карточек -->
        <div class="mb-6">
            <#if role == 2>
                <div class="flex justify-between items-center mb-4 bg-white p-4 rounded-lg shadow-sm">
                    <h2 class="text-xl font-bold text-gray-800 flex items-center">
                        <i class="fas fa-file-alt text-blue-600 mr-2"></i>
                        <#if currentUserId?? && currentUserId == user.id>Мои резюме
                    </h2>
                    <a href="/resumes/create" class="text-blue-600 hover:text-blue-800 flex items-center">
                        <i class="fas fa-plus mr-1"></i>Создать новое
                    </a>
                    </#if>
                </div>

                <#if resumesPage?? && resumesPage.content?size gt 0>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <#list resumesPage.content as resume>
                            <div class="bg-white rounded-lg shadow-sm hover:shadow-md transition ${resume.isActive?string('', 'opacity-70')}">
                                <div class="p-5">
                                    <div class="flex justify-between items-start mb-3">
                                        <h3 class="text-lg font-semibold text-gray-800 truncate"
                                            title="${resume.name}">${resume.name}</h3>
                                        <span class="px-2 py-1 rounded-full text-xs font-medium ${resume.isActive?string('bg-green-100 text-green-800', 'bg-gray-100 text-gray-800')}">
                                            ${resume.isActive?string('Активно', 'Не активно')}
                                        </span>
                                    </div>

                                    <div class="space-y-2 mb-3">
                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-money-bill-wave text-blue-500 mr-2 w-5"></i>
                                            <#if resume.salary?? && resume.salary gt 0>
                                                ${resume.salary?string(",##0")} с
                                            <#else>
                                                <span class="text-gray-500">Зарплата не указана</span>
                                            </#if>
                                        </p>

                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-tag text-blue-500 mr-2 w-5"></i>
                                            <span class="truncate"
                                                  title="${resume.category.name}">${resume.category.name}</span>
                                        </p>

                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-clock text-blue-500 mr-2 w-5"></i>
                                            <span class="text-gray-500 text-sm">
                                                Создано: ${resume.createdAt?string("dd.MM.yyyy")}
                                            </span>
                                        </p>
                                    </div>

                                    <div class="mt-3 pt-3 border-t border-gray-100 flex justify-between items-center">
                                        <a href="/resumes/${resume.id}"
                                           class="text-blue-600 hover:text-blue-800 font-medium">
                                            <i class="fas fa-eye mr-1"></i>Просмотр
                                        </a>

                                        <#if  currentUserId?? && currentUserId == user.id>
                                            <div class="flex gap-3">
                                                <a href="/resumes/${resume.id}/edit"
                                                   class="text-yellow-600 hover:text-yellow-800" title="Редактировать">
                                                    <i class="fas fa-edit"></i>
                                                </a>

                                                <form action="/resumes/${resume.id}/delete" method="post" onsubmit="return confirm('Вы уверены, что хотите удалить это резюме?');" class="inline">
                                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                    <button type="submit" class="text-red-600 hover:text-red-800" title="Удалить">
                                                        <i class="fas fa-trash-alt"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>

                    <!-- Пагинация для резюме -->
                    <#if resumesPage.totalPages gt 1>
                        <div class="flex justify-center items-center mt-6 gap-2">
                            <!-- Информация о пагинации -->
                            <div class="text-gray-500 text-sm mr-4">
                                Страница ${resumesPage.number + 1} из ${resumesPage.totalPages}
                            </div>

                            <!-- Кнопка "Предыдущая страница" -->
                            <#if resumesPage.number gt 0>
                                <a href="?page=${resumesPage.number}<#if size??>&size=${size}</#if>"
                                   class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                                    <i class="fas fa-chevron-left mr-1"></i> Назад
                                </a>
                            <#else>
                                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                                    <i class="fas fa-chevron-left mr-1"></i> Назад
                                </span>
                            </#if>

                            <!-- Номера страниц -->
                            <div class="flex space-x-1">
                                <#assign startPage = ((resumesPage.number - 2) gt 0)?then(resumesPage.number - 2, 0)>
                                <#assign endPage = ((startPage + 4) lt resumesPage.totalPages)?then(startPage + 4, resumesPage.totalPages - 1)>
                                <#if startPage gt 0>
                                    <a href="?page=1<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                        1
                                    </a>
                                    <#if startPage gt 1>
                                        <span class="px-2 py-2">...</span>
                                    </#if>
                                </#if>

                                <#list startPage..endPage as i>
                                    <a href="?page=${i + 1}<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded ${(i == resumesPage.number)?then('bg-blue-600 text-white', 'hover:bg-gray-100')} transition-colors">
                                        ${i + 1}
                                    </a>
                                </#list>

                                <#if endPage lt resumesPage.totalPages - 1>
                                    <#if endPage lt resumesPage.totalPages - 2>
                                        <span class="px-2 py-2">...</span>
                                    </#if>
                                    <a href="?page=${resumesPage.totalPages}<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                        ${resumesPage.totalPages}
                                    </a>
                                </#if>
                            </div>

                            <!-- Кнопка "Следующая страница" -->
                            <#if resumesPage.number lt resumesPage.totalPages - 1>
                                <a href="?page=${resumesPage.number + 2}<#if size??>&size=${size}</#if>"
                                   class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                                    Вперед <i class="fas fa-chevron-right ml-1"></i>
                                </a>
                            <#else>
                                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                                    Вперед <i class="fas fa-chevron-right ml-1"></i>
                                </span>
                            </#if>
                        </div>
                    </#if>
                <#else>
                    <div class="bg-white rounded-lg shadow-sm p-6 text-center">
                        <div class="w-16 h-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
                            <i class="fas fa-file-alt text-2xl"></i>
                        </div>
                        <h3 class="text-lg font-medium text-gray-800 mb-2">
                            <#if currentUserId?? && currentUserId == user.id>
                            У вас пока нет резюме</h3>
                        <p class="text-gray-600 mb-4">Создайте ваше первое резюме, чтобы найти работу</p>

                        <a href="/resumes/create"
                           class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition inline-flex items-center">
                            <i class="fas fa-plus mr-2"></i>Создать резюме
                        </a>
                        </#if>
                    </div>
                </#if>
            <#else>
                <div class="flex justify-between items-center mb-4 bg-white p-4 rounded-lg shadow-sm">
                    <h2 class="text-xl font-bold text-gray-800 flex items-center">
                        <#if currentUserId?? && currentUserId == user.id>
                        <i class="fas fa-briefcase text-blue-600 mr-2"></i>Мои вакансии
                    </h2>

                    <a href="/vacancies/create" class="text-blue-600 hover:text-blue-800 flex items-center">
                        <i class="fas fa-plus mr-1"></i>Создать новую
                    </a>
                    </#if>
                </div>

                <#if vacanciesPage?? && vacanciesPage.content?size gt 0>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <#list vacanciesPage.content as vacancy>
                            <div class="bg-white rounded-lg shadow-sm hover:shadow-md transition ${vacancy.isActive?string('', 'opacity-70')}">
                                <div class="p-5">
                                    <div class="flex justify-between items-start mb-3">
                                        <h3 class="text-lg font-semibold text-gray-800 truncate"
                                            title="${vacancy.name}">${vacancy.name}</h3>
                                        <span class="px-2 py-1 rounded-full text-xs font-medium ${vacancy.isActive?string('bg-green-100 text-green-800', 'bg-gray-100 text-gray-800')}">
                                            ${vacancy.isActive?string('Активна', 'Не активна')}
                                        </span>
                                    </div>

                                    <div class="space-y-2 mb-3">
                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-money-bill-wave text-blue-500 mr-2 w-5"></i>
                                            <#if vacancy.salary?? && vacancy.salary gt 0>
                                                ${vacancy.salary?string(",##0")} ₽
                                            <#else>
                                                <span class="text-gray-500">Зарплата не указана</span>
                                            </#if>
                                        </p>

                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-briefcase text-blue-500 mr-2 w-5"></i>
                                            <span>Опыт: ${vacancy.expFrom} - ${vacancy.expTo} ${getPluralForm(vacancy.expTo, "год", "года", "лет")}</span>
                                        </p>

                                        <p class="text-gray-600 flex items-center text-sm">
                                            <i class="fas fa-clock text-blue-500 mr-2 w-5"></i>
                                            <span class="text-gray-500 text-sm">
                                                Создана: ${vacancy.createdAt?string("dd.MM.yyyy")}
                                            </span>
                                        </p>
                                    </div>

                                    <div class="mt-3 pt-3 border-t border-gray-100 flex justify-between items-center">
                                        <a href="/vacancies/${vacancy.id}"
                                           class="text-blue-600 hover:text-blue-800 font-medium">
                                            <i class="fas fa-eye mr-1"></i>Просмотр
                                        </a>

                                        <#if currentUserId?? && currentUserId == user.id>
                                            <div class="flex gap-3">
                                                <a href="/vacancies/${vacancy.id}/edit"
                                                   class="text-yellow-600 hover:text-yellow-800" title="Редактировать">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <form action="/vacancies/${vacancy.id}/delete" method="post" onsubmit="return confirm('Вы уверены, что хотите удалить эту вакансию?');" class="inline">
                                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                    <button type="submit" class="text-red-600 hover:text-red-800" title="Удалить">
                                                        <i class="fas fa-trash-alt"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>

                    <!-- Пагинация для вакансий -->
                    <#if vacanciesPage.totalPages gt 1>
                        <div class="flex justify-center items-center mt-6 gap-2">
                            <!-- Информация о пагинации -->
                            <div class="text-gray-500 text-sm mr-4">
                                Страница ${vacanciesPage.number + 1} из ${vacanciesPage.totalPages}
                            </div>

                            <!-- Кнопка "Предыдущая страница" -->
                            <#if vacanciesPage.number gt 0>
                                <a href="?page=${vacanciesPage.number}<#if size??>&size=${size}</#if>"
                                   class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                                    <i class="fas fa-chevron-left mr-1"></i> Назад
                                </a>
                            <#else>
                                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                                    <i class="fas fa-chevron-left mr-1"></i> Назад
                                </span>
                            </#if>

                            <!-- Номера страниц -->
                            <div class="flex space-x-1">
                                <#assign startPage = ((vacanciesPage.number - 2) gt 0)?then(vacanciesPage.number - 2, 0)>
                                <#assign endPage = ((startPage + 4) lt vacanciesPage.totalPages)?then(startPage + 4, vacanciesPage.totalPages - 1)>
                                <#if startPage gt 0>
                                    <a href="?page=1<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                        1
                                    </a>
                                    <#if startPage gt 1>
                                        <span class="px-2 py-2">...</span>
                                    </#if>
                                </#if>

                                <#list startPage..endPage as i>
                                    <a href="?page=${i + 1}<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded ${(i == vacanciesPage.number)?then('bg-blue-600 text-white', 'hover:bg-gray-100')} transition-colors">
                                        ${i + 1}
                                    </a>
                                </#list>

                                <#if endPage lt vacanciesPage.totalPages - 1>
                                    <#if endPage lt vacanciesPage.totalPages - 2>
                                        <span class="px-2 py-2">...</span>
                                    </#if>
                                    <a href="?page=${vacanciesPage.totalPages}<#if size??>&size=${size}</#if>"
                                       class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                        ${vacanciesPage.totalPages}
                                    </a>
                                </#if>
                            </div>

                            <!-- Кнопка "Следующая страница" -->
                            <#if vacanciesPage.number lt vacanciesPage.totalPages - 1>
                                <a href="?page=${vacanciesPage.number + 2}<#if size??>&size=${size}</#if>"
                                   class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                                    Вперед <i class="fas fa-chevron-right ml-1"></i>
                                </a>
                            <#else>
                                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                                    Вперед <i class="fas fa-chevron-right ml-1"></i>
                                </span>
                            </#if>
                        </div>
                    </#if>
                <#else>
                    <div class="bg-white rounded-lg shadow-sm p-6 text-center">
                        <div class="w-16 h-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
                            <i class="fas fa-briefcase text-2xl"></i>
                        </div>
                        <h3 class="text-lg font-medium text-gray-800 mb-2">
                            <#if currentUserId?? && currentUserId == user.id>
                            У вас пока нет вакансий</h3>
                        <p class="text-gray-600 mb-4">Создайте вашу первую вакансию, чтобы найти сотрудников</p>
                        <a href="/vacancies/create"
                           class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition inline-flex items-center">
                            <i class="fas fa-plus mr-2"></i>Создать вакансию
                        </a>
                        </#if>
                    </div>
                </#if>
            </#if>
        </div>

        <!-- Дополнительные блоки информации -->
        <div class="bg-white rounded-lg shadow-sm p-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                <i class="fas fa-chart-line text-blue-600 mr-2"></i>Статистика
            </h3>

            <div class="space-y-4">
                <#--                TODO мб когда-нибудь сделаю -->
                <#if role == 2>
                    <div class="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                        <div class="flex items-center">
                            <div class="w-10 h-10 rounded-full bg-green-100 text-green-600 flex items-center justify-center flex-shrink-0 mr-3">
                                <i class="fas fa-envelope-open"></i>
                            </div>
                            <span class="text-gray-700">Приглашения</span>
                        </div>
                        <span class="text-xl font-bold text-gray-800">0</span>
                    </div>
                <#else>
                    <div class="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                        <div class="flex items-center">
                            <div class="w-10 h-10 rounded-full bg-green-100 text-green-600 flex items-center justify-center flex-shrink-0 mr-3">
                                <i class="fas fa-paper-plane"></i>
                            </div>
                            <span class="text-gray-700">Отклики на вакансии</span>
                        </div>
                        <span class="text-xl font-bold text-gray-800">0</span>
                    </div>
                </#if>
            </div>
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