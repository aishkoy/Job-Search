<#import '../layout.ftlh' as main>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JobSearch - Сброс пароля</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body class="bg-gray-100 flex justify-center items-center min-h-screen p-4">
<div class="w-full max-w-md">
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6 bg-blue-600 text-white flex flex-col items-center">
            <div class="w-20 h-20 bg-white rounded-full flex items-center justify-center mb-3">
                <i class="fas fa-briefcase text-blue-600 text-3xl"></i>
            </div>
            <h1 class="text-xl font-bold">JobSearch</h1>
            <p class="text-sm">Найдите работу вашей мечты</p>
        </div>

        <div class="p-6">
            <h2 class="text-xl font-bold text-center mb-6">Создание нового пароля</h2>

            <#if error??>
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <strong class="font-bold">Ошибка!</strong>
                    <span class="block sm:inline">${error}</span>
                </div>
            </#if>

            <form action="/auth/reset-password" method="post" class="space-y-4">
                <@main.csrf/>
                <input type="hidden" name="token" value="${token!}">

                <div class="space-y-2">
                    <label for="password" class="block text-sm font-medium text-gray-700">
                        Новый пароль
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i class="fas fa-lock text-gray-400"></i>
                        </div>
                        <input
                                type="password"
                                id="password"
                                name="password"
                                class="w-full pl-10 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        >
                    </div>
                </div>

                <button
                        type="submit"
                        class="w-full bg-blue-600 text-white py-3 rounded-lg font-medium flex items-center justify-center gap-2 hover:bg-blue-700 transition-colors"
                >
                    <i class="fas fa-key"></i>
                    <span>Сохранить новый пароль</span>
                </button>
            </form>

            <div class="text-center mt-6">
                <p class="text-sm text-gray-600">
                    Вспомнили пароль?
                    <a href="/auth/login" class="text-blue-600 font-medium hover:underline ml-1">
                        Войти
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>