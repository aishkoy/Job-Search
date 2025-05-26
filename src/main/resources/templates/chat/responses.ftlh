<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.layout>
    <div class="container mx-auto py-8 px-4">
        <div class="max-w-4xl mx-auto">
            <div class="mb-4">
                <a href="/chat" class="inline-flex items-center text-blue-600 hover:text-blue-800">
                    <i class="fas fa-arrow-left mr-2"></i> <@spring.message "chat.back"/>
                </a>
            </div>

            <div class="bg-white shadow-md rounded-lg overflow-hidden mb-6">
                <div class="p-6">
                    <h1 id="item-title" class="text-2xl font-bold mb-1 text-gray-800">
                    </h1>
                    <p id="item-subtitle" class="text-gray-600 mb-4">
                    </p>
                </div>
            </div>

            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <div class="p-6">
                    <h2 class="text-xl font-semibold mb-6 text-gray-800">
                        <@spring.message "chat.conversations"/>
                    </h2>

                    <div id="responses-container" class="space-y-4">
                        <div class="flex justify-center">
                            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                        </div>
                    </div>

                    <div class="mt-8 flex justify-center" id="pagination-container">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const messages = {
            statusConfirmed: '<@spring.message "chat.status.confirmed"/>',
            statusPending: '<@spring.message "chat.status.pending"/>',
            noResponses: '<@spring.message "chat.no.responses"/>',
            errorLoading: '<@spring.message "chat.error.loading"/>',
            retry: '<@spring.message "chat.retry"/>'
        };
    </script>

    <script src="/static/js/chat-responses.js"></script>
</@layout.layout>