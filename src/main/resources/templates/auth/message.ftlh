<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JobSearch - Пароль сброшен</title>
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
            <p class="text-sm">Найдите работу вашей мечты</p>
        </div>

        <div class="p-6 text-center">
            <div class="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                <i class="fas fa-check text-green-600 text-3xl"></i>
            </div>

            <h2 class="text-xl font-bold mb-3">${message}</h2>

            <p class="text-gray-600 mb-8">
                Вы можете использовать новый пароль для входа в систему. Пожалуйста, храните его в надежном месте.
            </p>

            <a href="/auth/login"
               class="inline-block bg-blue-600 text-white py-3 px-6 rounded-lg font-medium hover:bg-blue-700 transition-colors">
                <i class="fas fa-sign-in-alt mr-2"></i>
                Перейти на страницу входа
            </a>
        </div>
    </div>
</div>
</body>
</html>