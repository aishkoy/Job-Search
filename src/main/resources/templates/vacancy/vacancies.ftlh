<#import "../layout.ftlh" as main>

<@main.layout>

<div class="bg-blue-600 text-white pb-6">
    <div class="container mx-auto px-4">
        <#--TODO-->
        <form action="#" method="get">
            <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                <input
                        type="text"
                        name="query"
                        placeholder="Поиск вакансий, компаний..."
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
        <h2 class="text-xl font-bold">Найденные вакансии</h2>
        <div class="text-gray-500 text-sm">Найдено вакансий: ${vacancies?size} </div>
    </div>

    <div class="space-y-4">
        <#if vacancies?? && vacancies?size gt 0>
            <#list vacancies as vacancy>
                <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                    <div class="flex justify-between">
                        <div>
                            <h3 class="font-bold text-lg text-gray-800">${vacancy.name}</h3>
                            <p class="text-gray-600 mb-2">${vacancy.employer.name!'Работодатель'}</p>
                            <div class="text-blue-600 font-medium">
                                <#if vacancy.salary?? && vacancy.salary gt 0>
                                    ${vacancy.salary?string(",##0")} ₽
                                <#else>
                                    Зарплата не указана
                                </#if>
                            </div>
                        </div>
                        <div class="flex flex-col items-end justify-between">
                            <span class="text-xs text-gray-400 mt-4">
                                    <#if vacancy.createdAt??>
                                        ${vacancy.createdAt?datetime?string("dd.MM.yyyy")}
                                    <#else>
                                        Дата не указана
                                    </#if>
                                </span>
                        </div>
                    </div>
                    <div class="flex flex-wrap gap-2 mt-3">
                        <#if vacancy.expFrom?? && vacancy.expTo??>
                            <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                    Опыт в годах: ${vacancy.expFrom}-${vacancy.expTo}
                                </span>
                        </#if>

                        <#if vacancy.isActive?? && vacancy.isActive>
                            <span class="bg-green-100 text-green-600 text-xs px-2 py-1 rounded">Активна</span>
                        <#else>
                            <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">Не активна</span>
                        </#if>
                    </div>
                    <p class="text-gray-600 text-sm mt-3 line-clamp-2">
                        <#if vacancy.description?has_content>
                            ${vacancy.description?truncate(150)}
                        <#else>
                            Описание отсутствует
                        </#if>
                    </p>
                    <div class="flex justify-between items-center mt-4">
                        <a href="/vacancies/${vacancy.id}" class="text-blue-600 hover:text-blue-800 text-sm font-medium">Подробнее</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="bg-white rounded-lg shadow p-6 text-center">
                <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
                <h3 class="font-bold text-lg text-gray-800 mb-2">Вакансии не найдены</h3>
                <p class="text-gray-600">Попробуйте изменить параметры поиска или вернитесь позже.</p>
            </div>
        </#if>
    </div>
</main>
</@main.layout>