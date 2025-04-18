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
                                placeholder="Поиск резюме, специалистов..."
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
            <h2 class="text-xl font-bold">Найденные резюме</h2>
            <div class="text-gray-500 text-sm">Найдено: ${resumes?size} резюме</div>
        </div>

        <div class="space-y-4">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
                        <div class="flex justify-between">
                            <div>
                                <h3 class="font-bold text-lg text-gray-800">${resume.name}</h3>
                                <p class="text-gray-600 mb-2">${resume.applicant.name!'Специалист'}</p>
                                <div class="text-blue-600 font-medium">
                                    <#if resume.salary?? && resume.salary gt 0>
                                        ${resume.salary?string(",##0")} ₽
                                    <#else>
                                        Ожидаемая зарплата не указана
                                    </#if>
                                </div>
                            </div>
                            <div class="flex flex-col items-end justify-between">
                                <span class="text-xs text-gray-400 mt-4">
                                <#if resume.updatedAt??>
                                    Обновлено: ${resume.updatedAt?datetime?string("dd.MM.yyyy")}
                                <#elseif resume.createdAt??>
                                    Создано: ${resume.createdAt?datetime?string("dd.MM.yyyy")}
                                <#else>
                                    Дата не указана
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
                                Опыт: ${totalYears} лет
                            </span>
                            <#else>
                                <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                Без опыта
                            </span>
                            </#if>

                            <#if resume.isActive?? && resume.isActive>
                                <span class="bg-green-100 text-green-600 text-xs px-2 py-1 rounded">Активно</span>
                            <#else>
                                <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">Не активно</span>
                            </#if>

                            <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                    <i class="fas fa-tags mr-1"></i>${resume.category.name}
                            </span>
                        </div>

                        <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                            <#assign lastJob = resume.workExperiences[0]>
                            <p class="text-gray-600 text-sm mt-3">
                                <span class="font-medium">Последняя должность:</span> ${lastJob.position}
                                в ${lastJob.companyName}

                            </p>
                        <#else>
                            <p class="text-gray-600 text-sm mt-3 italic">Опыт работы не указан</p>
                        </#if>
                    </div>
                    <div class="flex justify-between items-center mt-4">
                        <a href="/resumes/${resume.id}" class="text-blue-600 hover:text-blue-800 text-sm font-medium">Подробнее</a>
                    </div>
                </#list>
            <#else>
                <div class="bg-white rounded-lg shadow p-6 text-center">
                    <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
                    <h3 class="font-bold text-lg text-gray-800 mb-2">Резюме не найдены</h3>
                    <p class="text-gray-600">Попробуйте изменить параметры поиска или вернитесь позже.</p>
                </div>
            </#if>
        </div>
    </main>
</@main.layout>