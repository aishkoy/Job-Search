<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="bg-blue-600 text-white py-8">
        <div class="container mx-auto px-4">
            <div class="flex items-center justify-between">
                <h1 class="text-2xl font-bold">${vacancy.name}</h1>
                <div class="flex space-x-3">
                    <#if currentUser?? && currentUser.id == vacancy.authorId>
                        <a href="/vacancies/${vacancy.id}/edit"
                           class="bg-white text-blue-600 px-4 py-2 rounded-md hover:bg-blue-50 transition font-medium">
                            <i class="fas fa-edit mr-2"></i>Редактировать
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="p-6 border-b">
                <div class="flex flex-col md:flex-row justify-between">
                    <div>
                        <h2 class="text-xl font-bold text-gray-800">${vacancy.authorName!'Компания'}</h2>
                        <div class="mt-2 text-lg font-medium text-blue-600">
                            <#if vacancy.salary?? && vacancy.salary gt 0>
                                ${vacancy.salary?string(",##0")} ₽
                            <#else>
                                Зарплата не указана
                            </#if>
                        </div>

                        <div class="mt-4 flex flex-wrap gap-2">
                            <#if vacancy.isActive?? && vacancy.isActive>
                                <span class="bg-green-100 text-green-700 text-sm px-3 py-1 rounded-full">Активная вакансия</span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">Вакансия закрыта</span>
                            </#if>

                            <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                Опыт: ${vacancy.expFrom} - ${vacancy.expTo} ${getPluralForm(vacancy.expTo, "год", "года", "лет")}
                            </span>
                        </div>
                    </div>

                    <div class="mt-4 md:mt-0 text-right">
                        <div class="text-sm text-gray-500">
                            <#if vacancy.updateTime??>
                                Обновлено: ${vacancy.updateTime?datetime?string("dd.MM.yyyy")}
                            <#elseif vacancy.createdDate??>
                                Создано: ${vacancy.createdDate?datetime?string("dd.MM.yyyy")}
                            </#if>
                        </div>
                    </div>
                </div>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-briefcase text-blue-600 mr-2"></i>Описание вакансии
                </h3>

                <#if vacancy.description?? && vacancy.description?has_content>
                    <div class="prose max-w-none">
                        <p class="text-gray-700">${vacancy.description}</p>
                    </div>
                <#else>
                    <p class="text-gray-500 italic">Описание не предоставлено</p>
                </#if>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-user-check text-blue-600 mr-2"></i>Требования
                </h3>

                <div class="space-y-3">
                    <div class="flex items-center">
                        <i class="fas fa-graduation-cap text-blue-500 mr-3"></i>
                        <span class="text-gray-700">Опыт работы: от ${vacancy.expFrom} до ${vacancy.expTo} ${getPluralForm(vacancy.expTo, "года", "лет", "лет")}</span>
                    </div>

                </div>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-building text-blue-600 mr-2"></i>О компании
                </h3>

                <div class="flex items-start">
                    <div class="flex-shrink-0 mr-4">
                        <div class="w-16 h-16 bg-gray-200 rounded-full flex items-center justify-center">
                            <i class="fas fa-building text-gray-400 text-2xl"></i>
                        </div>
                    </div>
                    <div>
                        <h4 class="font-medium text-gray-800">${vacancy.authorName!'Компания'}</h4>
                        <p class="text-gray-600 mt-1">Работодатель</p>
                    </div>
                </div>
            </div>

            <div class="p-6 bg-gray-50">
                <div class="flex flex-col md:flex-row justify-between items-center">
                    <div class="flex space-x-4">
                        <button class="text-blue-600 hover:text-blue-800">
                            <i class="far fa-bookmark mr-1"></i> Сохранить
                        </button>
                        <button class="text-blue-600 hover:text-blue-800">
                            <i class="fas fa-share-alt mr-1"></i> Поделиться
                        </button>
                    </div>

                    <#if !currentUser??>
                        <a href="/auth/login" class="mt-4 md:mt-0 bg-blue-500 text-white px-6 py-2 rounded-md hover:bg-blue-600 transition font-medium">
                            <i class="fas fa-sign-in-alt mr-2"></i>Войти, чтобы откликнуться
                        </a>
                    <#elseif currentUser.roleId == 2>
                        <button class="mt-4 md:mt-0 bg-green-500 text-white px-6 py-2 rounded-md hover:bg-green-600 transition font-medium">
                            <i class="fas fa-paper-plane mr-2"></i>Откликнуться на вакансию
                        </button>
                    </#if>
                </div>
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