<#import '../layout.ftlh' as main>
<#import '/spring.ftl' as spring>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@spring.message "auth.register.title"/></title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-4">
<div class="w-full max-w-md bg-white rounded-lg shadow-lg p-6 sm:p-8">
    <h1 class="text-2xl font-bold text-center text-gray-800 mb-6"><@spring.message "auth.register.title"/></h1>

    <form action="/auth/register" method="POST" class="space-y-6">
        <@main.csrf/>
        <div class="mb-6">
            <label for="role" class="block text-sm font-medium text-gray-700 mb-2">
                <@spring.message "auth.register.account.type"/>:
            </label>
            <select name="role.id"
                    id="role"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    onchange="toggleAgeField()">
                <option value="">-- <@spring.message "auth.register.select.role"/> --</option>
                <option name="role.id" value="1">
                    <@spring.message "auth.register.role.employer"/>
                </option>
                <option name="role.id" value="2">
                    <@spring.message "auth.register.role.applicant"/>
                </option>
            </select>
        </div>

        <div>
            <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.name.label"/>
            </label>
            <@spring.formInput "userDto.name", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500'", "text"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
        </div>

        <div>
            <label for="surname" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.surname.label"/>
            </label>
            <@spring.formInput "userDto.surname", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500'", "text"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
        </div>

        <div id="ageField" class="hidden">
            <label for="age" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.age.label"/>
            </label>
            <@spring.formInput "userDto.age", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500' min='18'", "number"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
            <p class="mt-1 text-xs text-gray-500"><@spring.message "auth.register.age.hint"/></p>
        </div>

        <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.email.label"/>
            </label>
            <@spring.formInput "userDto.email", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500'", "email"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
        </div>

        <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.password.label"/>
            </label>
            <@spring.formPasswordInput "userDto.password", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500' minlength='8' maxlength='20'"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
            <p class="mt-1 text-xs text-gray-500"><@spring.message "auth.register.password.hint"/></p>
        </div>

        <div>
            <label for="phoneNumber" class="block text-sm font-medium text-gray-700 mb-1">
                <@spring.message "auth.register.phone.label"/>
            </label>
            <@spring.formInput "userDto.phoneNumber", "class='w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500'", "tel"/>
            <@spring.showErrors "<br>", "text-red-500 text-xs mt-1"/>
        </div>

        <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition duration-200">
            <@spring.message "auth.register.button"/>
        </button>

        <div class="text-center mt-4">
            <p class="text-sm text-gray-600">
                <@spring.message "auth.register.have.account"/>
                <a href="/auth/login" class="text-blue-600 hover:text-blue-800 font-medium">
                    <@spring.message "auth.register.login"/>
                </a>
            </p>
        </div>
    </form>
</div>

<script>
    function toggleAgeField() {
        const roleSelect = document.getElementById('role');
        const ageField = document.getElementById('ageField');
        const ageInput = ageField.querySelector('input[name="age"]');

        if (roleSelect.value === '2') {
            ageField.classList.remove('hidden');
        } else {
            ageField.classList.add('hidden');
            if (ageInput) {
                ageInput.value = '';
            }
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        toggleAgeField();
    });
</script>

</body>
</html>