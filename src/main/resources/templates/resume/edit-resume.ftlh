<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <!-- Шапка с названием -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-edit mr-2"></i><@spring.message"resume.edit.title"/>
                    </h1>
                    <div class="flex space-x-3">
                        <button type="submit"
                                form="resume-edit-form"
                                class="bg-green-600/80 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-green-700/90 transition font-medium">
                            <i class="fas fa-save mr-2"></i><@spring.message"resume.edit.save"/>
                        </button>
                        <form action="/resumes/${resumeForm.id}/delete" method="post"
                              onsubmit="return confirm('<@spring.message "profile.resumes.delete.confirm"/>');"
                              class="inline">
                            <@main.csrf/>
                            <button type="submit"
                                    class="bg-red-600/80 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-red-700/90 transition font-medium"
                                    title="<@spring.message "profile.resumes.delete"/>">
                                <i class="fas fa-trash-alt mr-2"></i><@spring.message "profile.resumes.delete"/>
                            </button>
                        </form>
                    </div>
                </div>
                <p class="mt-2 text-blue-100 text-sm">
                    <@spring.message"resume.edit.description"/>
                </p>
            </div>

            <form id="resume-edit-form" action="/resumes/${resumeForm.id}/edit" method="post" class="space-y-6">
                <@main.csrf/>
                <input type="hidden" name="id" value="${resumeForm.id}">
                <input type="hidden" name="applicant.id" value="${resumeForm.applicant.id}"/>

                <!-- Основная информация -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i><@spring.message"resume.edit.section.general"/>
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Левая колонка -->
                        <div>
                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700"><@spring.message"resume.edit.name.label"/></label>
                                <@spring.formInput  "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <p class="text-xs text-gray-500 mt-1"><@spring.message"resume.edit.name.hint"/></p>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700"><@spring.message"resume.edit.category.label"/></label>
                                <#assign categoryMap = {}>
                                <#list categories as category>
                                    <#assign categoryMap = categoryMap + {category.id?string: category.name}>
                                </#list>

                                <@spring.formSingleSelect
                                "resumeForm.category.id",
                                categoryMap,
                                'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <!-- Правая колонка -->
                        <div>
                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message"resume.edit.status.label"/></label>
                                <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
                                    <div class="flex space-x-4">
                                        <#assign activeLabel><@spring.message code="vacancy.edit.status.active" /></#assign>
                                        <#assign inactiveLabel><@spring.message code="vacancy.edit.status.inactive" /></#assign>
                                        <@spring.formRadioButtons
                                        "resumeForm.isActive",
                                        { "true": activeLabel, "false": inactiveLabel },
                                        "<br>",
                                        "class='h-4 w-4 text-blue-600 border-gray-300 focus:ring-blue-500'"
                                        />
                                        <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700"><@spring.message"resume.edit.salary.label"/></label>
                                <@spring.formInput "resumeForm.salary", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" type="number" min="0"', "number"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <!-- Контактная информация -->
            <div class="p-6 border-b">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg font-bold text-gray-800 flex items-center">
                        <i class="fas fa-address-card text-blue-600 mr-2"></i><@spring.message"resume.edit.contacts.title"/>
                    </h3>
                    <a href="/resumes/${resumeForm.id}/contacts/create"
                       class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                        <i class="fas fa-plus mr-2"></i> <@spring.message"resume.edit.contacts.add"/>
                    </a>
                </div>

                <#if resume.contacts?has_content>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <#list resume.contacts as contact>
                            <div class="bg-gray-50 rounded-lg p-4 border">
                                <div class="flex justify-between items-center mb-2">
                                    <div class="flex items-center">
                                        <#switch contact.contactType.id>
                                            <#case 1>
                                                <i class="fas fa-envelope text-red-500 mr-2"></i>
                                                <#break>
                                            <#case 2>
                                                <i class="fas fa-phone text-green-500 mr-2"></i>
                                                <#break>
                                            <#case 3>
                                                <i class="fab fa-telegram text-blue-500 mr-2"></i>
                                                <#break>
                                            <#default>
                                                <i class="fas fa-link text-gray-500 mr-2"></i>
                                        </#switch>
                                        <span class="font-medium">${contact.contactType.name}</span>
                                    </div>
                                    <div class="flex space-x-2">
                                        <a href="/resumes/${resumeForm.id}/contacts/${contact.id}/edit"
                                           class="text-blue-500 hover:text-blue-600"
                                           title="<@spring.message "resume.edit.contacts.edit"/>">
                                            <i class="fas fa-edit"></i>
                                        </a>

                                        <form action="/resumes/${resumeForm.id}/contacts/${contact.id}/delete"
                                              method="post"
                                              onsubmit="return confirm('<@spring.message "contact.delete.confirm"/>');"
                                              class="inline">
                                            <@main.csrf/>
                                            <button type="submit"
                                                    class="text-red-500 hover:text-red-600"
                                                    title="<@spring.message "contact.delete"/>">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                                <div class="text-gray-700">${contact.contactValue}</div>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="text-center text-gray-500 py-4">
                        <@spring.message"resume.edit.contacts.empty"/>
                    </div>
                </#if>
            </div>

            <!-- Опыт работы -->
            <div class="p-6 border-b">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg font-bold text-gray-800 flex items-center">
                        <i class="fas fa-briefcase text-blue-600 mr-2"></i><@spring.message"resume.experience.title"/>
                    </h3>
                    <a href="/resumes/${resumeForm.id}/experiences/create"
                       class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                        <i class="fas fa-plus mr-2"></i> <@spring.message"resume.edit.experience.add"/>
                    </a>
                </div>

                <#if resume.workExperiences?has_content>
                    <div class="space-y-4">
                        <#list resume.workExperiences as exp>
                            <div class="bg-gray-50 rounded-lg p-4 border">
                                <div class="flex justify-between items-start">
                                    <div class="flex-1">
                                        <h4 class="font-medium text-gray-800">${exp.position}</h4>
                                        <p class="text-gray-600">${exp.companyName}</p>
                                        <div class="mt-2 text-sm text-gray-700">
                                            ${exp.responsibilities}
                                        </div>
                                        <div class="mt-2 text-sm text-blue-600">
                                            <@spring.message"resume.edit.experience.duration"/> ${exp.years}
                                        </div>
                                    </div>
                                    <div class="flex space-x-2 ml-4">
                                        <a href="/resumes/${resumeForm.id}/experiences/${exp.id}/edit"
                                           class="text-blue-500 hover:text-blue-600"
                                           title="<@spring.message "resume.edit.experience.edit"/>">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <form action="/resumes/${resumeForm.id}/experiences/${exp.id}/delete"
                                              method="post"
                                              onsubmit="return confirm('<@spring.message "experience.delete.confirm"/>');"
                                              class="inline">
                                            <@main.csrf/>
                                            <button type="submit"
                                                    class="text-red-500 hover:text-red-600"
                                                    title="<@spring.message "experience.delete"/>">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="text-center text-gray-500 py-4">
                        <@spring.message"resume.edit.experience.empty"/>
                    </div>
                </#if>
            </div>

            <!-- Образование -->
            <div class="p-6 border-b">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg font-bold text-gray-800 flex items-center">
                        <i class="fas fa-graduation-cap text-blue-600 mr-2"></i><@spring.message"resume.education.title"/>
                    </h3>
                    <a href="/resumes/${resumeForm.id}/educations/create"
                       class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                        <i class="fas fa-plus mr-2"></i> <@spring.message"resume.edit.education.add"/>
                    </a>
                </div>

                <#if resume.educations?has_content>
                    <div class="space-y-4">
                        <#list resume.educations as edu>
                            <div class="bg-gray-50 rounded-lg p-4 border">
                                <div class="flex justify-between items-start">
                                    <div class="flex-1">
                                        <h4 class="font-medium text-gray-800">${edu.institution}</h4>
                                        <p class="text-gray-600">${edu.program}</p>
                                        <p class="text-sm text-gray-500">${edu.degree}</p>
                                        <div class="mt-2 text-sm text-blue-600">
                                            ${edu.startDate} - ${edu.endDate}
                                        </div>
                                    </div>
                                    <div class="flex space-x-2 ml-4">
                                        <a href="/resumes/${resumeForm.id}/educations/${edu.id}/edit"
                                           class="text-blue-500 hover:text-blue-600"
                                           title="<@spring.message "resume.edit.education.edit"/>">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <form action="/resumes/${resumeForm.id}/educations/${edu.id}/delete"
                                              method="post"
                                              onsubmit="return confirm('<@spring.message "education.delete.confirm"/>');"
                                              class="inline">
                                            <@main.csrf/>
                                            <button type="submit"
                                                    class="text-red-500 hover:text-red-600"
                                                    title="<@spring.message "education.delete"/>">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <div class="text-center text-gray-500 py-4">
                        <@spring.message"resume.education.notSpecified"/>
                    </div>
                </#if>
            </div>

            <!-- Кнопки действий -->
            <div class="p-6 bg-gray-50 flex justify-center">
                <a href="/resumes/${resumeForm.id}"
                   class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                    <i class="fas fa-arrow-left mr-2"></i><@spring.message"resume.edit.back"/>
                </a>
            </div>
        </div>
    </div>
</@main.layout>