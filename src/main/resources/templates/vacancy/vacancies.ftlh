<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>
<@main.layout>

    <div class="bg-blue-600 text-white pb-6">
        <div class="container mx-auto px-4">
            <form id="search-form">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <input
                            type="text"
                            name="query"
                            id="search-input"
                            placeholder="<@spring.message "vacancies.search.placeholder"/>"
                            class="flex-1 outline-none text-gray-800 py-2 px-1"
                    >
                    <button type="submit" class="bg-gray-100 text-gray-600 p-2 rounded-full mr-1">
                        <i class="fas fa-search"></i>
                    </button>
                    <button type="button" id="clear-search-btn"
                            class="bg-red-100 text-red-600 p-2 rounded-full mr-1 hidden"
                            title="Очистить поиск">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <main class="container mx-auto px-4 py-6">
        <div class="flex items-center justify-between mb-6">
            <div class="flex items-center space-x-2">
                <div class="flex items-center space-x-2">
                    <label>
                        <select
                                id="category-select"
                                class="appearance-none w-full bg-white border border-gray-300 rounded-md pl-3 pr-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">
                                <@spring.message "vacancies.category.all"/>
                            </option>
                            <#if categories?? && categories?size gt 0>
                                <#list categories as category>
                                    <option value="${category.id}">
                                        ${category.name}
                                    </option>
                                </#list>
                            </#if>
                        </select>
                    </label>

                    <label>
                        <select
                                id="sort-by-select"
                                class="appearance-none w-full bg-white border border-gray-300 rounded-md pl-3 pr-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="updatedAt">
                                <@spring.message "vacancies.filter.sort.byUpdatedAt"/>
                            </option>
                            <option value="createdAt">
                                <@spring.message "vacancies.filter.sort.byDate"/>
                            </option>
                            <option value="salary">
                                <@spring.message "vacancies.filter.sort.bySalary"/>
                            </option>
                            <option value="responses">
                                <@spring.message "vacancies.filter.sort.byResponses"/>
                            </option>
                        </select>
                    </label>

                    <button
                            type="button"
                            id="sort-direction-btn"
                            class="p-2 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors"
                            data-direction="desc"
                    >
                        <i class="fas fa-sort-amount-down text-gray-600"></i>
                    </button>
                </div>
            </div>

            <div class="flex items-center space-x-4">
                <div class="text-gray-500 text-sm" id="vacancies-count">
                    <@spring.message "vacancies.count"/>: 0
                </div>
                <button
                        id="clear-filters-btn"
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

        <div id="loading-spinner" class="hidden text-center py-8">
            <i class="fas fa-spinner fa-spin text-3xl text-blue-600"></i>
            <p class="text-gray-600 mt-2">Загрузка вакансий...</p>
        </div>

        <div class="space-y-4" id="vacancies-container">
        </div>

        <div id="no-vacancies" class="bg-white rounded-lg shadow p-6 text-center hidden">
            <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
            <h3 class="font-bold text-lg text-gray-800 mb-2" id="no-vacancies-title">
                <@spring.message "vacancies.empty.title"/>
            </h3>
            <p class="text-gray-600" id="no-vacancies-description">
                <@spring.message "vacancies.empty.description"/>
            </p>
        </div>

        <div class="flex justify-center items-center mt-6 gap-2" id="pagination-container">
        </div>
    </main>

    <!-- Скрипт для интернационализации -->
    <script>
        window.i18n = {
            'vacancies.count': '<@spring.message "vacancies.count"/>',
            'vacancies.salary.notSpecified': '<@spring.message "vacancies.salary.notSpecified"/>',
            'vacancy.experience': '<@spring.message "vacancy.experience"/>',
            'vacancies.status.active': '<@spring.message "vacancies.status.active"/>',
            'vacancies.status.inactive': '<@spring.message "vacancies.status.inactive"/>',
            'vacancies.description.empty': '<@spring.message "vacancies.description.empty"/>',
            'profile.vacancies.created': '<@spring.message "profile.vacancies.created"/>',
            'profile.vacancies.updated': '<@spring.message "profile.vacancies.updated"/>',
            'vacancies.responses': '<@spring.message "vacancies.responses"/>',
            'pagination.page': '<@spring.message "pagination.page"/>',
            'pagination.of': '<@spring.message "pagination.of"/>',
            'pagination.previous': '<@spring.message "pagination.previous"/>',
            'pagination.next': '<@spring.message "pagination.next"/>',
            'vacancies.empty.title': '<@spring.message "vacancies.empty.title"/>',
            'vacancies.empty.description': '<@spring.message "vacancies.empty.description"/>',
            'vacancies.search.noResults': '<@spring.message "search.noResult"/>',
            'vacancies.search.tryOther': '<@spring.message "search.tryOther"/>'
        };
    </script>

    <script src="/static/js/vacancies-filter.js"></script>
    <script src="/static/js/vacancies.js"></script>
</@main.layout>