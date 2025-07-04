<#import "/spring.ftl" as spring>
<#macro layout showLanguageSwitcher=true>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>JobSearch</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
        <script src="https://kit.fontawesome.com/9b45ec162a.js" crossorigin="anonymous"></script>
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>
    </head>

    <body class="bg-gray-100 min-h-screen">
    <#assign known = SPRING_SECURITY_CONTEXT?? >
    <#if known>
        <#assign
        user = SPRING_SECURITY_CONTEXT.authentication.principal
        currentUserId = user.getUserId()
        roles = user.authorities
        >
    </#if>

    <header class="bg-blue-600 text-white p-4">
        <div class="container mx-auto">
            <!-- Mobile menu button -->
            <div class="flex justify-between items-center md:hidden">
                <a href="/" class="flex items-center">
                    <div class="bg-white rounded-full w-8 h-8 flex items-center justify-center mr-2">
                        <i class="fas fa-briefcase text-blue-600 text-sm"></i>
                    </div>
                    <h1 class="text-lg font-bold">JobSearch</h1>
                </a>
                <button id="mobile-menu-button" class="text-white hover:text-blue-200">
                    <i class="fas fa-bars text-xl"></i>
                </button>
            </div>

            <!-- Desktop navigation -->
            <div class="hidden md:flex justify-between items-center">
                <div class="flex items-center gap-6">
                    <a href="/" class="flex items-center">
                        <div class="bg-white rounded-full w-10 h-10 flex items-center justify-center mr-3">
                            <i class="fas fa-briefcase text-blue-600"></i>
                        </div>
                        <h1 class="text-xl font-bold">JobSearch</h1>
                    </a>
                    <#if showLanguageSwitcher>
                        <div class="relative inline-block text-left language-switcher">
                            <div>
                                <button type="button"
                                        class="cursor-pointer inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-white hover:text-blue-200"
                                        id="language-menu-button" aria-expanded="false" aria-haspopup="true">
                                    <#if springMacroRequestContext.locale.language == 'ru'>
                                        ru
                                    <#elseif springMacroRequestContext.locale.language == 'en'>
                                        en
                                    <#elseif springMacroRequestContext.locale.language == 'el'>
                                        el
                                    <#else>
                                        Выбрать язык
                                    </#if>
                                    <svg class="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                         fill="currentColor" aria-hidden="true">
                                        <path fill-rule="evenodd"
                                              d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                              clip-rule="evenodd"/>
                                    </svg>
                                </button>
                            </div>

                            <div class="hidden origin-top-right absolute right-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-blue-200 ring-opacity-5 focus:outline-none"
                                 role="menu" aria-orientation="vertical" aria-labelledby="language-menu-button"
                                 tabindex="-1"
                                 id="language-dropdown">
                                <div class="py-1" role="none">
                                    <a href="#" data-lang="ru"
                                       class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100 <#if springMacroRequestContext.locale.language == 'ru'>bg-gray-100 font-medium</#if>"
                                       role="menuitem" tabindex="-1">
                                        <span class="inline-block w-5 mr-2">🇷🇺</span> Русский
                                    </a>
                                    <a href="#" data-lang="en"
                                       class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100 <#if springMacroRequestContext.locale.language == 'en'>bg-gray-100 font-medium</#if>"
                                       role="menuitem" tabindex="-1">
                                        <span class="inline-block w-5 mr-2">🇬🇧</span> English
                                    </a>
                                    <a href="#" data-lang="el"
                                       class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100 <#if springMacroRequestContext.locale.language == 'el'>bg-gray-100 font-medium</#if>"
                                       role="menuitem" tabindex="-1">
                                        <span class="inline-block w-5 mr-2">🇬🇷</span> Ελληνικά
                                    </a>
                                </div>
                            </div>
                        </div>
                    </#if>
                </div>

                <#if user??>
                    <#if roles??>
                        <div class="flex items-center space-x-4">
                            <#list roles as role>
                                <#if role.authority == 'ROLE_EMPLOYER'>
                                    <div class="flex items-center space-x-4">
                                        <a href="/resumes" class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-briefcase mr-2"></i>
                                            <span class="hidden lg:inline"><@spring.message "layout.nav.resumes"/></span>
                                        </a>
                                        <a href="/applicants" class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-users mr-2"></i>
                                            <span class="hidden lg:inline"><@spring.message "layout.nav.applicants"/></span>
                                        </a>
                                        <a href="/chat" class="text-white hover:text-blue-200 flex items-center relative">
                                            <i class="fas fa-comments mr-2"></i>
                                        </a>
                                    </div>
                                    <div class="flex items-center space-x-4">
                                        <a href="/employers/${currentUserId}"
                                           class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-user mr-2"></i>
                                        </a>
                                    </div>
                                </#if>
                                <#if role.authority == 'ROLE_APPLICANT'>
                                    <div class="flex items-center space-x-4">
                                        <a href="/vacancies" class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-search mr-2"></i>
                                            <span class="hidden lg:inline"><@spring.message "layout.nav.vacancies"/></span>
                                        </a>
                                        <a href="/employers" class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-users mr-2"></i>
                                            <span class="hidden lg:inline"><@spring.message "layout.nav.employers"/></span>
                                        </a>
                                        <a href="/chat" class="text-white hover:text-blue-200 flex items-center relative">
                                            <i class="fas fa-comments mr-2"></i>
                                        </a>
                                    </div>
                                    <div class="flex items-center space-x-4">
                                        <a href="/applicants/${currentUserId}"
                                           class="text-white hover:text-blue-200 flex items-center">
                                            <i class="fas fa-user mr-2"></i>
                                        </a>
                                    </div>
                                </#if>
                            </#list>

                            <#if _csrf??>
                                <form action="/logout" method="post">
                                    <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                                           value="${(_csrf.token)!'--no-token--'}"/>
                                    <button type="submit" class="text-white hover:text-blue-200 flex items-center"
                                            id="logout">
                                        <i class="fas fa-sign-out-alt"></i>
                                    </button>
                                </form>
                            <#else>
                                <a href="/logout" class="text-white hover:text-blue-200 flex items-center">
                                    <i class="fas fa-sign-out-alt"></i>
                                </a>
                            </#if>
                        </div>
                    </#if>
                <#else>
                    <div class="flex items-center space-x-4">
                        <a href="/auth/login" class="text-white hover:text-blue-200 flex items-center">
                            <i class="fas fa-sign-in-alt mr-2"></i>
                            <span class="hidden lg:inline"><@spring.message "layout.nav.logIn"/></span>
                        </a>
                        <a href="/auth/register" class="text-white hover:text-blue-200 flex items-center">
                            <i class="fas fa-user-plus mr-2"></i>
                            <span class="hidden lg:inline"><@spring.message "layout.nav.register"/></span>
                        </a>
                    </div>
                </#if>
            </div>

            <!-- Mobile navigation menu -->
            <div id="mobile-menu" class="hidden md:hidden mt-4 border-t border-blue-500 pt-4">
                <div class="flex flex-col space-y-3">
                    <#if showLanguageSwitcher>
                        <div class="flex items-center justify-between">
                            <span class="text-sm font-medium">Язык:</span>
                            <div class="relative inline-block text-left language-switcher-mobile">
                                <button type="button"
                                        class="cursor-pointer inline-flex justify-center px-3 py-1 text-sm font-medium text-white border border-blue-400 rounded hover:bg-blue-500"
                                        id="mobile-language-menu-button">
                                    <#if springMacroRequestContext.locale.language == 'ru'>
                                        🇷🇺 ru
                                    <#elseif springMacroRequestContext.locale.language == 'en'>
                                        🇬🇧 en
                                    <#elseif springMacroRequestContext.locale.language == 'el'>
                                        🇬🇷 el
                                    <#else>
                                        Выбрать
                                    </#if>
                                </button>
                                <div class="hidden origin-top-right absolute right-0 mt-2 w-40 rounded-md shadow-lg bg-white ring-1 ring-blue-200 ring-opacity-5"
                                     id="mobile-language-dropdown">
                                    <div class="py-1">
                                        <a href="#" data-lang="ru" class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100">
                                            🇷🇺 Русский
                                        </a>
                                        <a href="#" data-lang="en" class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100">
                                            🇬🇧 English
                                        </a>
                                        <a href="#" data-lang="el" class="language-option text-gray-700 block px-4 py-2 text-sm hover:bg-gray-100">
                                            🇬🇷 Ελληνικά
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#if>

                    <#if user??>
                        <#if roles??>
                            <#list roles as role>
                                <#if role.authority == 'ROLE_EMPLOYER'>
                                    <a href="/resumes" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-briefcase mr-3 w-5"></i> <@spring.message "layout.nav.resumes"/>
                                    </a>
                                    <a href="/applicants" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-users mr-3 w-5"></i> <@spring.message "layout.nav.applicants"/>
                                    </a>
                                    <a href="/chat" class="text-white hover:text-blue-200 flex items-center py-2 relative">
                                        <i class="fas fa-comments mr-3 w-5"></i> Чат
                                    </a>
                                    <a href="/employers/${currentUserId}" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-user mr-3 w-5"></i> Профиль
                                    </a>
                                </#if>
                                <#if role.authority == 'ROLE_APPLICANT'>
                                    <a href="/vacancies" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-search mr-3 w-5"></i> <@spring.message "layout.nav.vacancies"/>
                                    </a>
                                    <a href="/employers" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-users mr-3 w-5"></i> <@spring.message "layout.nav.employers"/>
                                    </a>
                                    <a href="/chat" class="text-white hover:text-blue-200 flex items-center py-2 relative">
                                        <i class="fas fa-comments mr-3 w-5"></i> Чат
                                    </a>
                                    <a href="/applicants/${currentUserId}" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-user mr-3 w-5"></i> Профиль
                                    </a>
                                </#if>
                            </#list>

                            <div class="border-t border-blue-500 pt-3 mt-3">
                                <#if _csrf??>
                                    <form action="/logout" method="post">
                                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                                               value="${(_csrf.token)!'--no-token--'}"/>
                                        <button type="submit" class="text-white hover:text-blue-200 flex items-center py-2 w-full text-left">
                                            <i class="fas fa-sign-out-alt mr-3 w-5"></i> Выйти
                                        </button>
                                    </form>
                                <#else>
                                    <a href="/logout" class="text-white hover:text-blue-200 flex items-center py-2">
                                        <i class="fas fa-sign-out-alt mr-3 w-5"></i> Выйти
                                    </a>
                                </#if>
                            </div>
                        </#if>
                    <#else>
                        <a href="/auth/login" class="text-white hover:text-blue-200 flex items-center py-2">
                            <i class="fas fa-sign-in-alt mr-3 w-5"></i> <@spring.message "layout.nav.logIn"/>
                        </a>
                        <a href="/auth/register" class="text-white hover:text-blue-200 flex items-center py-2">
                            <i class="fas fa-user-plus mr-3 w-5"></i> <@spring.message "layout.nav.register"/>
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    </header>

    <#nested spring>

    <footer class="bg-gray-800 text-white mt-12 py-8">
        <div class="container mx-auto px-4">
            <#if !known>
                <div class="grid md:grid-cols-4 gap-8">
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.about"/></h3>
                        <p class="text-gray-400"><@spring.message "layout.footer.about.description"/></p>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.applicants"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li><a href="/vacancies" class="hover:text-white"><@spring.message "layout.footer.vacancies.search"/></a></li>
                            <li><a href="/resumes/create" class="hover:text-white"><@spring.message "layout.footer.resume.create"/></a></li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.employers"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li><a href="/vacancies/create" class="hover:text-white"><@spring.message "layout.footer.vacancy.create"/></a></li>
                            <li><a href="/resumes" class="hover:text-white"><@spring.message "layout.footer.resume.search"/></a></li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.contacts"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li class="flex items-center">
                                <i class="fas fa-map-marker-alt mr-2"></i>
                                <span><@spring.message "layout.footer.address"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-phone mr-2"></i>
                                <span><@spring.message "layout.footer.phone"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-envelope mr-2"></i>
                                <span><@spring.message "layout.footer.email"/></span>
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
            <#elseif roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                <!-- Соискатели - показываем только информацию для соискателей -->
                <div class="grid md:grid-cols-3 gap-8">
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.about"/></h3>
                        <p class="text-gray-400"><@spring.message "layout.footer.about.description"/></p>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.applicants"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li><a href="/vacancies" class="hover:text-white"><@spring.message "layout.footer.vacancies.search"/></a></li>
                            <li><a href="/resumes/create" class="hover:text-white"><@spring.message "layout.footer.resume.create"/></a></li>
                            <li><a href="/employers" class="hover:text-white">Просмотр работодателей</a></li>
                            <li><a href="/applicants/${currentUserId}" class="hover:text-white">Мой профиль</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.contacts"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li class="flex items-center">
                                <i class="fas fa-map-marker-alt mr-2"></i>
                                <span><@spring.message "layout.footer.address"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-phone mr-2"></i>
                                <span><@spring.message "layout.footer.phone"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-envelope mr-2"></i>
                                <span><@spring.message "layout.footer.email"/></span>
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
            <#elseif roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                <!-- Работодатели - показываем только информацию для работодателей -->
                <div class="grid md:grid-cols-3 gap-8">
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.about"/></h3>
                        <p class="text-gray-400"><@spring.message "layout.footer.about.description"/></p>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.employers"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li><a href="/vacancies/create" class="hover:text-white"><@spring.message "layout.footer.vacancy.create"/></a></li>
                            <li><a href="/resumes" class="hover:text-white"><@spring.message "layout.footer.resume.search"/></a></li>
                            <li><a href="/applicants" class="hover:text-white">Просмотр соискателей</a></li>
                            <li><a href="/employers/${currentUserId}" class="hover:text-white">Мой профиль</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.contacts"/></h3>
                        <ul class="space-y-2 text-gray-400">
                            <li class="flex items-center">
                                <i class="fas fa-map-marker-alt mr-2"></i>
                                <span><@spring.message "layout.footer.address"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-phone mr-2"></i>
                                <span><@spring.message "layout.footer.phone"/></span>
                            </li>
                            <li class="flex items-center">
                                <i class="fas fa-envelope mr-2"></i>
                                <span><@spring.message "layout.footer.email"/></span>
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
            </#if>

            <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
                <p>&copy; <@spring.message "layout.footer.copyright"/></p>
            </div>
        </div>
    </footer>

    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <script src="/static/js/language-switcher.js"></script>
    <script>
        document.getElementById('mobile-menu-button').addEventListener('click', function() {
            const mobileMenu = document.getElementById('mobile-menu');
            const button = this;
            const icon = button.querySelector('i');

            if (mobileMenu.classList.contains('hidden')) {
                mobileMenu.classList.remove('hidden');
                icon.classList.remove('fa-bars');
                icon.classList.add('fa-times');
            } else {
                mobileMenu.classList.add('hidden');
                icon.classList.remove('fa-times');
                icon.classList.add('fa-bars');
            }
        });

            const mobileLanguageButton = document.getElementById('mobile-language-menu-button');
            const mobileLanguageDropdown = document.getElementById('mobile-language-dropdown');

            if (mobileLanguageButton && mobileLanguageDropdown) {
                mobileLanguageButton.addEventListener('click', function() {
                    mobileLanguageDropdown.classList.toggle('hidden');
                });
            }
    </script>
    </body>
    </html>
</#macro>

<#macro csrf>
    <#if _csrf??>
        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
               value="${(_csrf.token)!'csrf-token'}"/>
    </#if>
</#macro>