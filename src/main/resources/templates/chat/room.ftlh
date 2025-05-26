<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.layout>
    <div class="container mx-auto py-8 px-4">
        <div class="max-w-4xl mx-auto">
            <div class="mb-4">
                <a href="javascript:history.back()" class="inline-flex items-center text-blue-600 hover:text-blue-800">
                    <i class="fas fa-arrow-left mr-2"></i> <@spring.message "chat.back"/>
                </a>
            </div>

            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <div class="bg-gray-50 p-4 border-b">
                    <div class="flex justify-between items-center">
                        <div>
                            <h2 id="chat-title" class="font-semibold text-lg text-gray-800">
                            </h2>
                            <p id="chat-subtitle" class="text-gray-600 text-sm">
                            </p>
                        </div>
                        <div id="chat-status">
                        </div>
                    </div>
                </div>

                <div id="messages-container" class="p-4 h-96 overflow-y-auto">
                    <div class="flex justify-center">
                        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                    </div>
                </div>

                <div class="border-t p-4">
                    <form id="message-form" class="flex">
                        <input type="text" id="message-input" class="flex-grow border border-gray-300 rounded-l-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="<@spring.message "chat.message.placeholder"/>">
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-r-md hover:bg-blue-600 flex items-center">
                            <i class="fas fa-paper-plane mr-2"></i> <@spring.message "chat.send"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

    <script>
        window.messages = {
            statusConfirmed: '<@spring.message "chat.status.confirmed"/>',
            statusPending: '<@spring.message "chat.status.pending"/>',
            noMessages: '<@spring.message "chat.no.messages"/>',
            errorLoading: '<@spring.message "chat.error.loading"/>',
            retry: '<@spring.message "chat.retry"/>',
            you: '<@spring.message "chat.you"/>'
        };

        window.csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
        window.csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-CSRF-TOKEN';
    </script>

    <script src="/static/js/chat-room.js"></script>
</@layout.layout>