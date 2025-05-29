<#import '../layout.ftlh' as main>
<#import '/spring.ftl' as spring>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@spring.message "auth.forgot.site.title"/></title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body class="bg-gray-100 flex justify-center items-center min-h-screen p-4">
<div class="w-full max-w-md">
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6 bg-blue-600 text-white flex flex-col items-center">
            <div class="w-20 h-20 bg-white rounded-full flex items-center justify-center mb-3">
                <i class="fas fa-briefcase text-blue-600 text-3xl"></i>
            </div>
            <h1 class="text-xl font-bold">JobSearch</h1>
        </div>

        <div class="p-6">
            <h2 class="text-xl font-bold text-center mb-6"><@spring.message "auth.forgot.title"/></h2>

            <#if message??>
                <div class="bg-red-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <span class="block sm:inline">${message}</span>
                </div>
            </#if>

            <form action="/auth/forgot-password" method="post" class="space-y-4">
                <@main.csrf/>
                <div class="space-y-2">
                    <label for="email" class="block text-sm font-medium text-gray-700">
                        <@spring.message "auth.forgot.email.label"/>
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i class="fas fa-envelope text-gray-400"></i>
                        </div>
                        <input
                                type="email"
                                id="email"
                                name="email"
                                placeholder="<@spring.message "auth.forgot.email.placeholder"/>"
                                class="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
                        />
                    </div>
                </div>

                <#if error??>
                    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                        <strong class="font-bold"><@spring.message "auth.forgot.error.title"/></strong>
                        <span class="block sm:inline">${error}</span>
                    </div>
                </#if>

                <button
                        type="submit"
                        class="w-full bg-blue-600 text-white py-3 rounded-lg font-medium flex items-center justify-center gap-2 hover:bg-blue-700 transition-colors"
                >
                    <i class="fas fa-key"></i>
                    <span><@spring.message "auth.forgot.button"/></span>
                </button>
            </form>

            <div class="relative py-4 mt-4">
                <div class="absolute inset-0 flex items-center">
                    <div class="w-full border-t border-gray-300"></div>
                </div>
                <div class="relative flex justify-center">
                    <span class="bg-white px-4 text-sm text-gray-500"><@spring.message "auth.forgot.or"/></span>
                </div>
            </div>

            <div class="text-center mt-6">
                <p class="text-sm text-gray-600">
                    <@spring.message "auth.forgot.remembered"/>
                    <a href="/auth/login" class="text-blue-600 font-medium hover:underline ml-1">
                        <@spring.message "auth.forgot.login"/>
                    </a>
                </p>
                <p class="text-sm text-gray-600">
                    <@spring.message "auth.forgot.no.account"/>
                    <a href="/auth/register" class="text-blue-600 font-medium hover:underline ml-1">
                        <@spring.message "auth.forgot.register"/>
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>