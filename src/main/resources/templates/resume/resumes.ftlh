<#import "../layout.ftlh" as main>
<#import '/spring.ftl' as spring>
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
                            placeholder="<@spring.message "resumes.search.placeholder"/>"
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
                    <!-- Выбор категории -->
                    <label>
                        <select
                                id="category-select"
                                class="appearance-none w-full bg-white border border-gray-300 rounded-md pl-3 pr-10 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">
                                <@spring.message "resumes.category.all"/>
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

                    <!-- Сортировка -->
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
                <div class="text-gray-500 text-sm" id="resumes-count">
                    <@spring.message "resumes.count"/>: 0
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
            <h2 class="text-xl font-bold"><@spring.message "resumes.title"/></h2>
        </div>

        <!-- Loader -->
        <div id="loading-spinner" class="hidden text-center py-8">
            <i class="fas fa-spinner fa-spin text-3xl text-blue-600"></i>
            <p class="text-gray-600 mt-2">Загрузка резюме...</p>
        </div>

        <!-- Контейнер для резюме -->
        <div class="space-y-4" id="resumes-container">
            <!-- Резюме будут загружены динамически -->
        </div>

        <!-- Сообщение об отсутствии резюме -->
        <div id="no-resumes" class="bg-white rounded-lg shadow p-6 text-center hidden">
            <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
            <h3 class="font-bold text-lg text-gray-800 mb-2" id="no-resumes-title">
                <@spring.message "resumes.empty.title"/>
            </h3>
            <p class="text-gray-600" id="no-resumes-description">
                <@spring.message "resumes.empty.description"/>
            </p>
        </div>

        <!-- Пагинация -->
        <div class="flex justify-center items-center mt-6 gap-2" id="pagination-container">
            <!-- Пагинация будет загружена динамически -->
        </div>
    </main>

    <!-- Скрипт для интернационализации -->
    <script>
        window.i18n = {
            'resumes.count': '<@spring.message "resumes.count"/>',
            'resumes.salary.notSpecified': '<@spring.message "resumes.salary.notSpecified"/>',
            'resumes.experience.years': '<@spring.message "resumes.experience.years"/>',
            'resumes.experience.none': '<@spring.message "resumes.experience.none"/>',
            'resumes.status.active': '<@spring.message "resumes.status.active"/>',
            'resumes.status.inactive': '<@spring.message "resumes.status.inactive"/>',
            'resumes.lastPosition': '<@spring.message "resumes.lastPosition"/>',
            'resumes.lastPosition.at': '<@spring.message "resumes.lastPosition.at"/>',
            'resumes.experience.notSpecified': '<@spring.message "resumes.experience.notSpecified"/>',
            'resumes.created': '<@spring.message "resumes.created"/>',
            'resumes.updated': '<@spring.message "resumes.updated"/>',
            'pagination.page': '<@spring.message "pagination.page"/>',
            'pagination.of': '<@spring.message "pagination.of"/>',
            'pagination.previous': '<@spring.message "pagination.previous"/>',
            'pagination.next': '<@spring.message "pagination.next"/>',
            'resumes.empty.title': '<@spring.message "resumes.empty.title"/>',
            'resumes.empty.description': '<@spring.message "resumes.empty.description"/>',
            'resumes.search.noResults': '<@spring.message "search.noResult"/>',
            'resumes.search.tryOther': '<@spring.message "search.tryOther"/>'
        };
    </script>
    <script src="/static/js/resumes-filter.js"></script>
    <script src="/static/js/resumes.js"></script>
</@main.layout>