<#import "layout.ftlh" as main>
<#import "/spring.ftl" as spring>

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
                           <@spring.message "index.hero.title.applicant"/>
                        <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                            <@spring.message "index.hero.title.employer"/>
                        <#else>
                            <@spring.message "index.hero.title.default"/>
                        </#if>
                    </h1>
                    <p class="text-gray-600 mb-6">
                        <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                            <@spring.message "index.hero.description.applicant"/>
                        <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                            <@spring.message "index.hero.description.employer"/>
                        <#else>
                            <@spring.message "index.hero.description.default"/>
                        </#if>
                    </p>
                    <div class="flex flex-col sm:flex-row gap-4">
                        <#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')>
                            <a href="/vacancies"
                               class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                                <@spring.message "index.hero.button.findJob"/>
                            </a>
                        </#if>
                        <#if !known || (roles?? && roles?first.authority == 'ROLE_EMPLOYER')>
                            <a href="/resumes"
                               class="<#if !known || (roles?? && roles?first.authority == 'ROLE_EMPLOYER')>bg-blue-600 hover:bg-blue-700 text-white<#else>bg-white border border-blue-600 hover:bg-blue-50 text-blue-600</#if> font-medium py-3 px-6 rounded-md transition duration-200 text-center">
                                <@spring.message "index.hero.button.findApplicant"/>
                            </a>
                        </#if>
                    </div>
                </div>

                <div class="md:w-1/2 bg-blue-600 p-8 md:p-12 text-white">
                    <div class="bg-white/10 backdrop-blur-sm rounded-lg p-6">
                        <h2 class="text-xl font-semibold mb-4"><@spring.message "index.search.title"/></h2>
                        <form action="/search" method="GET">
                            <@main.csrf/>
                            <div class="mb-4">
                                <label for="keyword" class="block text-sm font-medium mb-1"><@spring.message "index.search.keyword.label"/></label>
                                <input type="text" id="keyword" name="keyword"
                                       placeholder="<#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')><@spring.message "index.search.keyword.placeholder.applicant"/><#else><@spring.message "index.search.keyword.placeholder.employer"/></#if>"
                                       class="w-full px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white focus:outline-none focus:ring-2 focus:ring-white/50">
                            </div>
                            <div class="mb-4">
                                <label for="location" class="block text-sm font-medium mb-1"><@spring.message "index.search.location.label"/></label>
                                <input type="text" id="location" name="location" placeholder="<@spring.message "index.search.location.placeholder"/>"
                                       class="w-full px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white focus:outline-none focus:ring-2 focus:ring-white/50">
                            </div>
                            <button type="submit"
                                    class="w-full bg-white text-blue-600 hover:bg-blue-50 font-medium py-2 px-4 rounded-md transition duration-200">
                                <i class="fas fa-search mr-2"></i> <@spring.message "index.search.button"/>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>

        <section class="mb-12">
            <h2 class="text-2xl font-bold text-gray-800 mb-6"><@spring.message "index.categories.title"/></h2>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <a href="/vacancies/categories?category=it"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-laptop-code text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800"><@spring.message "index.categories.it"/></h3>
                    <p class="text-sm text-gray-500 mt-1"><@spring.message "index.categories.jobs"/> 1,234</p>
                </a>
                <a href="/vacancies/categories?category=finance"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-chart-line text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800"><@spring.message "index.categories.finance"/></h3>
                    <p class="text-sm text-gray-500 mt-1"><@spring.message "index.categories.jobs"/> 874</p>
                </a>
                <a href="/vacancies/categories?category=marketing"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-bullhorn text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800"><@spring.message "index.categories.marketing"/></h3>
                    <p class="text-sm text-gray-500 mt-1"><@spring.message "index.categories.jobs"/> 543</p>
                </a>
                <a href="/vacancies/categories?category=medical"
                   class="bg-white rounded-lg shadow p-6 flex flex-col items-center transition hover:shadow-md hover:bg-blue-50">
                    <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                        <i class="fas fa-stethoscope text-blue-600 text-xl"></i>
                    </div>
                    <h3 class="font-medium text-gray-800"><@spring.message "index.categories.medical"/></h3>
                    <p class="text-sm text-gray-500 mt-1"><@spring.message "index.categories.jobs"/> 451</p>
                </a>
            </div>
        </section>

        <!-- Показываем раздел в зависимости от роли пользователя -->
        <#if !known || (roles?? && roles?first.authority == 'ROLE_APPLICANT')>
            <section class="mb-12">
                <div class="flex justify-between items-center mb-6">
                    <h2 class="text-2xl font-bold text-gray-800"><@spring.message "index.newVacancies.title"/></h2>
                    <a href="/vacancies" class="text-blue-600 hover:text-blue-800 font-medium">
                        <@spring.message "index.newVacancies.viewAll"/> <i class="fas fa-arrow-right ml-1"></i>
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
                                    <span class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded"><@spring.message "index.vacancy.fullTime"/></span>
                                </div>
                                <div class="flex items-center text-gray-500 text-sm mb-4">
                                    <i class="fas fa-map-marker-alt mr-2"></i>
                                    <span><@spring.message "index.city"/></span>
                                    <span class="mx-2">•</span>
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
                                        <@spring.message "index.publishedOn"/> ${vacancy.createdAt?string("dd.MM.yyyy")}
                                    </span>
                                    <a href="/vacancies/${vacancy.id}"
                                       class="text-blue-600 hover:text-blue-800 font-medium">
                                        <@spring.message "index.details"/>
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
                    <h2 class="text-2xl font-bold text-gray-800"><@spring.message "index.newResumes.title"/></h2>
                    <a href="/resumes" class="text-blue-600 hover:text-blue-800 font-medium">
                        <@spring.message "index.newResumes.viewAll"/> <i class="fas fa-arrow-right ml-1"></i>
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
                                        <#if resume.isActive><@spring.message "index.resume.active"/><#else><@spring.message "index.resume.inactive"/></#if>
                                    </span>
                                </div>
                                <div class="flex items-center text-gray-500 text-sm mb-4">
                                    <i class="fas fa-map-marker-alt mr-2"></i>
                                    <span><@spring.message "index.city"/></span>
                                    <#if resume.salary?? && resume.salary gt 0>
                                        <span class="mx-2">•</span>
                                        <span>${resume.salary} c</span>
                                    </#if>
                                </div>
                                <div class="flex flex-wrap gap-2 mb-4">
                                    <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">${resume.category.name}</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-sm text-gray-500">
                                        <@spring.message "index.publishedOn"/> ${resume.createdAt?string("dd.MM.yyyy")}
                                    </span>
                                    <a href="/resumes/${resume.id}"
                                       class="text-blue-600 hover:text-blue-800 font-medium">
                                        <@spring.message "index.details"/>
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
                <h2 class="text-2xl font-bold text-gray-800 mb-8 text-center"><@spring.message "index.howItWorks.title"/></h2>
                <div class="grid md:grid-cols-3 gap-8">
                    <#if !currentUserId??>
                        <a href="/auth/register">
                            <div class="flex flex-col items-center text-center">
                                <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                    <i class="fas fa-user-plus text-blue-600 text-2xl"></i>
                                </div>
                                <h3 class="font-bold text-lg text-gray-800 mb-2"><@spring.message "index.howItWorks.step1.title"/></h3>
                                <p class="text-gray-600">
                                    <@spring.message "index.howItWorks.step1.description"/>
                                </p>
                            </div>
                        </a>
                    <#else>
                        <a href="<#if roles?? && roles?first.authority == 'ROLE_APPLICANT'>/applicants/${currentUserId}<#else>/employers/${currentUserId}</#if>">
                            <div class="flex flex-col items-center text-center">
                                <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                                    <i class="fas fa-user text-blue-600 text-2xl"></i>
                                </div>
                                <h3 class="font-bold text-lg text-gray-800 mb-2"><@spring.message "index.howItWorks.step1.profile.title"/></h3>
                                <p class="text-gray-600">
                                    <@spring.message "index.howItWorks.step1.profile.description"/>
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
                                    <@spring.message "index.howItWorks.step2.title.applicant"/>
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    <@spring.message "index.howItWorks.step2.title.employer"/>
                                <#else>
                                    <@spring.message "index.howItWorks.step2.title.default"/>
                                </#if>
                            </h3>
                            <p class="text-gray-600">
                                <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                                    <@spring.message "index.howItWorks.step2.description.applicant"/>
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    <@spring.message "index.howItWorks.step2.description.employer"/>
                                <#else>
                                    <@spring.message "index.howItWorks.step2.description.default"/>
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
                                <@spring.message "index.howItWorks.step3.title"/>
                            </h3>
                            <p class="text-gray-600">
                                <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                                    <@spring.message "index.howItWorks.step3.description.applicant"/>
                                <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                                    <@spring.message "index.howItWorks.step3.description.employer"/>
                                <#else>
                                    <@spring.message "index.howItWorks.step3.description.default"/>
                                </#if>
                            </p>
                        </div>
                    </a>
                </div>
            </div>
        </section>

        <section class="mb-12">
            <h2 class="text-2xl font-bold text-gray-800 mb-6"><@spring.message "index.topEmployers.title"/></h2>
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
                        <@spring.message "index.subscribe.title.applicant"/>
                    <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                        <@spring.message "index.subscribe.title.employer"/>
                    <#else>
                        <@spring.message "index.subscribe.title.default"/>
                    </#if>
                </h2>
                <p class="mb-6 max-w-2xl mx-auto">
                    <#if known && roles?? && roles?first.authority == 'ROLE_APPLICANT'>
                        <@spring.message "index.subscribe.description.applicant"/>
                    <#elseif known && roles?? && roles?first.authority == 'ROLE_EMPLOYER'>
                        <@spring.message "index.subscribe.description.employer"/>
                    <#else>
                        <@spring.message "index.subscribe.description.default"/>
                    </#if>
                </p>
                <form class="flex flex-col sm:flex-row gap-4 max-w-md mx-auto" method="post" action="/subscribe">
                    <@main.csrf/>
                    <label>
                        <input type="email" placeholder="<@spring.message "index.subscribe.email.placeholder"/>" name="email"
                               class="px-4 py-2 rounded-md border border-white/30 bg-white/20 placeholder-white/60 text-white flex-grow focus:outline-none focus:ring-2 focus:ring-white/50">
                    </label>
                    <button type="submit"
                            class="bg-white text-blue-600 hover:bg-blue-50 font-medium py-2 px-6 rounded-md transition duration-200 whitespace-nowrap">
                        <@spring.message "index.subscribe.button"/>
                    </button>
                </form>
            </div>
        </section>
    </div>
</@main.layout>