<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <!-- Шапка с названием -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-edit mr-2"></i><@spring.message "vacancy.edit.title"/>
                    </h1>
                    <div class="flex space-x-3">
                        <a href="/vacancies/${vacancyForm.id}"
                           class="bg-white/20 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-white/30 transition font-medium">
                            <i class="fas fa-eye mr-2"></i><@spring.message "vacancy.edit.preview"/>
                        </a>
                        <button type="submit"
                                form="vacancy-edit-form"
                                class="bg-green-600/80 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-green-700/90 transition font-medium">
                            <i class="fas fa-save mr-2"></i><@spring.message "vacancy.actions.save"/>
                        </button>
                        <form action="/vacancies/${vacancyForm.id}/delete" method="post"
                              onsubmit="return confirm('<@spring.message "profile.vacancies.delete.confirm"/>');"
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
                    <@spring.message "vacancy.edit.description"/>
                </p>
            </div>

            <form id="vacancy-edit-form" action="/vacancies/${vacancyForm.id}/edit" method="post" class="space-y-6">
                <@main.csrf/>
                <input type="hidden" name="id" value="${vacancyForm.id}"/>
                <input type="hidden" name="employer.id" value="${vacancyForm.employer.id}"/>

                <!-- Основная информация -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i><@spring.message "vacancy.edit.section.general"/>
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Левая колонка -->
                        <div>
                            <div class="mb-4">
                                <label for="name" class="block text-sm font-semibold text-gray-700"><@spring.message "vacancy.edit.label.name"/> <span class="text-red-500">*</span></label>
                                <@spring.formInput "vacancyForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"' />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="category.id" class="block text-sm font-semibold text-gray-700"><@spring.message "vacancy.edit.label.category"/> <span class="text-red-500">*</span></label>
                                <#assign categoryMap = {}>
                                <#list categories as category>
                                    <#assign categoryMap = categoryMap + {category.id?string: category.name}>
                                </#list>

                                <@spring.formSingleSelect
                                "vacancyForm.category.id",
                                categoryMap,
                                'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="salary" class="block text-sm font-semibold text-gray-700"><@spring.message "vacancy.edit.label.salary"/> <span class="text-red-500">*</span></label>
                                <@spring.formInput "vacancyForm.salary", 'type="number" min="0" step="0.01" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"' />
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>

                        <!-- Правая колонка -->
                        <div>
                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "vacancy.edit.label.status"/> <span class="text-red-500">*</span></label>
                                <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
                                    <div class="flex space-x-4">
                                        <#assign activeLabel><@spring.message code="vacancy.edit.status.active" /></#assign>
                                        <#assign inactiveLabel><@spring.message code="vacancy.edit.status.inactive" /></#assign>
                                        <@spring.formRadioButtons
                                        "vacancyForm.isActive",
                                        { "true": activeLabel, "false": inactiveLabel },
                                        "<br>",
                                        "class='h-4 w-4 text-blue-600 border-gray-300 focus:ring-blue-500'"
                                        />
                                        <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "vacancy.edit.label.experience"/> <span class="text-red-500">*</span></label>
                                <div class="grid grid-cols-2 gap-4">
                                    <div>
                                        <label for="expFrom" class="block text-xs text-gray-500"><@spring.message "vacancy.edit.label.expFrom"/></label>
                                        <@spring.formInput "vacancyForm.expFrom", 'type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"' />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>

                                    <div>
                                        <label for="expTo" class="block text-xs text-gray-500"><@spring.message "vacancy.edit.label.expTo"/></label>
                                        <@spring.formInput "vacancyForm.expTo", 'type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"' />
                                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Описание вакансии -->
                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-file-alt text-blue-600 mr-2"></i><@spring.message "vacancy.edit.section.description"/>
                    </h3>

                    <div class="mb-4">
                        <label for="description" class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "vacancy.edit.label.fullDescription"/></label>
                        <@spring.formTextarea "vacancyForm.description", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="6"' />
                        <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                        <p class="text-sm text-gray-500 mt-2">
                            <i class="fas fa-info-circle mr-1"></i><@spring.message "vacancy.edit.hint.description"/>
                        </p>
                    </div>
                </div>

                <!-- Кнопки действий -->
                <div class="p-6 bg-gray-50 flex flex-col sm:flex-row justify-between space-y-4 sm:space-y-0">
                    <div class="flex items-center space-x-2">
                        <a href="/vacancies/${vacancyForm.id}" class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-arrow-left mr-2"></i><@spring.message "vacancy.edit.back"/>
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</@main.layout>