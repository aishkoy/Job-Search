<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mx-auto px-4 py-12">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden max-w-2xl mx-auto">
            <div class="bg-red-600 text-white py-4 px-6">
                <h1 class="text-3xl font-bold">${status}</h1>
            </div>

            <div class="py-8 px-6">
                <div class="flex items-center mb-6">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-red-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                    <h2 class="text-2xl font-semibold text-gray-800"><span data-l10n>UH OH</span>! <span data-l10n>${reason}</span></h2>
                </div>

                <#if details??>
                    <div class="mt-6 bg-gray-50 rounded-md p-4">
                        <h3 class="text-lg font-medium text-gray-700 mb-3"><span data-l10n>Технические детали</span>:</h3>
                        <ul class="space-y-2 text-sm">
                            <#if details.serverName??>
                                <li class="flex">
                                    <span class="font-medium text-gray-600 w-32"><span data-l10n>Host</span>:</span>
                                    <code class="bg-gray-200 px-2 py-1 rounded text-gray-800">${details.serverName}</code>
                                </li>
                            </#if>
                            <#if details.requestURL??>
                                <li class="flex flex-wrap">
                                    <span class="font-medium text-gray-600 w-32"><span data-l10n>Original URI</span>:</span>
                                    <code class="bg-gray-200 px-2 py-1 rounded text-gray-800">${details.requestURL}</code>
                                </li>
                            </#if>
                            <#if details.serverPort??>
                                <li class="flex">
                                    <span class="font-medium text-gray-600 w-32"><span data-l10n>Service port</span>:</span>
                                    <code class="bg-gray-200 px-2 py-1 rounded text-gray-800">${details.serverPort?c}</code>
                                </li>
                            </#if>
                            <#if details.requestId??>
                                <li class="flex">
                                    <span class="font-medium text-gray-600 w-32"><span data-l10n>Request ID</span>:</span>
                                    <code class="bg-gray-200 px-2 py-1 rounded text-gray-800">${details.requestId}</code>
                                </li>
                            </#if>
                            <li class="flex">
                                <span class="font-medium text-gray-600 w-32"><span data-l10n>Timestamp</span>:</span>
                                <code class="bg-gray-200 px-2 py-1 rounded text-gray-800">${.now}</code>
                            </li>
                        </ul>
                    </div>
                </#if>

                <div class="mt-8 text-center">
                    <a href="/" class="inline-block bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-md transition duration-200">
                        <span data-l10n>Вернуться на главную</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</@main.layout>