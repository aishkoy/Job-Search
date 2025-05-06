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
        currentUserId = user.getUserId()
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
                    <div class="flex items-center space-x-4">
                        <#list roles as role>
                            <#if role.authority == 'ROLE_EMPLOYER'>
                                <div class="flex items-center space-x-4">
                                    <a href="/resumes" class="text-white hover:text-blue-200 flex items-center">
                                        <i class="fas fa-briefcase mr-2"></i> <@spring.message "layout.nav.resumes"/>
                                    </a>
                                    <a href="/applicants" class="text-white hover:text-blue-200 flex items-center">
                                        <i class="fas fa-users mr-2"></i> <@spring.message "layout.nav.applicants"/>
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
                                        <i class="fas fa-search mr-2"></i> <@spring.message "layout.nav.vacancies"/>
                                    </a>
                                    <a href="/employers" class="text-white hover:text-blue-200 flex items-center">
                                        <i class="fas fa-users mr-2"></i> <@spring.message "layout.nav.employers"/>
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
                                <button type="submit" class="text-white hover:text-blue-200 flex items-center" id="logout">
                                    <i class="fas fa-sign-out-alt mr-2"></i>
                                </button>
                            </form>
                        <#else>
                            <a href="/logout" class="text-white hover:text-blue-200 flex items-center">
                                <i class="fas fa-sign-out-alt mr-2"></i>
                            </a>
                        </#if>
                    </div>
                </#if>
            <#else>
                <div class="flex items-center space-x-4">
                    <a href="/auth/login" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-sign-in-alt mr-2"></i> <@spring.message "layout.nav.logIn"/>
                    </a>
                    <a href="/auth/register" class="text-white hover:text-blue-200 flex items-center">
                        <i class="fas fa-user-plus mr-2"></i> <@spring.message "layout.nav.register"/>
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
                    <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.about"/></h3>
                    <p class="text-gray-400"><@spring.message "layout.footer.about.description"/></p>
                </div>
                <div>
                    <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.applicants"/></h3>
                    <ul class="space-y-2 text-gray-400">
                        <li><a href="/vacancies" class="hover:text-white"><@spring.message "layout.footer.vacancies.search"/></a></li>
                        <li><a href="/resumes/create" class="hover:text-white"><@spring.message "layout.footer.resume.create"/></a></li>
                        <li><a href="#" class="hover:text-white"><@spring.message "layout.footer.tips"/></a></li>
                        <li><a href="#" class="hover:text-white"><@spring.message "layout.footer.reviews"/></a></li>
                    </ul>
                </div>
                <div>
                    <h3 class="font-bold text-lg mb-4"><@spring.message "layout.footer.employers"/></h3>
                    <ul class="space-y-2 text-gray-400">
                        <li><a href="/vacancies/create" class="hover:text-white"><@spring.message "layout.footer.vacancy.create"/></a></li>
                        <li><a href="/resumes" class="hover:text-white"><@spring.message "layout.footer.resume.search"/></a></li>
                        <li><a href="#" class="hover:text-white"><@spring.message "layout.footer.corporate"/></a></li>
                        <li><a href="#" class="hover:text-white"><@spring.message "layout.footer.pricing"/></a></li>
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
            <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
                <p>&copy; <@spring.message "layout.footer.copyright"/></p>
            </div>
        </div>
    </footer>
    </body>
    </html>
</#macro>

<#macro csrf>
    <#if _csrf??>
        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
               value="${(_csrf.token)!'csrf-token'}"/>
    </#if>
</#macro>