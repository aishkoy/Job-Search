<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-briefcase mr-2"></i>${vacancy.name}
                    </h1>
                    <div class="flex space-x-3">
                        <#if currentUser?? && currentUser.id == vacancy.employer.id>
                            <a href="/vacancies/${vacancy.id}/edit"
                               class="bg-white text-blue-600 px-4 py-2 rounded-md hover:bg-blue-50 transition font-medium">
                                <i class="fas fa-edit mr-2"></i><@spring.message "vacancy.edit"/>
                            </a>
                            <form action="/vacancies/${vacancy.id}/delete" method="post"
                                  onsubmit="return confirm('<@spring.message "profile.vacancies.delete.confirm"/>');"
                                  class="inline">
                                <@main.csrf/>
                                <button type="submit"
                                        class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition font-medium"
                                        title="<@spring.message "profile.resumes.delete"/>">
                                    <i class="fas fa-trash-alt mr-2"></i><@spring.message "profile.resumes.delete"/>
                                </button>
                            </form>
                        </#if>
                    </div>
                </div>
            </div>

            <!-- Основная информация о вакансии -->
            <div class="p-6 border-b">
                <div class="flex flex-col md:flex-row justify-between">
                    <div>
                        <h2 class="text-xl font-bold text-gray-800">${vacancy.employer.name} ${vacancy.employer.surname!}</h2>
                        <div class="mt-2 text-lg font-medium text-blue-600">
                            <#if vacancy.salary?? && vacancy.salary gt 0>
                                ${vacancy.salary?string(",##0")} c
                            <#else>
                                <@spring.message "vacancy.salary.notSpecified"/>
                            </#if>
                        </div>

                        <div class="mt-4 flex flex-wrap gap-2">
                            <#if vacancy.isActive?? && vacancy.isActive>
                                <span class="bg-green-100 text-green-700 text-sm px-3 py-1 rounded-full"><@spring.message "vacancy.status.active"/></span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full"><@spring.message "vacancy.status.closed"/></span>
                            </#if>

                            <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                <i class="fas fa-tags mr-1"></i>${vacancy.category.name}
                            </span>

                            <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                <@spring.message "vacancy.experience"/>: ${vacancy.expFrom} - ${vacancy.expTo}
                            </span>
                        </div>
                    </div>

                    <div class="mt-4 md:mt-0 text-right">
                        <div class="text-sm text-gray-500">
                            <#if vacancy.updatedAt??>
                                <@spring.message "vacancy.updated"/>: ${vacancy.updatedAt?datetime?string("dd.MM.yyyy")}
                            <#elseif vacancy.createdAt??>
                                <@spring.message "vacancy.created"/>: ${vacancy.createdAt?datetime?string("dd.MM.yyyy")}
                            </#if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Описание вакансии -->
            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-file-alt text-blue-600 mr-2"></i><@spring.message "vacancy.description.title"/>
                </h3>

                <#if vacancy.description?? && vacancy.description?has_content>
                    <div class="prose max-w-none bg-gray-50 p-4 rounded-lg border border-gray-100">
                        <p class="text-gray-700">${vacancy.description}</p>
                    </div>
                <#else>
                    <p class="text-gray-500 italic"><@spring.message "vacancy.description.empty"/>о</p>
                </#if>
            </div>

            <!-- Требования -->
            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-user-check text-blue-600 mr-2"></i><@spring.message "vacancy.requirements.title"/>
                </h3>

                <div class="space-y-3 bg-gray-50 p-4 rounded-lg border border-gray-100">
                    <div class="flex items-center">
                        <div class="w-10 h-10 bg-blue-100 text-blue-600 rounded-lg flex items-center justify-center mr-3">
                            <i class="fas fa-briefcase"></i>
                        </div>
                        <div>
                            <div class="text-xs text-gray-500"><@spring.message "vacancy.requirements.experience"/></div>
                            <div class="text-gray-700 font-medium"><@spring.message "vacancy.experience"/> ${vacancy.expFrom}-${vacancy.expTo}</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- О компании -->
            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-building text-blue-600 mr-2"></i><@spring.message "vacancy.company.title"/>
                </h3>

                <div class="flex items-start bg-gray-50 p-4 rounded-lg border border-gray-100">
                    <div class="flex-shrink-0 mr-4">
                        <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center">
                            <i class="fas fa-building text-blue-600 text-2xl"></i>
                        </div>
                    </div>
                    <div>
                        <h4 class="font-semibold text-gray-800 text-lg">${vacancy.employer.name} ${vacancy.employer.surname!}</h4>
                        <p class="text-gray-600 mt-1">${vacancy.employer.email}</p>
                        <p class="text-gray-600 mt-1">${vacancy.employer.phoneNumber}</p>
                    </div>
                </div>
            </div>

            <!-- Нижняя панель действий -->
            <div class="p-6 bg-gray-50">
                <div class="flex flex-col md:flex-row justify-between items-center">
                    <#if !currentUser??>
                        <a href="/auth/login"
                           class="mt-4 md:mt-0 bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition font-medium flex items-center">
                            <i class="fas fa-sign-in-alt mr-2"></i><@spring.message "vacancy.actions.login"/>
                        </a>
                    <#elseif currentUser.role.id == 2>
                        <div id="vacancy-actions-container">
                            <button id="vacancy-apply-btn" class="mt-4 md:mt-0 bg-green-500 text-white px-6 py-2 rounded-md hover:bg-green-600 transition font-medium flex items-center">
                                <i class="fas fa-paper-plane mr-2"></i><@spring.message "vacancy.actions.apply"/>
                            </button>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <div id="message-storage" class="hidden">
        <div data-key="resume.choose"><@spring.message "response.resume.choose" /></div>
        <div data-key="resume.empty"><@spring.message "response.resume.empty" /></div>
        <div data-key="resume.create"><@spring.message "response.resume.create" /></div>
        <div data-key="apply"><@spring.message "response.apply" /></div>
        <div data-key="user.error"><@spring.message "response.user.error" /></div>
        <div data-key="resume.error"><@spring.message "response.resume.error" /></div>
        <div data-key="work-exp.empty"><@spring.message "response.resume.work-exp.empty" /></div>
        <div data-key="sending"><@spring.message "response.sending" /></div>
        <div data-key="fail"><@spring.message "response.fail" /></div>
        <div data-key="success"><@spring.message "response.success" /></div>
        <div data-key="ready"><@spring.message "response.ready" /></div>
        <div data-key="page"><@spring.message "pagination.page" /></div>
        <div data-key="of"><@spring.message "pagination.of" /></div>
        <div data-key="resume.create.submit"><@spring.message "resume.create.submit" /></div>
        <div data-key="chat"><@spring.message "response.chat" /></div>
    </div>

    <script src="/static/js/response.js"></script>
</@main.layout>