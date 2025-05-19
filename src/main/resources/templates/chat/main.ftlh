<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.layout>
    <div class="container mx-auto py-8 px-4">
        <h1 class="text-2xl font-bold mb-8 text-center text-gray-800">
            <#if isEmployer?? && isEmployer>
                <@spring.message "chat.title.employer"/>
            <#else>
                <@spring.message "chat.title.applicant"/>
            </#if>
        </h1>

        <div class="bg-white shadow-md rounded-lg overflow-hidden max-w-4xl mx-auto">
            <div class="p-6">
                <div id="chat-items-container" class="space-y-4">
                    <div class="flex justify-center">
                        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                    </div>
                </div>

                <div class="mt-8 flex justify-center" id="pagination-container">
                </div>
            </div>
        </div>
    </div>

    <script>
        const isEmployerRole = ${isEmployer?string('true', 'false')};
        const noVacanciesMessage = '<@spring.message "chat.no.vacancies"/>';
        const noResumesMessage = '<@spring.message "chat.no.resumes"/>';
        const errorLoadingMessage = '<@spring.message "chat.error.loading"/>';
        const retryMessage = '<@spring.message "chat.retry"/>';
    </script>

    <script src="/static/js/chat-main.js"></script>
</@layout.layout>