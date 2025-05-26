<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-address-card mr-2"></i>
                        <#if mode == 'create'><@spring.message"contact.create.title"/><#else><@spring.message"contact.edit.title"/></#if>
                    </h1>
                    <#if mode == 'edit'>
                        <form action="/resumes/${resumeId}/contacts/${contactDto.id}/delete" method="post"
                              onsubmit="return confirm('<@spring.message "contact.delete.confirm"/>');"
                              class="inline">
                            <@main.csrf/>
                            <button type="submit"
                                    class="bg-red-600/80 backdrop-blur-sm text-white px-4 py-2 rounded-md hover:bg-red-700/90 transition font-medium"
                                    title="<@spring.message "contact.delete"/>">
                                <i class="fas fa-trash-alt mr-2"></i><@spring.message "contact.delete"/>
                            </button>
                        </form>
                    </#if>
                </div>
            </div>

            <form action="${action}" method="post" class="p-6 space-y-6">
                <@main.csrf/>
                <input type="hidden" name="resumeId" value="${contactDto.resumeId}"/>
                <#if mode == 'edit'>
                    <input type="hidden" name="id" value="${contactDto.id}"/>
                </#if>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message"contact.type.label"/></label>
                    <#assign contactTypeMap = {}>
                    <#list contactTypes as contactType>
                        <#assign contactTypeMap = contactTypeMap + {contactType.id?string: contactType.name}>
                    </#list>

                    <@spring.formSingleSelect
                    "contactDto.contactType.id",
                    contactTypeMap,
                    'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-semibold text-gray-700 mb-2"><@spring.message"contact.value.label"/></label>
                    <@spring.formInput
                    "contactDto.contactValue",
                    'class="block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                    />
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>

                <div class="flex justify-between items-center">
                    <a href="/resumes/${resumeId}/edit"
                       class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                        <i class="fas fa-arrow-left mr-2"></i><@spring.message"contact.back"/>
                    </a>
                    <button type="submit"
                            class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-all">
                        <i class="fas fa-save mr-2"></i>
                        <#if mode == 'create'><@spring.message"contact.create.button"/><#else><@spring.message"contact.edit.button"/></#if>
                    </button>
                </div>
            </form>
        </div>
    </div>
</@main.layout>