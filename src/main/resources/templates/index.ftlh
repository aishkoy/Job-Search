<#import "layout.ftlh" as main>

<@main.layout>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/locale/ru.min.js"></script>

    <div class="container mx-auto px-4 py-8">
        <section class="bg-white rounded-lg shadow-lg overflow-hidden mb-12">
            <div class="flex flex-col md:flex-row">
                <div class="md:w-1/2 p-8 md:p-12 flex flex-col justify-center">
                    <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-4">Найдите работу своей мечты</h1>
                    <p class="text-gray-600 mb-6">Тысячи вакансий и резюме. Начните свой карьерный путь прямо
                        сейчас!</p>
                    <div class="flex flex-col sm:flex-row gap-4">
                        <a href="/vacancies"
                           class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                            Найти вакансию
                        </a>
                        <a href="/resumes"
                           class="bg-white border border-blue-600 hover:bg-blue-50 text-blue-600 font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                            Найти соискателя
                        </a>
                    </div>
                </div>

                <#--                TODO-->
                <div class="md:w-1/2 bg-blue-600 p-8 md:p-12 text-white">
                    <div class="bg-white/10 backdrop-blur-sm rounded-lg p-6">
                        <h2 class="text-xl font-semibold mb-4">Быстрый поиск</h2>
                        <form action="/search" method="GET">
                            <div class="mb-4">
                                <label for="keyword" class="block text-sm font-medium mb-1">Ключевые слова</label>
                                <input type="text" id="keyword" name="keyword"
                                       placeholder="Должность, навыки или компания"
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
<#--            TODO-->
            <h2 class="text-2xl font-bold text-gray-800 mb-6">Популярные категории</h2>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <a href="/vacancies?category=it"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-laptop-code text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">IT и разработка</h3>
                    <p class="text-sm text-gray-500 mt-1">1,234 вакансий</p>
                </a>
                <a href="/vacancies?category=finance"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-chart-line text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Финансы</h3>
                    <p class="text-sm text-gray-500 mt-1">876 вакансий</p>
                </a>
                <a href="/vacancies?category=marketing"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-bullhorn text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Маркетинг</h3>
                    <p class="text-sm text-gray-500 mt-1">543 вакансий</p>
                </a>
                <a href="/vacancies?category=medical"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-stethoscope text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800">Медицина</h3>
                    <p class="text-sm text-gray-500 mt-1">421 вакансий</p>
                </a>
            </div>
        </section>

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
                                    <p class="text-gray-600">${vacancy.authorName}</p>
                                </div>
                                <span class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded">Полная занятость</span>
                            </div>
                            <div class="flex items-center text-gray-500 text-sm mb-4">
                                <#--                                TODO-->
                                <i class="fas fa-map-marker-alt mr-2"></i>
                                <span>Бишкек</span>
                                <span class="mx-2">•</span>
                                <i class="fas fa-dollar-sign mr-2"></i>
                                <span>${vacancy.salary/85}$</span>
                            </div>
                            <p class="text-gray-600 mb-4 line-clamp-2">${vacancy.description}</p>
                            <div class="flex flex-wrap gap-2 mb-4">
                                <#--                                TODO-->
                                <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">Java</span>
                                <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">Spring</span>
                                <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">PostgreSQL</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-sm text-gray-500 date-from-now"
                                      data-timestamp="${vacancy.createdDate?long?c}">
                                Опубликовано ${vacancy.createdDate?datetime}
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

        <section class="bg-white rounded-lg shadow-lg overflow-hidden mb-12">
            <div class="p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-8 text-center">Как это работает</h2>
                <div class="grid md:grid-cols-3 gap-8">
                    <a href="/auth/register">
                        <div class="flex flex-col items-center text-center">
                            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                <i class="fas fa-user-plus text-blue-600 text-2xl"></i>
                            </div>
                            <h3 class="font-bold text-lg text-gray-800 mb-2">Создайте аккаунт</h3>
                            <p class="text-gray-600">Зарегистрируйтесь и создайте профиль работодателя или
                                соискателя</p>
                        </div>
                    </a>
                    <a href="profile">
                        <div class="flex flex-col items-center text-center">
                            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                <i class="fas fa-file-alt text-blue-600 text-2xl"></i>
                            </div>
                            <h3 class="font-bold text-lg text-gray-800 mb-2">Создайте резюме или вакансию</h3>
                            <p class="text-gray-600">Добавьте информацию о себе или о вашей компании и требуемой
                                позиции</p>
                        </div>
                    </a>
                    <a href="vacancies">
                        <div class="flex flex-col items-center text-center">
                            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                <i class="fas fa-handshake text-blue-600 text-2xl"></i>
                            </div>
                            <h3 class="font-bold text-lg text-gray-800 mb-2">Найдите подходящие варианты</h3>
                            <p class="text-gray-600">Используйте поиск и фильтры для нахождения идеального
                                соответствия</p>
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
<#--            TODO-->
            <div class="p-8 text-white text-center">
                <h2 class="text-2xl font-bold mb-4">Получайте новые вакансии на почту</h2>
                <p class="mb-6 max-w-2xl mx-auto">Подпишитесь на нашу рассылку и получайте уведомления о новых
                    вакансиях, соответствующих вашим навыкам и интересам</p>
                <form class="flex flex-col sm:flex-row gap-4 max-w-md mx-auto">
                    <input type="email" placeholder="Ваш email"
                           class="px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white flex-grow focus:outline-none focus:ring-2 focus:ring-white/50">
                    <button type="submit"
                            class="bg-white text-blue-600 hover:bg-blue-50 font-medium py-2 px-6 rounded-md transition duration-200 whitespace-nowrap">
                        Подписаться
                    </button>
                </form>
            </div>
        </section>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            moment.locale('ru');

            document.querySelectorAll('.date-from-now').forEach(element => {
                const timestamp = parseInt(element.getAttribute('data-timestamp'));
                element.textContent = 'Опубликовано ' + moment(timestamp).fromNow();
            });
        });
    </script>
</@main.layout>