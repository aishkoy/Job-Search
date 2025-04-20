<#import "/spring.ftl" as spring>
<#macro layout>

    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>JobSearch</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
        <script src="https://kit.fontawesome.com/9b45ec162a.js" crossorigin="anonymous"></script>
    </head>

    <body class="bg-gray-100 min-h-screen">

    <#assign known = SPRING_SECURITY_CONTEXT?? >
    <#if known>
        <#assign
        user = SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        roles = user.authorities
        >
    </#if>


    <header class="bg-blue-600 text-white p-4">
        <div class="container mx-auto flex justify-between items-center">
            <div>
                <a href="/" class="flex items-center">
                    <div class="bg-white rounded-full w-10 h-10 flex items-center justify-center mr-3">
                        <i class="fas fa-briefcase text-blue-600"></i>
                    </div>
                    <h1 class="text-xl font-bold">JobSearch</h1>
                </a>
            </div>

            <#if user??>
                <#if roles??>
                    <#list roles as role>
                        <#if role.authority == 'ROLE_EMPLOYER'>
                            <div class="flex items-center space-x-4">
                                <a href="/resumes" class="text-white hover:text-blue-200 flex items-center">
                                    <i class="fas fa-briefcase mr-2"></i> Резюме
                                </a>
                            </div>
                        </#if>
                        <#if role.authority == 'ROLE_APPLICANT'>
                            <div class="flex items-center space-x-4">
                                <a href="/vacancies" class="text-white hover:text-blue-200 flex items-center">
                                    <i class="fas fa-search mr-2"></i> Вакансии
                                </a>
                            </div>
                        </#if>
                    </#list>
                </#if>

                <div class="flex items-center space-x-4">
                    <a href="/profile" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-user mr-2"></i>
                    </a>
                    <a href="/logout" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-sign-out-alt mr-2"></i>
                    </a>
                </div>
            <#else>
                <div class="flex items-center space-x-4">
                    <a href="/auth/login" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-sign-in-alt mr-2"></i> Войти
                    </a>
                    <a href="/auth/register" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-user-plus mr-2"></i> Регистрация
                    </a>
                </div>
            </#if>
        </div>
    </header>

    <#nested spring>

    <footer class="bg-gray-800 text-white mt-12 py-8">
        <div class="container mx-auto px-4">
            <div class="grid md:grid-cols-4 gap-8">
                <div>
                    <h3 class="font-bold text-lg mb-4">О JobSearch</h3>
                    <p class="text-gray-400">Платформа для поиска работы и сотрудников. Мы соединяем талантливых
                        специалистов с лучшими компаниями.</p>
                </div>
                <div>
                    <h3 class="font-bold text-lg mb-4">Для соискателей</h3>
                    <ul class="space-y-2 text-gray-400">
                        <li><a href="#" class="hover:text-white">Поиск вакансий</a></li>
                        <li><a href="#" class="hover:text-white">Создать резюме</a></li>
                        <li><a href="#" class="hover:text-white">Советы по трудоустройству</a></li>
                        <li><a href="#" class="hover:text-white">Отзывы о компаниях</a></li>
                    </ul>
                </div>
                <div>
                    <h3 class="font-bold text-lg mb-4">Для работодателей</h3>
                    <ul class="space-y-2 text-gray-400">
                        <li><a href="#" class="hover:text-white">Разместить вакансию</a></li>
                        <li><a href="#" class="hover:text-white">Поиск резюме</a></li>
                        <li><a href="#" class="hover:text-white">Корпоративные решения</a></li>
                        <li><a href="#" class="hover:text-white">Цены и планы</a></li>
                    </ul>
                </div>
                <div>
                    <h3 class="font-bold text-lg mb-4">Контакты</h3>
                    <ul class="space-y-2 text-gray-400">
                        <li class="flex items-center">
                            <i class="fas fa-map-marker-alt mr-2"></i>
                            <span>ул. Главная, 123, Бишкек</span>
                        </li>
                        <li class="flex items-center">
                            <i class="fas fa-phone mr-2"></i>
                            <span>+996 555 123 456</span>
                        </li>
                        <li class="flex items-center">
                            <i class="fas fa-envelope mr-2"></i>
                            <span>info@jobsearch.kg</span>
                        </li>
                    </ul>
                    <div class="flex space-x-4 mt-4">
                        <a href="#" class="text-gray-400 hover:text-white">
                            <i class="fab fa-facebook"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white">
                            <i class="fab fa-twitter"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white">
                            <i class="fab fa-linkedin"></i>
                        </a>
                        <a href="#" class="text-gray-400 hover:text-white">
                            <i class="fab fa-instagram"></i>
                        </a>
                    </div>
                </div>
            </div>
            <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
                <p>&copy; 2025 JobSearch. Все права защищены.</p>
            </div>
        </div>
    </footer>
    </body>
    </html>
</#macro>