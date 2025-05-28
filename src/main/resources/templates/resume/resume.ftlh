<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">

            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-file-alt mr-2"></i>${resume.name}
                    </h1>
                    <div class="flex space-x-3">
                        <#if currentUser?? && currentUser.role.id == 2>
                            <a href="/resumes/${resume.id}/edit"
                               class="bg-white text-blue-600 px-4 py-2 rounded-md hover:bg-blue-50 transition font-medium">
                                <i class="fas fa-edit mr-2"></i><@spring.message "resume.edit.button"/>
                            </a>
                            <form action="/resumes/${resume.id}/delete" method="post"
                                  onsubmit="return confirm('<@spring.message "profile.resumes.delete.confirm"/>');"
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
            <div class="p-6 border-b">
                <div class="flex flex-col md:flex-row justify-between">
                    <div>
                        <h2 class="text-xl font-bold text-gray-800">${resume.applicant.name!}</h2>
                        <div class="mt-2 text-lg font-medium text-blue-600">
                            <#if resume.salary?? && resume.salary gt 0>
                                ${resume.salary?string(",##0")} ₽
                            <#else>
                                <@spring.message "resumes.salary.notSpecified"/>
                            </#if>
                        </div>

                        <div class="mt-4 flex flex-wrap gap-2">
                            <#if resume.isActive?? && resume.isActive>
                                <span class="bg-green-100 text-green-700 text-sm px-3 py-1 rounded-full"><@spring.message "resume.status.active"/></span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full"><@spring.message "resume.status.inactive"/></span>
                            </#if>

                            <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                    <i class="fas fa-tags mr-1"></i>${resume.category.name}
                            </span>

                            <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                                <#assign totalYears = 0>
                                <#list resume.workExperiences as exp>
                                    <#assign totalYears = totalYears + exp.years>
                                </#list>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                <@spring.message "resumes.experience.years"/>: ${totalYears}
                            </span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                <@spring.message "resumes.experience.none"/>
                            </span>
                            </#if>
                        </div>
                    </div>

                    <div class="mt-6 md:mt-0 text-right">
                        <div class="text-sm text-gray-500">
                            <#if resume.updatedAt??>
                                <@spring.message "resumes.updated"/>: ${resume.updatedAt?datetime?string("dd.MM.yyyy")}
                            <#elseif resume.createdAt??>
                                <@spring.message "resumes.created"/>: ${resume.createdAt?datetime?string("dd.MM.yyyy")}
                            </#if>
                        </div>
                    </div>
                </div>
            </div>


            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-address-card text-blue-600 mr-2"></i><@spring.message "resume.contacts.title"/>
                </h3>

                <#if resume.contacts?? && resume.contacts?size gt 0>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <#list resume.contacts as contact>
                            <div class="flex items-center p-3 bg-gray-50 rounded-lg border border-gray-100 hover:border-blue-200 hover:bg-blue-50 transition-colors">
                                <div class="w-10 h-10 mr-3 flex items-center justify-center rounded-lg shadow-sm
                                <#if contact.contactType.id == 1>
                                    bg-red-100 text-red-600
                                <#elseif contact.contactType.id == 2>
                                    bg-green-100 text-green-600
                                <#elseif contact.contactType.id == 3>
                                    bg-blue-100 text-blue-600
                                <#elseif contact.contactType.id == 4>
                                    bg-purple-100 text-purple-600
                                <#else>
                                    bg-blue-100 text-blue-600
                                </#if>">
                                    <#if contact.contactType.id == 1>
                                        <i class="fas fa-envelope"></i>
                                    <#elseif contact.contactType.id == 2>
                                        <i class="fas fa-phone"></i>
                                    <#elseif contact.contactType.id == 3>
                                        <i class="fab fa-telegram"></i>
                                    <#elseif contact.contactType.id == 4>
                                        <i class="fab fa-facebook"></i>
                                    <#else>
                                        <i class="fab fa-linkedin"></i>
                                    </#if>
                                </div>
                                <div>
                                    <div class="text-xs text-gray-500">
                                        <#if contact.contactType.id == 1>
                                            <@spring.message "resume.contacts.email"/>
                                        <#elseif contact.contactType.id == 2>
                                            <@spring.message "resume.contacts.phone"/>
                                        <#elseif contact.contactType.id == 3>
                                            <@spring.message "resume.contacts.telegram"/>
                                        <#elseif contact.contactType.id == 4>
                                            <@spring.message "resume.contacts.facebook"/>
                                        <#else>
                                            <@spring.message "resume.contacts.linkedin"/>
                                        </#if>
                                    </div>
                                    <div class="text-gray-800 font-medium">${contact.contactValue}</div>
                                </div>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <p class="text-gray-500 italic"><@spring.message"resume.edit.contacts.empty"/></p>
                </#if>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-briefcase text-blue-600 mr-2"></i><@spring.message "resume.experience.title"/>
                </h3>

                <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                    <div class="space-y-6">
                        <#list resume.workExperiences as exp>
                            <div class="border-l-2 border-blue-500 pl-4">
                                <div class="flex justify-between">
                                    <h4 class="font-medium text-gray-800">${exp.position}</h4>
                                    <span class="text-sm text-gray-500">
                                    <@spring.message "resumes.experience.years"/> ${exp.years}
                                </span>
                                </div>
                                <p class="text-blue-600">${exp.companyName}</p>
                                <#if exp.responsibilities?? && exp.responsibilities?has_content>
                                    <p class="text-gray-600 mt-2">${exp.responsibilities}</p>
                                </#if>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <p class="text-gray-500 italic"><@spring.message "resumes.experience.notSpecified"/></p>
                </#if>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-graduation-cap text-blue-600 mr-2"></i><@spring.message "resume.education.title"/>
                </h3>

                <#if resume.educations?? && resume.educations?size gt 0>
                    <div class="space-y-6">
                        <#list resume.educations as edu>
                            <div class="border-l-2 border-blue-500 pl-4">
                                <div class="flex justify-between">
                                    <h4 class="font-medium text-gray-800">${edu.institution}</h4>
                                    <#if edu.endDate??>
                                        <span class="text-sm text-gray-500">${edu.startDate} - ${edu.endDate}</span>
                                    </#if>
                                </div>
                                <p class="text-blue-600">${edu.program}</p>
                                <p class="text-gray-600 mt-1">${edu.degree}</p>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <p class="text-gray-500 italic"><@spring.message "resume.education.notSpecified"/></p>
                </#if>
            </div>
        </div>
    </div>
</@main.layout>