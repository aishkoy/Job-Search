<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>

    <div class="bg-blue-600 text-white pb-6">
        <div class="container mx-auto px-4">
            <form action="/vacancies" method="get" id="search-form">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <input
                            type="text"
                            name="query"
                            placeholder="<@spring.message "vacancies.search.placeholder"/>"
                            class="flex-1 outline-none text-gray-800 py-2 px-1"
                            value="${RequestParameters.query!''}"
                    >
                    <button type="submit" class="bg-gray-100 text-gray-600 p-2 rounded-full mr-1">
                        <i class="fas fa-search"></i>
                    </button>
                    <#if RequestParameters.query??>
                        <button type="button" onclick="window.VacancyFilters.update('query', '')"
                                class="bg-red-100 text-red-600 p-2 rounded-full mr-1" title="Очистить поиск">
                            <i class="fas fa-times"></i>
                        </button>
                    </#if>
                </div>
            </form>
        </div>
    </div>

    <main class="container mx-auto px-4 py-6">
        <div class="flex items-center justify-between mb-6">
            <div class="flex items-center space-x-2">
                <form action="/vacancies" method="get" class="flex items-center space-x-2" id="sort-form">
                    <#if categoryId??>
                        <input type="hidden" name="categoryId" value="${categoryId}">
                    </#if>
                    <#if size??>
                        <input type="hidden" name="size" value="${size}">
                    </#if>
                    <#if RequestParameters.query??>
                        <input type="hidden" name="query" value="${RequestParameters.query}">
                    </#if>

                    <label>
                        <select
                                name="sortBy"
                                class="appearance-none w-full bg-white border border-gray-300 rounded-md pl-3 pr-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                                id="sort-by-select"
                        >
                            <option value="updatedAt" <#if (sortBy!'updatedAt') == 'updatedAt'>selected</#if>>
                                <@spring.message "vacancies.filter.sort.byUpdatedAt"/>
                            </option>
                            <option value="createdAt" <#if sortBy == 'createdAt'>selected</#if>>
                                <@spring.message "vacancies.filter.sort.byDate"/>
                            </option>
                            <option value="salary" <#if sortBy == 'salary'>selected</#if>>
                                <@spring.message "vacancies.filter.sort.bySalary"/>
                            </option>
                            <option value="responses" <#if sortBy == 'responses'>selected</#if>>
                                <@spring.message "vacancies.filter.sort.byResponses"/>
                            </option>
                        </select>
                    </label>

                    <button
                            type="button"
                            id="sort-direction-btn"
                            class="p-2 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors"
                            data-direction="${sortDirection!'desc'}"
                    >
                        <#if (sortDirection!'desc') == 'desc'>
                            <i class="fas fa-sort-amount-down text-gray-600"></i>
                        <#else>
                            <i class="fas fa-sort-amount-up text-gray-600"></i>
                        </#if>
                    </button>
                </form>
            </div>

            <div class="flex items-center space-x-4">
                <div class="text-gray-500 text-sm">
                    <@spring.message "vacancies.count"/>: ${vacancies.totalElements}
                </div>
                <button
                        onclick="window.VacancyFilters.clearAll()"
                        class="text-red-600 hover:text-red-800 text-sm"
                        title="<@spring.message "vacancies.filter.clear"/>">
                    <i class="fas fa-times-circle mr-1"></i>
                    <@spring.message "vacancies.filter.clear"/>
                </button>
            </div>
        </div>

        <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold"><@spring.message "vacancies.title"/></h2>
        </div>

        <#if categories?? && categories?size gt 0>
            <div class="flex flex-wrap gap-2 mb-4">
                <a href="/vacancies<#if sortBy?? || sortDirection?? || RequestParameters.query??>?<#if sortBy??>sortBy=${sortBy}</#if><#if sortDirection??><#if sortBy??>&</#if>sortDirection=${sortDirection}</#if><#if RequestParameters.query??><#if sortBy?? || sortDirection??>&</#if>query=${RequestParameters.query}</#if></#if>"
                   class="px-3 py-1 rounded-full ${(categoryId??)?then('bg-gray-100 text-gray-700', 'bg-blue-600 text-white')} category-link">
                    <@spring.message "vacancies.category.all"/>
                </a>
                <#list categories as category>
                    <a href="/vacancies?categoryId=${category.id}<#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                       class="px-3 py-1 rounded-full ${(categoryId?? && categoryId == category.id)?then('bg-blue-600 text-white', 'bg-gray-100 text-gray-700')} category-link">
                        ${category.name}
                    </a>
                </#list>
            </div>
        </#if>

        <div class="space-y-4">
            <#if vacancies.content?? && vacancies.content?size gt 0>
                <#list vacancies.content as vacancy>
                    <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                        <div class="flex justify-between">
                            <div>
                                <h3 class="font-bold text-lg text-gray-800">${vacancy.name}</h3>
                                <p class="text-gray-600 mb-2">${vacancy.employer.name!}</p>
                                <div class="text-blue-600 font-medium">
                                    <#if vacancy.salary?? && vacancy.salary gt 0>
                                        ${vacancy.salary?string(",##0")} ₽
                                    <#else>
                                        <@spring.message "vacancies.salary.notSpecified"/>
                                    </#if>
                                </div>
                            </div>
                            <div class="flex flex-col items-end justify-between">
                                <span class="text-xs text-gray-400 mt-4">
                                          <@spring.message "profile.vacancies.created"/>: ${vacancy.createdAt?datetime?string("dd.MM.yyyy")}
                                </span>

                                <span class="text-xs text-gray-400 mt-4">
                                          <@spring.message "profile.vacancies.updated"/>: ${vacancy.updatedAt?datetime?string("dd.MM.yyyy")}
                                </span>
                                <span class="text-xs text-gray-400 mt-4">
                                        <@spring.message "vacancies.responses"/>: ${vacancy.responses!'0'}
                                </span>
                            </div>
                        </div>
                        <div class="flex flex-wrap gap-2 mt-3">
                            <#if vacancy.expFrom?? && vacancy.expTo??>
                                <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                    <@spring.message "vacancy.experience"/>: ${vacancy.expFrom}-${vacancy.expTo}
                                </span>
                            </#if>

                            <#if vacancy.isActive?? && vacancy.isActive>
                                <span class="bg-green-100 text-green-600 text-xs px-2 py-1 rounded"><@spring.message "vacancies.status.active"/></span>
                            <#else>
                                <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded"><@spring.message "vacancies.status.inactive"/></span>
                            </#if>

                            <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                    <i class="fas fa-tags mr-1"></i>${vacancy.category.name}
                            </span>
                        </div>
                        <p class="text-gray-600 text-sm mt-3 line-clamp-2">
                            <#if vacancy.description?has_content>
                                ${vacancy.description?truncate(150)}
                            <#else>
                                <@spring.message "vacancies.description.empty"/>
                            </#if>
                        </p>
                        <div class="flex justify-between items-center mt-4">
                            <a href="/vacancies/${vacancy.id}"
                               class="text-blue-600 hover:text-blue-800 text-sm font-medium"><@spring.message "index.details"/></a>
                        </div>
                    </div>
                </#list>

                <!-- Пагинация -->
                <div class="flex justify-center items-center mt-6 gap-2">
                    <!-- Информация о пагинации -->
                    <div class="text-gray-500 text-sm mr-4">
                        <@spring.message "pagination.page"/> ${vacancies.number + 1} <@spring.message "pagination.of"/> ${vacancies.totalPages}
                    </div>

                    <!-- Кнопка "Предыдущая страница" -->
                    <#if vacancies.number gt 0>
                        <a href="?page=${vacancies.number}<#if size??>&size=${vacancies.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if><#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                           class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors pagination-link">
                            <i class="fas fa-chevron-left mr-1"></i> <@spring.message "pagination.previous"/>
                        </a>
                    <#else>
                        <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                            <i class="fas fa-chevron-left mr-1"></i> <@spring.message "pagination.previous"/>
                        </span>
                    </#if>

                    <!-- Номера страниц -->
                    <div class="flex space-x-1">
                        <#assign startPage = ((vacancies.number - 2) gt 0)?then(vacancies.number - 2, 0)>
                        <#assign endPage = ((startPage + 4) lt vacancies.totalPages)?then(startPage + 4, vacancies.totalPages - 1)>
                        <#if startPage gt 0>
                            <a href="?page=1<#if size??>&size=${vacancies.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if><#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors pagination-link">
                                1
                            </a>
                            <#if startPage gt 1>
                                <span class="px-2 py-2">...</span>
                            </#if>
                        </#if>

                        <#list startPage..endPage as i>
                            <a href="?page=${i + 1}<#if size??>&size=${vacancies.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if><#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                               class="px-3 py-2 rounded ${(i == vacancies.number)?then('bg-blue-600 text-white', 'hover:bg-gray-100')} transition-colors pagination-link">
                                ${i + 1}
                            </a>
                        </#list>

                        <#if endPage lt vacancies.totalPages - 1>
                            <#if endPage lt vacancies.totalPages - 2>
                                <span class="px-2 py-2">...</span>
                            </#if>
                            <a href="?page=${vacancies.totalPages}<#if size??>&size=${vacancies.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if><#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                               class="px-3 py-2 rounded hover:bg-gray-100 transition-colors pagination-link">
                                ${vacancies.totalPages}
                            </a>
                        </#if>
                    </div>

                    <#if vacancies.number lt vacancies.totalPages - 1>
                        <a href="?page=${vacancies.number + 2}<#if size??>&size=${vacancies.size}</#if><#if categoryId??>&categoryId=${categoryId}</#if><#if sortBy??>&sortBy=${sortBy}</#if><#if sortDirection??>&sortDirection=${sortDirection}</#if><#if RequestParameters.query??>&query=${RequestParameters.query}</#if>"
                           class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors pagination-link">
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
                    <h3 class="font-bold text-lg text-gray-800 mb-2"><@spring.message "vacancies.empty.title"/></h3>
                    <p class="text-gray-600"><@spring.message "vacancies.empty.description"/></p>
                </div>
            </#if>
        </div>
    </main>

    <script src="/static/js/vacancies.js"></script>
</@main.layout>