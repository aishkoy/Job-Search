<#import "../layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="bg-blue-600 text-white pb-6 -mt-8">
        <div class="container mx-auto px-4 pt-8">
            <form id="searchForm">
                <div class="flex items-center bg-white rounded-full p-2 shadow-md">
                    <i class="fas fa-search text-gray-500 ml-3 mr-2"></i>
                    <input
                            type="text"
                            id="searchQuery"
                            name="query"
                            placeholder="<@spring.message "users.search.placeholder"/>"
                            class="flex-1 outline-none text-gray-800 py-2 px-1"
                    >
                    <button type="submit" class="bg-gray-100 text-gray-600 p-2 rounded-full mr-1">
                        <i class="fas fa-filter"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <main class="container mx-auto px-4 py-6">
        <div class="flex items-center justify-between mb-6">
            <div class="text-gray-500 text-sm" id="totalResults"><@spring.message "users.total.found"/>: 0</div>
        </div>

        <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-bold" id="pageTitle"><@spring.message "users.employers"/></h2>
        </div>

        <div id="loading" class="text-center py-8 hidden">
            <i class="fas fa-spinner fa-spin text-2xl text-blue-600"></i>
            <p class="text-gray-600 mt-2" id="loadingText"><@spring.message "users.loading"/></p>
        </div>

        <div class="space-y-4" id="usersList"></div>

        <div id="pagination" class="flex justify-center items-center mt-6 gap-2"></div>
    </main>

    <script>
        window.i18n = {
            'users.search.placeholder': '<@spring.message "users.search.placeholder"/>',
            'users.total.found': '<@spring.message "users.total.found"/>',
            'users.employers': '<@spring.message "users.employers"/>',
            'users.jobseekers': '<@spring.message "users.jobseekers"/>',
            'users.available.employers': '<@spring.message "users.available.employers"/>',
            'users.available.jobseekers': '<@spring.message "users.available.jobseekers"/>',
            'profile.role.employer': '<@spring.message "profile.role.employer"/>',
            'profile.role.applicant': '<@spring.message "profile.role.applicant"/>',
            'users.vacancies': '<@spring.message "users.vacancies"/>',
            'users.resumes': '<@spring.message "users.resumes"/>',
            'profile.age': '<@spring.message "profile.age"/>',
            'users.profile.link': '<@spring.message "users.profile.link"/>',
            'pagination.page': '<@spring.message "pagination.page"/>',
            'pagination.of': '<@spring.message "pagination.of"/>',
            'pagination.previous': '<@spring.message "pagination.previous"/>',
            'pagination.next': '<@spring.message "pagination.next"/>',
            'users.empty.title': '<@spring.message "users.empty.title"/>',
            'users.empty.description': '<@spring.message "users.empty.description"/>',
            'users.not.found.title': '<@spring.message "users.not.found.title"/>',
            'users.not.found.description': '<@spring.message "users.not.found.description"/>',
            'users.clear.search': '<@spring.message "users.clear.search"/>',
            'users.loading': '<@spring.message "users.loading"/>',
            'users.error.title': '<@spring.message "users.error.title"/>',
            'users.error.description': '<@spring.message "users.error.description"/>',
            'users.try.again': '<@spring.message "users.try.again"/>'
        };
    </script>
    <script src="/static/js/users.js"></script>
</@main.layout>