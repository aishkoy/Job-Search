<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="bg-blue-600 text-white pb-6">
        <div class="container mx-auto px-4">
            <form action="#" method="get">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <input
                            type="text"
                            name="query"
                            placeholder="<@spring.message "users.search.placeholder"/>"
                            class="flex-1 outline-none text-gray-800 py-2 px-1"
                    >
                    <button type="submit" class="bg-gray-100 text-gray-600 p-2 rounded-full mr-1">
                        <i class="fas fa-filter"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <main class="container mx-auto px-4 py-6">
        <div class="flex items-center justify-between mb-6">
            <div class="text-gray-500 text-sm"><@spring.message "users.total.found"/> ${users.totalElements}</div>
        </div>

        <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold">
                <#if users.content?size gt 0>
                    <#if users.content[0].role.name == 'EMPLOYER'><@spring.message "users.employers"/><#else><@spring.message "users.jobseekers"/></#if>
                </#if>
            </h2>
        </div>

        <div class="space-y-4">
            <#if users.content?? && users.content?size gt 0>
                <#list users.content as user>
                    <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                        <div class="flex justify-between items-start">
                            <div class="flex items-center space-x-4">
                                <#if user.avatar??>
                                    <img src="/api/users/${user.id}/avatar" alt="${user.name}"
                                         class="w-16 h-16 rounded-full object-cover">
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
                                        <#if user.role.name == 'EMPLOYER'>
                                            <i class="fas fa-briefcase mr-2"></i><@spring.message "profile.role.employer"/>
                                        <#else>
                                            <i class="fas fa-user-tie mr-2"></i><@spring.message "profile.role.applicant"/>
                                        </#if>
                                    </p>
                                </div>
                            </div>

                            <div class="text-right">
                                <#if user.role.name == 'EMPLOYER'>
                                    <div class="text-blue-600 font-medium">
                                        <@spring.message "users.vacancies"/>: ${user.vacanciesCount!0}
                                    </div>
                                <#else>
                                    <div class="text-blue-600 font-medium">
                                        <@spring.message "users.resumes"/>: ${user.resumesCount!0}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="mt-4 flex justify-between items-center">
                            <div class="flex flex-col text-gray-500 text-sm">
                                <div>
                                    <i class="fas fa-envelope mr-2"></i>${user.email}
                                </div>
                                <#if user.age??>
                                    <@spring.message"profile.age"/>: ${user.age}
                                </#if>
                            </div>

                            <a href="/${url}/${user.id}"
                               class="text-blue-600 hover:text-blue-800 text-sm font-medium">
                                <@spring.message "users.profile.link"/>
                                <i class="fas fa-arrow-right ml-2"></i>
                            </a>
                        </div>
                    </div>
                </#list>

                <!-- Пагинация -->
                <div class="flex justify-center items-center mt-6 gap-2">
                    <!-- Информация о пагинации -->
                    <div class="text-gray-500 text-sm mr-4">
                        <@spring.message "pagination.page"/> ${users.number + 1} <@spring.message "pagination.of"/> ${users.totalPages}
                    </div>

                    <!-- Кнопка "Предыдущая страница" -->
                    <#if users.number gt 0>
                        <a href="?page=${users.number}<#if size??>&size=${users.size}</#if>"
                           class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                            <i class="fas fa-chevron-left mr-1"></i> <@spring.message "pagination.previous"/>
                        </a>
                    <#else>
                        <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                            <i class="fas fa-chevron-left mr-1"></i> <@spring.message "pagination.previous"/>
                        </span>
                    </#if>

                    <!-- Номера страниц -->
                    <div class="flex space-x-1">
                        <#assign startPage = ((users.number - 2) gt 0)?then(users.number - 2, 0)>
                        <#assign endPage = ((startPage + 4) lt users.totalPages)?then(startPage + 4, users.totalPages - 1)>
                        <#if startPage gt 0>
                            <a href="?page=1<#if size??>&size=${users.size}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                1
                            </a>
                            <#if startPage gt 1>
                                <span class="px-2 py-2">...</span>
                            </#if>
                        </#if>

                        <#list startPage..endPage as i>
                            <a href="?page=${i + 1}<#if size??>&size=${users.size}</#if>"
                               class="px-3 py-2 rounded ${(i == users.number)?then('bg-blue-600 text-white', 'hover:bg-gray-100')} transition-colors">
                                ${i + 1}
                            </a>
                        </#list>

                        <#if endPage lt users.totalPages - 1>
                            <#if endPage lt users.totalPages - 2>
                                <span class="px-2 py-2">...</span>
                            </#if>
                            <a href="?page=${users.totalPages}<#if size??>&size=${users.size}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                ${users.totalPages}
                            </a>
                        </#if>
                    </div>

                    <!-- Кнопка "Следующая страница" -->
                    <#if users.number lt users.totalPages - 1>
                        <a href="?page=${users.number + 2}<#if size??>&size=${users.size}</#if>"
                           class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                            <@spring.message "pagination.next"/> <i class="fas fa-chevron-right ml-1"></i>
                        </a>
                    <#else>
                        <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                            <@spring.message "pagination.next"/> <i class="fas fa-chevron-right ml-1"></i>
                        </span>
                    </#if>
                </div>
            <#else>
                <div class="bg-white rounded-lg shadow p-6 text-center">
                    <i class="fas fa-users-slash text-gray-400 text-4xl mb-4"></i>
                    <h3 class="font-bold text-lg text-gray-800 mb-2"><@spring.message "users.empty.title"/></h3>
                    <p class="text-gray-600"><@spring.message "users.empty.description"/></p>
                </div>
            </#if>
        </div>
    </main>
</@main.layout>