<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout showLanguageSwitcher=false>
    <div class="container mx-auto px-4 py-6">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
            <div class="bg-gradient-to-r from-blue-600 to-blue-500 text-white p-6">
                <div class="flex items-center justify-between">
                    <h1 class="text-2xl font-bold flex items-center">
                        <i class="fas fa-plus-circle mr-2"></i><@spring.message "resume.create.title"/>
                    </h1>
                </div>
                <p class="mt-2 text-blue-100 text-sm">
                    <@spring.message "resume.create.description"/>
                </p>
            </div>

            <form action="/resumes/create" method="post" class="space-y-6" id="resume-form">
                <@main.csrf/>
                <@spring.formHiddenInput "resumeForm.applicant.id"/>

                <div class="p-6 border-b">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-info-circle text-blue-600 mr-2"></i><@spring.message "resume.create.section.general"/>
                    </h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <div class="mb-4">
                                <label for="name" class="block text-sm font-semibold text-gray-700">
                                    <@spring.message "resume.create.name.label"/> <span class="text-red-500">*</span>
                                </label>
                                <@spring.formInput "resumeForm.name", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                <p class="text-xs text-gray-500 mt-1"><@spring.message "resume.create.name.hint"/></p>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>

                            <div class="mb-4">
                                <label for="category.id" class="block text-sm font-semibold text-gray-700">
                                    Категория <span class="text-red-500">*</span>
                                </label>
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

                        <div>
                            <div class="mb-4">
                                <label for="salary" class="block text-sm font-semibold text-gray-700">
                                    <@spring.message "resume.create.salary.label"/>
                                </label>
                                <@spring.formInput "resumeForm.salary", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" type="number" min="0"', "number"/>
                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Контактная информация -->
                <div class="p-6 border-b" id="contacts-section">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-address-card text-blue-600 mr-2"></i><@spring.message "resume.edit.contacts.title"/>
                    </h3>

                    <#if resumeForm.contacts?? && resumeForm.contacts?has_content>
                        <div class="space-y-4 mb-6" id="contacts-container">
                            <#list resumeForm.contacts as contact>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800"><@spring.messageArgs "resume.create.contacts.item", ['${contact?index + 1}']/> </h4>
                                        <div>
                                            <button type="button" class="contact-delete-btn text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div>
                                            <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.contacts.type"/></label>
                                            <#assign contactTypeMap = {}>
                                            <#list contactTypes as contactType>
                                                <#assign contactTypeMap = contactTypeMap + {contactType.id?string: contactType.name}>
                                            </#list>

                                            <@spring.formSingleSelect
                                            "resumeForm.contacts[${contact?index}].contactType.id",
                                            contactTypeMap,
                                            'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"'
                                            />
                                        </div>

                                        <div>
                                            <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.contacts.value"/></label>
                                            <@spring.formInput "resumeForm.contacts[${contact?index}].contactValue", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                            <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="space-y-4 mb-6 hidden" id="contacts-container"></div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="button" id="add-contact-btn"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> <@spring.message "resume.create.contacts.add"/>
                        </button>
                    </div>
                </div>

                <!-- Опыт работы -->
                <div class="p-6 border-b" id="work-experience-section">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-briefcase text-blue-600 mr-2"></i><@spring.message "resume.create.experience.title"/>
                    </h3>

                    <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                        <div class="space-y-4 mb-6" id="work-experience-container">
                            <#list resumeForm.workExperiences as workExperience>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800"><@spring.messageArgs "resume.create.experience.item", ['${workExperience?index + 1}']/></h4>
                                        <div>
                                            <button type="button" class="work-exp-delete-btn text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>

                                    <div class="space-y-4">
                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.edit.experience.company"/></label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].companyName", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.experience.position"/></label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].position", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>

                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.experience.years"/></label>
                                                <@spring.formInput "resumeForm.workExperiences[${workExperience?index}].years", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" type="number" min="0"', "number"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.experience.responsibilities"/></label>
                                                <@spring.formTextarea "resumeForm.workExperiences[${workExperience?index}].responsibilities", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500" rows="2"'/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="space-y-4 mb-6 hidden" id="work-experience-container"></div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="button" id="add-experience-btn"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> <@spring.message "resume.create.experience.add"/>
                        </button>
                    </div>
                </div>

                <!-- Образование -->
                <div class="p-6 border-b" id="education-section">
                    <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                        <i class="fas fa-graduation-cap text-blue-600 mr-2"></i><@spring.message "resume.create.education"/>
                    </h3>

                    <#if resumeForm.educations?? && resumeForm.educations?has_content>
                        <div class="space-y-4 mb-6" id="education-container">
                            <#list resumeForm.educations as education>
                                <div class="p-4 border border-gray-200 rounded-lg bg-gray-50">
                                    <div class="flex justify-between items-center mb-3">
                                        <h4 class="font-medium text-gray-800"><@spring.messageArgs "resume.create.education.title", ['${education?index + 1}']/></h4>
                                        <div>
                                            <button type="button" class="education-delete-btn text-red-500 hover:text-red-700">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </div>

                                    <div class="space-y-4">
                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.education.institution"/></label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].institution", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.education.program"/></label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].program", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>

                                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.education.startDate"/></label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].startDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.education.endDate"/></label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].endDate", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "date"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>

                                            <div>
                                                <label class="block text-sm font-medium text-gray-700"><@spring.message "resume.create.education.degree"/></label>
                                                <@spring.formInput "resumeForm.educations[${education?index}].degree", 'class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                                                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="space-y-4 mb-6 hidden" id="education-container"></div>
                    </#if>

                    <div class="flex justify-center">
                        <button type="button" id="add-education-btn"
                                class="flex items-center justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            <i class="fas fa-plus mr-2"></i> <@spring.message "resume.create.education.add"/>
                        </button>
                    </div>
                </div>

                <!-- Кнопки действий -->
                <div class="p-6 bg-gray-50 flex flex-col sm:flex-row justify-between space-y-4 sm:space-y-0">
                    <div class="flex items-center space-x-2">
                        <a href="/" class="text-gray-600 hover:text-blue-600 transition-all flex items-center">
                            <i class="fas fa-arrow-left mr-2"></i><@spring.message "resume.create.cancel"/>
                        </a>
                    </div>

                    <div class="flex space-x-3">
                        <button type="submit" name="action" value="save"
                                class="flex items-center justify-center py-2 px-6 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 shadow-sm">
                            <i class="fas fa-check mr-2"></i><@spring.message "resume.create.submit"/>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div id="message-storage" class="hidden">
        <div data-key="contact.title"><@spring.message "resume.create.contacts.item" /></div>
        <div data-key="contact.type"><@spring.message "resume.create.contacts.type" /></div>
        <div data-key="contact.value"><@spring.message "resume.create.contacts.value" /></div>

        <div data-key="experience.title"><@spring.message "resume.create.experience.item" /></div>
        <div data-key="experience.company"><@spring.message "resume.edit.experience.company" /></div>
        <div data-key="experience.position"><@spring.message "resume.create.experience.position" /></div>
        <div data-key="experience.years"><@spring.message "resume.create.experience.years" /></div>
        <div data-key="experience.responsibilities"><@spring.message "resume.create.experience.responsibilities" /></div>

        <div data-key="education.title"><@spring.message "resume.create.education.title" /></div>
        <div data-key="education.institution"><@spring.message "resume.create.education.institution" /></div>
        <div data-key="education.program"><@spring.message "resume.create.education.program" /></div>
        <div data-key="education.startDate"><@spring.message "resume.create.education.startDate" /></div>
        <div data-key="education.endDate"><@spring.message "resume.create.education.endDate" /></div>
        <div data-key="education.degree"><@spring.message "resume.create.education.degree" /></div>
    </div>

    <script src="/static/js/resume/create-resume.js"></script>
</@main.layout>