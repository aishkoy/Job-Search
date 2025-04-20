<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="bg-blue-600 text-white pb-6">
        <div class="container mx-auto px-4">
            <form action="#" method="get">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <label>
                        <input
                                type="text"
                                name="query"
                                placeholder="Поиск пользователей..."
                                class="flex-1 outline-none text-gray-800 py-2 px-1"
                        >
                    </label>
                    <button type="submit" class="bg-gray-100 text-gray-600 p-2 rounded-full mr-1">
                        <i class="fas fa-filter"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <main class="container mx-auto px-4 py-6">
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold">
                <#if users?? && users?size gt 0>
                    <#if users[0].role.role == 'EMPLOYER'>Работодатели<#else>Соискатели</#if>
                </#if>
            </h2>
            <div class="text-gray-500 text-sm">
                Найдено пользователей: ${users?size}
            </div>
        </div>

        <div class="space-y-4">
            <#if users?? && users?size gt 0>
                <#list users as user>
                    <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                        <div class="flex justify-between items-start">
                            <div class="flex items-center space-x-4">
                                <#if user.avatar??>
                                    <img src="/api/users/${user.id}/avatar" alt="${user.name}" class="w-16 h-16 rounded-full object-cover">
                                <#else>
                                    <div class="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center">
                                        <i class="fas fa-user text-gray-500 text-2xl"></i>
                                    </div>
                                </#if>
                                <div>
                                    <h3 class="font-bold text-lg text-gray-800">
                                        ${user.name} ${user.surname!}
                                    </h3>
                                    <p class="text-gray-600 text-sm">
                                        <#if user.role.role == 'EMPLOYER'>
                                            <i class="fas fa-briefcase mr-2"></i>Работодатель
                                        <#else>
                                            <i class="fas fa-user-tie mr-2"></i>Соискатель
                                        </#if>
                                    </p>
                                </div>
                            </div>

                            <div class="text-right">
                                <#if user.role.role == 'EMPLOYER'>
                                    <div class="text-blue-600 font-medium">
                                        Вакансий: ${user.vacancies?size!0}
                                    </div>
                                <#else>
                                    <div class="text-blue-600 font-medium">
                                        Резюме: ${user.resumes?size!0}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="mt-4 flex justify-between items-center">
                            <div class="text-gray-500 text-sm">
                                <i class="fas fa-envelope mr-2"></i>${user.email}
                                <#if user.age??>
                                    • ${user.age} лет
                                </#if>
                            </div>

                            <a href="/profile/${user.id}"
                               class="text-blue-600 hover:text-blue-800 text-sm font-medium">
                                Перейти в профиль
                                <i class="fas fa-arrow-right ml-2"></i>
                            </a>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="bg-white rounded-lg shadow p-6 text-center">
                    <i class="fas fa-users-slash text-gray-400 text-4xl mb-4"></i>
                    <h3 class="font-bold text-lg text-gray-800 mb-2">Пользователи не найдены</h3>
                    <p class="text-gray-600">Список пользователей пуст.</p>
                </div>
            </#if>
        </div>
    </main>
</@main.layout>