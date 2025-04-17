<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="bg-blue-600 text-white py-8">
        <div class="container mx-auto px-4">
            <div class="flex items-center justify-between">
                <h1 class="text-2xl font-bold">${resume.name}</h1>
                <div class="flex space-x-3">
                    <#if currentUser?? && currentUser.role.id == 2>
                        <a href="/resumes/${resume.id}/edit"
                           class="bg-white text-blue-600 px-4 py-2 rounded-md hover:bg-blue-50 transition font-medium">
                            <i class="fas fa-edit mr-2"></i>Редактировать
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="p-6 border-b">
                <div class="flex flex-col md:flex-row justify-between">
                    <div>
                        <h2 class="text-xl font-bold text-gray-800">${resume.applicant.name!'Соискатель'}</h2>
                        <div class="mt-2 text-lg font-medium text-blue-600">
                            <#if resume.salary?? && resume.salary gt 0>
                                ${resume.salary?string(",##0")} ₽
                            <#else>
                                Ожидаемая зарплата не указана
                            </#if>
                        </div>

                        <div class="mt-4 flex flex-wrap gap-2">
                                <span class="bg-blue-100 text-blue-800 text-sm px-3 py-1 rounded-full">
                                ${resume.category.name}
                            </span>

                            <#if resume.isActive?? && resume.isActive>
                                <span class="bg-green-100 text-green-700 text-sm px-3 py-1 rounded-full">Активно ищет работу</span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">Не ищет работу</span>
                            </#if>

                            <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                                <#assign totalYears = 0>
                                <#list resume.workExperiences as exp>
                                    <#assign totalYears = totalYears + exp.years>
                                </#list>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                Опыт: ${totalYears} ${getPluralForm(totalYears, "год", "года", "лет")}
                            </span>
                            <#else>
                                <span class="bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-full">
                                Без опыта
                            </span>
                            </#if>
                        </div>
                    </div>

                    <div class="mt-4 md:mt-0 text-right">
                        <div class="text-sm text-gray-500">
                            <#if resume.updatedAt??>
                                Обновлено: ${resume.updatedAt?datetime?string("dd.MM.yyyy")}
                            <#elseif resume.createdAt??>
                                Создано: ${resume.createdAt?datetime?string("dd.MM.yyyy")}
                            </#if>
                        </div>

                        <#if resume.contacts?? && resume.contacts?size gt 0>
                            <div class="mt-4">
                                <h3 class="text-sm font-medium text-gray-700 mb-2">Контактная информация:</h3>
                                <ul class="space-y-1">
                                    <#list resume.contacts as contact>
                                        <li class="flex items-center justify-end">
                                            <#if contact.contactType.id == 1>
                                                <i class="fas fa-envelope text-gray-500 mr-2"></i>
                                            <#elseif contact.contactType.id == 2>
                                                <i class="fas fa-phone text-gray-500 mr-2"></i>
                                            <#elseif contact.contactType.id == 3>
                                                <i class="fab fa-telegram text-gray-500 mr-2"></i>
                                            <#elseif contact.contactType.id == 4>
                                                <i class="fa-brands fa-facebook text-gray-500 mr-2"></i>
                                            <#else>
                                                <i class="fa-brands fa-linkedin text-gray-500 mr-2"></i>
                                            </#if>
                                            <span>${contact.contactValue}</span>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-briefcase text-blue-600 mr-2"></i>Опыт работы
                </h3>

                <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                    <div class="space-y-6">
                        <#list resume.workExperiences as exp>
                            <div class="border-l-2 border-blue-500 pl-4">
                                <div class="flex justify-between">
                                    <h4 class="font-medium text-gray-800">${exp.position}</h4>
                                    <span class="text-sm text-gray-500">
                                    ${exp.years} ${getPluralForm(exp.years, "год", "года", "лет")}
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
                    <p class="text-gray-500 italic">Опыт работы не указан</p>
                </#if>
            </div>

            <div class="p-6 border-b">
                <h3 class="text-lg font-bold text-gray-800 mb-4">
                    <i class="fas fa-graduation-cap text-blue-600 mr-2"></i>Образование
                </h3>

                <#if resume.educations?? && resume.educations?size gt 0>
                    <div class="space-y-6">
                        <#list resume.educations as edu>
                            <div class="border-l-2 border-blue-500 pl-4">
                                <div class="flex justify-between">
                                    <h4 class="font-medium text-gray-800">${edu.institution}</h4>
                                    <#if edu.endDate??>
                                        <span class="text-sm text-gray-500">${edu.endDate}</span>
                                    </#if>
                                </div>
                                <p class="text-blue-600">${edu.program}</p>
                                <p class="text-gray-600 mt-1">${edu.degree}</p>
                            </div>
                        </#list>
                    </div>
                <#else>
                    <p class="text-gray-500 italic">Образование не указано</p>
                </#if>
            </div>

            <div class="p-6 bg-gray-50">
                <div class="flex flex-col md:flex-row justify-between items-center">
                    <#--                    TODO-->
                    <div class="flex space-x-4">
                        <button class="text-blue-600 hover:text-blue-800">
                            <i class="far fa-bookmark mr-1"></i> Сохранить
                        </button>
                        <button class="text-blue-600 hover:text-blue-800">
                            <i class="fas fa-share-alt mr-1"></i> Поделиться
                        </button>
                    </div>

                    <#if currentUser?? && currentUser.role.id == 1>
                        <button class="mt-4 md:mt-0 bg-green-500 text-white px-6 py-2 rounded-md hover:bg-green-600 transition font-medium">
                            <i class="fas fa-paper-plane mr-2"></i>Пригласить на собеседование
                        </button>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <#function getPluralForm number singular dual plural>
        <#assign mod10 = number % 10 />
        <#assign mod100 = number % 100 />

        <#if mod10 == 1 && mod100 != 11>
            <#return singular />
        <#elseif (mod10 >= 2 && mod10 <= 4) && (mod100 < 10 || mod100 >= 20)>
            <#return dual />
        <#else>
            <#return plural />
        </#if>
    </#function>
</@main.layout>