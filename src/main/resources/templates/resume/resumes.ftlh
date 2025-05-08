<#import "../layout.ftlh" as main>
<#import '/spring.ftl' as spring>
<@main.layout>

    <div class="bg-blue-600 text-white pb-6">
        <div class="container mx-auto px-4">
            <form action="#" method="get">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <input
                            type="text"
                            name="query"
                            placeholder="<@spring.message "resumes.search.placeholder"/>"
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
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold"><@spring.message "resumes.title"/></h2>
            <div class="text-gray-500 text-sm">  <@spring.messageArgs "resumes.count", [resumes.totalElements]/></div>
        </div>

        <#if categories?? && categories?size gt 0>
            <div class="flex flex-wrap gap-2 mb-4">
                <a href="/resumes"
                   class="px-3 py-1 rounded-full ${(categoryId??)?then('bg-gray-100 text-gray-700', 'bg-blue-600 text-white')}">
                    <@spring.message "resumes.category.all"/>
                </a>
                <#list categories as category>
                    <a href="/resumes?categoryId=${category.id}"
                       class="px-3 py-1 rounded-full ${(categoryId?? && categoryId == category.id)?then('bg-blue-600 text-white', 'bg-gray-100 text-gray-700')}">
                        ${category.name}
                    </a>
                </#list>
            </div>
        </#if>

        <div class="space-y-4">
            <#if resumes.content?? && resumes.content?size gt 0>
                <#list resumes.content as resume>
                    <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                        <div class="flex justify-between">
                            <div>
                                <h3 class="font-bold text-lg text-gray-800">${resume.name}</h3>
                                <p class="text-gray-600 mb-2">${resume.applicant.name!}</p>
                                <div class="text-blue-600 font-medium">
                                    <#if resume.salary?? && resume.salary gt 0>
                                        ${resume.salary?string(",##0")} ₽
                                    <#else>
                                        <@spring.message "resumes.salary.notSpecified"/>
                                    </#if>
                                </div>
                            </div>
                            <div class="flex flex-col items-end justify-between">
                                <span class="text-xs text-gray-400 mt-4">
                                <#if resume.updatedAt??>
                                    <@spring.message "resumes.updated"/>: ${resume.updatedAt?datetime?string("dd.MM.yyyy")}
                                <#elseif resume.createdAt??>
                                    <@spring.message "resumes.created"/>: ${resume.createdAt?datetime?string("dd.MM.yyyy")}
                                <#else>
                                    <@spring.message "resumes.date.notSpecified"/>
                                </#if>
                            </span>
                            </div>
                        </div>

                        <!-- Опыт работы и образование -->
                        <div class="flex flex-wrap gap-2 mt-3">
                            <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                                <#assign totalYears = 0>
                                <#list resume.workExperiences as exp>
                                    <#assign totalYears = totalYears + exp.years>
                                </#list>
                                <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                <@spring.message "resumes.experience.years"/>: ${totalYears}
                            </span>
                            <#else>
                                <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                <@spring.message "resumes.experience.none"/>
                            </span>
                            </#if>

                            <#if resume.isActive?? && resume.isActive>
                                <span class="bg-green-100 text-green-600 text-xs px-2 py-1 rounded"><@spring.message "resumes.status.active"/></span>
                            <#else>
                                <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded"><@spring.message "resumes.status.inactive"/></span>
                            </#if>

                            <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                    <i class="fas fa-tags mr-1"></i>${resume.category.name}
                            </span>
                        </div>

                        <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                            <#assign lastJob = resume.workExperiences[0]>
                            <p class="text-gray-600 text-sm mt-3">
                                <span class="font-medium"><@spring.message "resumes.lastPosition"/>:</span> ${lastJob.position}
                                <@spring.message "resumes.lastPosition.at"/> ${lastJob.companyName}

                            </p>
                        <#else>
                            <p class="text-gray-600 text-sm mt-3 italic"><@spring.message "resumes.experience.notSpecified"/></p>
                        </#if>

                        <div class="flex justify-between items-center mt-4">
                            <a href="/resumes/${resume.id}"
                               class="text-blue-600 hover:text-blue-800 text-sm font-medium"><@spring.message "resumes.details"/></a>
                        </div>
                    </div>
                </#list>

                <!-- Пагинация -->
                <div class="flex justify-center items-center mt-6 gap-2">
                    <!-- Информация о пагинации -->
                    <div class="text-gray-500 text-sm mr-4">
                        <@spring.message "pagination.page"/> ${resumes.number + 1} <@spring.message "pagination.of"/> ${resumes.totalPages}
                    </div>

                    <!-- Кнопка "Предыдущая страница" -->
                    <#if resumes.number gt 0>
                        <a href="?page=${resumes.number}<#if size??>&size=${resumes.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if>"
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
                        <#assign startPage = ((resumes.number - 2) gt 0)?then(resumes.number - 2, 0)>
                        <#assign endPage = ((startPage + 4) lt resumes.totalPages)?then(startPage + 4, resumes.totalPages - 1)>
                        <#if startPage gt 0>
                            <a href="?page=1<#if size??>&size=${resumes.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                1
                            </a>
                            <#if startPage gt 1>
                                <span class="px-2 py-2">...</span>
                            </#if>
                        </#if>

                        <#list startPage..endPage as i>
                            <a href="?page=${i + 1}<#if size??>&size=${resumes.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if>"
                               class="px-3 py-2 rounded ${(i == resumes.number)?then('bg-blue-600 text-white', 'hover:bg-gray-100')} transition-colors">
                                ${i + 1}
                            </a>
                        </#list>

                        <#if endPage lt resumes.totalPages - 1>
                            <#if endPage lt resumes.totalPages - 2>
                                <span class="px-2 py-2">...</span>
                            </#if>
                            <a href="?page=${resumes.totalPages}<#if size??>&size=${resumes.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors">
                                ${resumes.totalPages}
                            </a>
                        </#if>
                    </div>

                    <!-- Кнопка "Следующая страница" -->
                    <#if resumes.number lt resumes.totalPages - 1>
                        <a href="?page=${resumes.number + 2}<#if size??>&size=${resumes.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if>"
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
                    <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
                    <h3 class="font-bold text-lg text-gray-800 mb-2"><@spring.message "resumes.empty.title"/></h3>
                    <p class="text-gray-600"><@spring.message "resumes.empty.description"/></p>
                </div>
            </#if>
        </div>
    </main>
</@main.layout>