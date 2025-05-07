<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <h1 class="text-2xl font-bold flex items-center">
                    <i class="fas fa-briefcase mr-2"></i>
                    <#if mode == 'create'><@spring.message "experience.create.title"/><#else><@spring.message "experience.edit.title"/></#if>
                </h1>
            </div>

            <form action="${action}" method="post" class="p-6 space-y-6">
                <@main.csrf/>
                <input type="hidden" name="resumeId" value="${experienceDto.resumeId}"/>
                <#if mode == 'edit'>
                    <input type="hidden" name="id" value="${experienceDto.id}"/>
                </#if>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "experience.company.label"/></label>
                    <@spring.formInput
                    "experienceDto.companyName",
                    'class="block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "experience.position.label"/></label>
                    <@spring.formInput
                    "experienceDto.position",
                    'class="block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "experience.years.label"/></label>
                    <@spring.formInput
                    "experienceDto.years",
                    'type="number" min="1" max="80" class="block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message "experience.responsibilities.label"/></label>
                    <@spring.formTextarea
                    "experienceDto.responsibilities",
                    'class="block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 h-32"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="flex justify-between items-center">
                    <a href="/resumes/${resumeId}/edit"
                       class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                        <i class="fas fa-arrow-left mr-2"></i><@spring.message "experience.back"/>
                    </a>
                    <button type="submit"
                            class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-all">
                        <i class="fas fa-save mr-2"></i>
                        <#if mode == 'create'><@spring.message "experience.create.button"/><#else><@spring.message "experience.edit.button"/></#if>
                    </button>
                </div>
            </form>
        </div>
    </div>
</@main.layout>