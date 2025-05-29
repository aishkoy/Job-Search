<#import '../layout.ftlh' as main>
<#import "/spring.ftl" as spring>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@spring.message "profile.edit.title"/></title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>

<body class="bg-gray-100 min-h-screen">
<div class="container mx-auto py-8">
    <div class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-md">
        <h1 class="text-2xl font-bold text-center text-gray-800 mb-6"><@spring.message "profile.edit.title"/></h1>

        <div class="flex flex-col items-center mb-6">
            <div class="relative">
                <img
                        id="user-avatar"
                        class="w-32 h-32 rounded-full object-cover border-4 border-gray-200"
                        src="/api/users/${userDto.id}/avatar"
                        alt="<@spring.message "profile.avatar.label"/>"
                >
            </div>

            <form method="POST" action="/${url}/${userDto.id}/avatar" enctype="multipart/form-data" class="mt-4">
                <@main.csrf/>
                <label for="avatar-upload"
                       class="cursor-pointer px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    <@spring.message "profile.avatar.change"/>
                </label>
                <input id="avatar-upload" name="file" type="file" accept="image/jpeg, image/png" class="hidden"
                       onchange="this.form.submit()">
            </form>
        </div>

        <form method="POST" action="/${url}/${userDto.id}/edit" class="space-y-6">
            <@main.csrf/>
            <input type="hidden" name="role.id" value="${userDto.role.id}">
            <input type="hidden" name="role.name" value="${userDto.role.name}">
            <input type="hidden" name="id" value="${userDto.id}">

            <div>
                <label for="name" class="block text-sm font-medium text-gray-700"><@spring.message "profile.name.label"/></label>
                <@spring.formInput "userDto.name", 'class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
            </div>

            <div>
                <label for="surname" class="block text-sm font-medium text-gray-700"><@spring.message "profile.surname.label"/></label>
                <@spring.formInput "userDto.surname", 'class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "text"/>
                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
            </div>

            <div>
                <label for="phoneNumber" class="block text-sm font-medium text-gray-700"><@spring.message "profile.phone.label"/></label>
                <@spring.formInput "userDto.phoneNumber", 'class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "tel"/>
                <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
            </div>

            <#if userDto.role.name == 'APPLICANT'>
                <div>
                    <label for="age" class="block text-sm font-medium text-gray-700"><@spring.message "profile.age.label"/></label>
                    <@spring.formInput "userDto.age", 'class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"', "number"/>
                    <@spring.showErrors '<br>', 'text-red-500 text-xs mt-1'/>
                </div>
            </#if>

            <div class="flex items-center justify-between pt-4">
                <a href="/${url}/${userDto.id}"
                   class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500">
                    <@spring.message "profile.cancel"/>
                </a>
                <button type="submit"
                        class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    <@spring.message "profile.save"/>
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>