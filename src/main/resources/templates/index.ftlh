<#import "layout.ftlh" as main>

<#assign known = SPRING_SECURITY_CONTEXT?? >
<#if known>
    <#assign
    user = SPRING_SECURITY_CONTEXT.authentication.principal
    currentUserId = user.getUserId()
    roles = user.authorities
    >
</#if>

<@main.layout>
    <div class="container mx-auto px-4 py-8">
        <section class="bg-white rounded-lg shadow-lg overflow-hidden mb-12">
            <div class="flex flex-col md:flex-row">
                <div class="md:w-1/2 p-8 md:p-12 flex flex-col justify-center">
                    <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-4">
                        <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                            Найдите работу своей мечты
                        <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                            Найдите идеальных сотрудников
                        <#else>
                            Добро пожаловать на JobSearch
                        </#if>
                    </h1>
                    <p class="text-gray-600 mb-6">
                        <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                            Тысячи вакансий ждут вас. Найдите идеальную работу уже сегодня!
                        <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                            Тысячи специалистов ищут работу. Найдите идеального сотрудника уже сегодня!
                        <#else>
                            Тысячи вакансий и резюме. Начните свой карьерный путь прямо сейчас!
                        </#if>
                    </p>
                    <div class="flex flex-col sm:flex-row gap-4">
                        <#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')>
                            <a href="/vacancies"
                               class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                                Найти вакансию
                            </a>
                        </#if>
                        <#if !known || (roles?? && roles?first.authority == 'ROLE_EMPLOYER')>
                            <a href="/resumes"
                               class="<#if !known || (roles?? && roles?first.authority == 'ROLE_EMPLOYER')>bg-blue-600 hover:bg-blue-700 text-white<#else>bg-white border border-blue-600 hover:bg-blue-50 text-blue-600</#if> font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                                Найти соискателя
                            </a>
                        </#if>
                    </div>
                </div>

                <div class="md:w-1/2 bg-blue-600 p-8 md:p-12 text-white">
                    <div class="bg-white/10 backdrop-blur-sm rounded-lg p-6">
                        <h2 class="text-xl font-semibold mb-4">Быстрый поиск</h2>
                        <form action="/search" method="GET">
                            <@main.csrf/>
                            <div class="mb-4">
                                <label for="keyword" class="block text-sm font-medium mb-1">Ключевые слова</label>
                                <input type="text" id="keyword" name="keyword"
                                       placeholder="<#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')>Должность, навыки или компания<#else>Специальность, навыки или опыт</#if>"
                                       class="w-full px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white focus:outline-none focus:ring-2 focus:ring-white/50">
                            </div>
                            <div class="mb-4">
                                <label for="location" class="block text-sm font-medium mb-1">Местоположение</label>
                                <input type="text" id="location" name="location" placeholder="Город или регион"
                                       class="w-full px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white focus:outline-none focus:ring-2 focus:ring-white/50">
                            </div>
                            <button type="submit"
                                    class="w-full bg-white text-blue-600 hover:bg-blue-50 font-medium py-2 px-4 rounded-md transition duration-200">
                                <i class="fas fa-search mr-2"></i> Поиск
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>

        <section class="mb-12">
            <h2 class="text-2xl font-bold text-gray-800 mb-6">Популярные категории</h2>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <a href="/vacancies/categories?category=it"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-laptop-code text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">IT и разработка</h3>
                    <p class="text-sm text-gray-500 mt-1">1,234 вакансий</p>
                </a>
                <a href="/vacancies/categories?category=finance"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-chart-line text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Финансы</h3>
                    <p class="text-sm text-gray-500 mt-1">876 вакансий</p>
                </a>
                <a href="/vacancies/categories?category=marketing"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-bullhorn text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Маркетинг</h3>
                    <p class="text-sm text-gray-500 mt-1">543 вакансий</p>
                </a>
                <a href="/vacancies/categories?category=medical"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-stethoscope text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Медицина</h3>
                    <p class="text-sm text-gray-500 mt-1">421 вакансий</p>
                </a>
            </div>
        </section>

        <!-- Показываем раздел в зависимости от роли пользователя -->
        <#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')>
            <section class="mb-12">
                <div class="flex justify-between items-center mb-6">
                    <h2 class="text-2xl font-bold text-gray-800">Новые вакансии</h2>
                    <a href="/vacancies" class="text-blue-600 hover:text-blue-800 font-medium">
                        Все вакансии <i class="fas fa-arrow-right ml-1"></i>
                    </a>
                </div>
                <div class="grid md:grid-cols-2 gap-6">
                    <#list vacancies as vacancy>
                        <div class="bg-white rounded-lg shadow overflow-hidden transition hover:shadow-md">
                            <div class="p-6">
                                <div class="flex justify-between items-start mb-4">
                                    <div>
                                        <h3 class="font-bold text-lg text-gray-800">${vacancy.name}</h3>
                                        <p class="text-gray-600">${vacancy.employer.name}</p>
                                    </div>
                                    <span class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded">Полная занятость</span>
                                </div>
                                <div class="flex items-center text-gray-500 text-sm mb-4">
                                    <i class="fas fa-map-marker-alt mr-2"></i>
                                    <span>Бишкек</span>
                                    <span class="mx-2">•</span>
                                    <i class="fas fa-dollar-sign mr-2"></i>
                                    <span>${vacancy.salary} c</span>
                                </div>
                                <p class="text-gray-600 mb-4 line-clamp-2">${vacancy.description}</p>
                                <div class="flex flex-wrap gap-2 mb-4">
                                    <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">Java</span>
                                    <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">Spring</span>
                                    <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">PostgreSQL</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-sm text-gray-500">
                                        Опубликовано ${vacancy.createdAt?string("dd.MM.yyyy")}
                                    </span>
                                    <a href="/vacancies/${vacancy.id}"
                                       class="text-blue-600 hover:text-blue-800 font-medium">
                                        Подробнее
                                    </a>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </section>
        <#elseif roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
            <section class="mb-12">
                <div class="flex justify-between items-center mb-6">
                    <h2 class="text-2xl font-bold text-gray-800">Новые резюме</h2>
                    <a href="/resumes" class="text-blue-600 hover:text-blue-800 font-medium">
                        Все резюме <i class="fas fa-arrow-right ml-1"></i>
                    </a>
                </div>
                <div class="grid md:grid-cols-2 gap-6">
                    <#list resumes as resume>
                        <div class="bg-white rounded-lg shadow overflow-hidden transition hover:shadow-md">
                            <div class="p-6">
                                <div class="flex justify-between items-start mb-4">
                                    <div>
                                        <h3 class="font-bold text-lg text-gray-800">${resume.name}</h3>
                                        <p class="text-gray-600">${resume.applicant.name}</p>
                                    </div>
                                    <span class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded">
                                        <#if resume.isActive>Активно ищет работу<#else>Не ищет работу</#if>
                                    </span>
                                </div>
                                <div class="flex items-center text-gray-500 text-sm mb-4">
                                    <i class="fas fa-map-marker-alt mr-2"></i>
                                    <span>Бишкек</span>
                                    <#if resume.salary?? && resume.salary gt 0>
                                        <span class="mx-2">•</span>
                                        <i class="fas fa-dollar-sign mr-2"></i>
                                        <span>${resume.salary} c</span>
                                    </#if>
                                </div>
                                <div class="flex flex-wrap gap-2 mb-4">
                                    <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">${resume.category.name}</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-sm text-gray-500">
                                        Опубликовано ${resume.createdAt?string("dd.MM.yyyy")}
                                    </span>
                                    <a href="/resumes/${resume.id}"
                                       class="text-blue-600 hover:text-blue-800 font-medium">
                                        Подробнее
                                    </a>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </section>
        </#if>

        <section class="bg-white rounded-lg shadow-lg overflow-hidden mb-12">
            <div class="p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-8 text-center">Как это работает</h2>
                <div class="grid md:grid-cols-3 gap-8">
                    <#if !currentUserId??>
                        <a href="/auth/register">
                            <div class="flex flex-col items-center text-center">
                                <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                    <i class="fas fa-user-plus text-blue-600 text-2xl"></i>
                                </div>
                                <h3 class="font-bold text-lg text-gray-800 mb-2">Создайте аккаунт</h3>
                                <p class="text-gray-600">
                                    Зарегистрируйтесь и создайте профиль работодателя или соискателя
                                </p>
                            </div>
                        </a>
                    <#else>
                        <a href="<#if roles?? && roles?first.authority == 'ROLE_APPLICANT'>/applicants/${currentUserId}<#else>/employers/${currentUserId}</#if>">
                            <div class="flex flex-col items-center text-center">
                                <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                    <i class="fas fa-user text-blue-600 text-2xl"></i>
                                </div>
                                <h3 class="font-bold text-lg text-gray-800 mb-2">Перейдите в профиль</h3>
                                <p class="text-gray-600">
                                    Управляйте своими данными и настройками аккаунта
                                </p>
                            </div>
                        </a>
                    </#if>
                    <a href="<#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>/resumes/create<#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>/vacancies/create<#else>profile</#if>">
                        <div class="flex flex-col items-center text-center">
                            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                <i class="fas fa-file-alt text-blue-600 text-2xl"></i>
                            </div>
                            <h3 class="font-bold text-lg text-gray-800 mb-2">
                                <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                                    Создайте резюме
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    Создайте вакансию
                                <#else>
                                    Создайте резюме или вакансию
                                </#if>
                            </h3>
                            <p class="text-gray-600">
                                <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                                    Добавьте информацию о своем опыте, образовании и навыках
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    Добавьте информацию о требуемой позиции и условиях работы
                                <#else>
                                    Добавьте информацию о себе или о вашей компании и требуемой позиции
                                </#if>
                            </p>
                        </div>
                    </a>
                    <a href="<#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>vacancies<#else>resumes</#if>">
                        <div class="flex flex-col items-center text-center">
                            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                <i class="fas fa-handshake text-blue-600 text-2xl"></i>
                            </div>
                            <h3 class="font-bold text-lg text-gray-800 mb-2">
                                Найдите подходящие варианты
                            </h3>
                            <p class="text-gray-600">
                                <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                                    Используйте поиск и фильтры для нахождения идеальной вакансии
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    Используйте поиск и фильтры для нахождения идеального кандидата
                                <#else>
                                    Используйте поиск и фильтры для нахождения идеального соответствия
                                </#if>
                            </p>
                        </div>
                    </a>
                </div>
            </div>
        </section>

        <section class="mb-12">
            <h2 class="text-2xl font-bold text-gray-800 mb-6">Ведущие работодатели</h2>
            <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6 gap-4">
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fab fa-google text-4xl text-gray-700"></i>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fab fa-microsoft text-4xl text-gray-700"></i>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fab fa-apple text-4xl text-gray-700"></i>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fab fa-amazon text-4xl text-gray-700"></i>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fab fa-facebook text-4xl text-gray-700"></i>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-4 flex items-center justify-center">
                    <div class="w-16 h-16 flex items-center justify-center">
                        <i class="fa-brands fa-yandex text-4xl text-gray-700"></i>
                    </div>
                </div>
            </div>
        </section>

        <section class="bg-blue-600 rounded-lg shadow-lg overflow-hidden">
            <div class="p-8 text-white text-center">
                <h2 class="text-2xl font-bold mb-4">
                    <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                        Получайте новые вакансии на почту
                    <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                        Получайте новые резюме на почту
                    <#else>
                        Получайте новые вакансии на почту
                    </#if>
                </h2>
                <p class="mb-6 max-w-2xl mx-auto">
                    <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                        Подпишитесь на нашу рассылку и получайте уведомления о новых вакансиях, соответствующих вашим навыкам и интересам
                    <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                        Подпишитесь на нашу рассылку и получайте уведомления о новых кандидатах, соответствующих вашим требованиям
                    <#else>
                        Подпишитесь на нашу рассылку и получайте уведомления о новых вакансиях, соответствующих вашим навыкам и интересам
                    </#if>
                </p>
                <form class="flex flex-col sm:flex-row gap-4 max-w-md mx-auto" method="post" action="/subscribe">
                    <@main.csrf/>
                    <label>
                        <input type="email" placeholder="Ваш email" name="email"
                               class="px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white flex-grow focus:outline-none focus:ring-2 focus:ring-white/50">
                    </label>
                    <button type="submit"
                            class="bg-white text-blue-600 hover:bg-blue-50 font-medium py-2 px-6 rounded-md transition duration-200 whitespace-nowrap">
                        Подписаться
                    </button>
                </form>
            </div>
        </section>
    </div>
</@main.layout>