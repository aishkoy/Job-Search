<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JobSearch - Резюме</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
</head>
<body class="bg-gray-100 min-h-screen pb-16">
<header class="bg-blue-600 text-white p-4">
    <div class="container mx-auto flex justify-between items-center">
        <div class="flex items-center">
            <div class="bg-white rounded-full w-10 h-10 flex items-center justify-center mr-3">
                <i class="fas fa-briefcase text-blue-600"></i>
            </div>
            <h1 class="text-xl font-bold">JobSearch</h1>
        </div>
        <div class="flex items-center space-x-4">
            <a href="/profile" class="text-white hover:text-blue-200">
                <i class="fas fa-user"></i>
            </a>
        </div>
    </div>
</header>

<div class="bg-blue-600 text-white pb-6">
    <div class="container mx-auto px-4">
        <form action="#" method="get">
            <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                <input
                        type="text"
                        name="query"
                        placeholder="Поиск резюме, специалистов..."
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
                            <#if applicantNames?? && applicantNames[resume.applicantId?string]??>
                                <p class="text-gray-600 mb-2">${applicantNames[resume.applicantId?string]}</p>
                            <#else>
                                <p class="text-gray-600 mb-2">Специалист</p>
                            </#if>
                            <div class="text-blue-600 font-medium">
                                <#if resume.salary?? && resume.salary gt 0>
                                    ${resume.salary?string(",##0")} ₽
                                <#else>
                                    Ожидаемая зарплата не указана
                                </#if>
                            </div>
                        </div>
                        <div class="flex flex-col items-end justify-between">
                            <div class="bg-blue-100 h-12 w-12 rounded-lg flex items-center justify-center">
                                <#if categoryIcons?? && categoryIcons[resume.categoryId?string]??>
                                    <i class="${categoryIcons[resume.categoryId?string]} text-blue-600"></i>
                                <#else>
                                    <i class="fas fa-user-tie text-blue-600"></i>
                                </#if>
                            </div>
                            <span class="text-xs text-gray-400 mt-4">
                                <#if resume.updateTime??>
                                    Обновлено: ${resume.updateTime?datetime?string("dd.MM.yyyy")}
                                <#elseif resume.createdDate??>
                                    Создано: ${resume.createdDate?datetime?string("dd.MM.yyyy")}
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

                        <#if categories?? && categories[resume.categoryId?string]??>
                            <span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                ${categories[resume.categoryId?string]}
                            </span>
                        </#if>

                        <#if resume.isActive?? && resume.isActive>
                            <span class="bg-green-100 text-green-600 text-xs px-2 py-1 rounded">Активно</span>
                        <#else>
                            <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">Не активно</span>
                        </#if>
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

<footer class="fixed bottom-0 w-full bg-white border-t shadow-md py-2">
    <div class="container mx-auto">
        <div class="flex justify-around">
            <a href="/profile" class="flex flex-col items-center text-gray-500">
                <i class="fas fa-user"></i>
                <span class="text-xs mt-1">Профиль</span>
            </a>
        </div>
    </div>
</footer>
</body>
</html>