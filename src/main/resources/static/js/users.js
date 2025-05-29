class UsersPage {
    constructor() {
        this.currentUserType = 'employers';
        this.currentPage = 1;
        this.pageSize = 5;
        this.currentQuery = '';
        this.usersData = null;
        this.searchTimeout = null;
        this.searchDelay = 300;
        this.currentUser = null;
        this.i18n = window.i18n || {};

        this.init();
    }

    getMessage(key, defaultValue = key) {
        return this.i18n[key] || defaultValue;
    }

    async init() {
        await this.loadCurrentUser();
        this.bindEvents();
        await this.loadUsers();
    }

    async loadCurrentUser() {
        try {
            const response = await fetch('/api/users/current');
            this.currentUser = await response.json();

            if (this.currentUser.role.name === 'EMPLOYER') {
                this.currentUserType = 'applicants';
            } else {
                this.currentUserType = 'employers';
            }


        } catch (error) {
            console.error('Error loading current user:', error);
            this.currentUser = null;
            this.currentUserType = 'employers';
        }
    }

    bindEvents() {
        document.getElementById('searchForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.performSearch();
        });

        document.getElementById('searchQuery').addEventListener('input', (e) => {
            this.handleRealTimeSearch(e.target.value);
        });
    }

    handleRealTimeSearch(query) {
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }

        this.searchTimeout = setTimeout(() => {
            this.currentQuery = query.trim();
            this.currentPage = 1;
            this.loadUsers();
        }, this.searchDelay);
    }

    performSearch() {
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }

        this.currentQuery = document.getElementById('searchQuery').value.trim();
        this.currentPage = 1;
        this.loadUsers();
    }

    async loadUsers() {
        this.showLoading(true);

        try {
            const params = new URLSearchParams({
                page: this.currentPage,
                size: this.pageSize
            });

            if (this.currentQuery) {
                params.append('query', this.currentQuery);
            }

            const response = await fetch(`/api/users/${this.currentUserType}?${params}`);

            if (response.status === 404) {
                this.showNotFound();
                return;
            }

            this.usersData = await response.json();
            this.renderUsers();
            this.renderPagination();

        } catch (error) {
            console.error('Error loading users:', error);

            if (error.message.includes('404')) {
                this.showNotFound();
            } else {
                this.showError('Failed to load users. Please try again.');
            }
        } finally {
            this.showLoading(false);
        }
    }

    showLoading(show) {
        const loading = document.getElementById('loading');
        const usersList = document.getElementById('usersList');
        const pagination = document.getElementById('pagination');

        loading.classList.toggle('hidden', !show);

        if (show) {
            usersList.innerHTML = '';
            pagination.innerHTML = '';
        }
    }

    showNotFound() {
        const usersList = document.getElementById('usersList');
        const totalResults = document.getElementById('totalResults');
        const pagination = document.getElementById('pagination');

        totalResults.textContent = this.getMessage('users.total.found') + ': 0';
        pagination.innerHTML = '';

        const searchText = this.currentQuery ? ` "${this.currentQuery}"` : '';

        usersList.innerHTML = `
            <div class="bg-white rounded-lg shadow p-6 text-center">
                <i class="fas fa-search text-gray-400 text-4xl mb-4"></i>
                <h3 class="font-bold text-lg text-gray-800 mb-2">${this.getMessage('users.not.found.title', 'No users found')}</h3>
                <p class="text-gray-600">
                    ${this.getMessage('users.not.found.description', 'No users were found matching your criteria.')}${searchText}
                    <br>${this.getMessage('users.empty.description')}
                </p>
                ${this.currentQuery ? `
                    <button onclick="usersPage.clearSearch()" 
                            class="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                        ${this.getMessage('users.clear.search', 'Clear Search')}
                    </button>
                ` : ''}
            </div>
        `;
    }

    clearSearch() {
        document.getElementById('searchQuery').value = '';
        this.currentQuery = '';
        this.currentPage = 1;

        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }

        this.loadUsers();
    }

    renderUsers() {
        const usersList = document.getElementById('usersList');
        const totalResults = document.getElementById('totalResults');
        const pageTitle = document.getElementById('pageTitle');

        totalResults.textContent = this.getMessage('users.total.found') + ': ' + this.usersData.totalElements;

        let title;
        if (this.currentUserType === 'employers') {
            title = this.getMessage('users.employers');
        } else {
            title = this.getMessage('users.jobseekers');
        }

        if (this.currentUser) {
            if (this.currentUser.role.name === 'EMPLOYER') {
                title = this.getMessage('users.available.jobseekers', 'Available Job Seekers');
            } else {
                title = this.getMessage('users.available.employers', 'Available Employers');
            }
        }

        pageTitle.textContent = title;

        if (!this.usersData.content || this.usersData.content.length === 0) {
            this.showNotFound();
            return;
        }

        usersList.innerHTML = this.usersData.content.map(user => this.renderUserCard(user)).join('');
    }

    renderUserCard(user) {
        const isEmployer = user.role.name === 'EMPLOYER';

        const userName = user.name || '';
        const userSurname = user.surname || '';
        const userEmail = user.email || '';

        const avatarHtml = user.avatar
            ? `<img src="/api/users/${user.id}/avatar" alt="${this.escapeHtml(userName)}" 
                class="w-16 h-16 rounded-full object-cover"
                onerror="this.outerHTML='&lt;div class=&quot;w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center&quot;&gt;&lt;i class=&quot;fas fa-user text-gray-500 text-2xl&quot;&gt;&lt;/i&gt;&lt;/div&gt;'">`
            : `<div class="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center">
             <i class="fas fa-user text-gray-500 text-2xl"></i>
           </div>`;

        const roleInfo = isEmployer
            ? `<i class="fas fa-briefcase mr-2"></i>${this.getMessage('profile.role.employer')}`
            : `<i class="fas fa-user-tie mr-2"></i>${this.getMessage('profile.role.applicant')}`;

        const countInfo = isEmployer
            ? `<div class="text-blue-600 font-medium">${this.getMessage('users.vacancies')}: ${user.vacanciesCount || 0}</div>`
            : `<div class="text-blue-600 font-medium">${this.getMessage('users.resumes')}: ${user.resumesCount || 0}</div>`;

        const ageInfo = user.age ? `<div>${this.getMessage('profile.age')}: ${user.age}</div>` : '';

        return `
        <div class="bg-white rounded-lg shadow p-4 border border-gray-200 hover:shadow-md transition-shadow">
            <div class="flex justify-between items-start">
                <div class="flex items-center space-x-4">
                    ${avatarHtml}
                    <div>
                        <h3 class="font-bold text-lg text-gray-800">
                            ${this.escapeHtml(userName)} ${this.escapeHtml(userSurname)}
                        </h3>
                        <p class="text-gray-600 text-sm">
                            ${roleInfo}
                        </p>
                    </div>
                </div>
                <div class="text-right">
                    ${countInfo}
                </div>
            </div>
            
            <div class="mt-4 flex justify-between items-center">
                <div class="flex flex-col text-gray-500 text-sm">
                    <div>
                        <i class="fas fa-envelope mr-2"></i>${this.escapeHtml(userEmail)}
                    </div>
                    ${ageInfo}
                </div>
                
                <a href="/${this.currentUserType}/${user.id}" 
                   class="text-blue-600 hover:text-blue-800 text-sm font-medium">
                    ${this.getMessage('users.profile.link')}
                    <i class="fas fa-arrow-right ml-2"></i>
                </a>
            </div>
        </div>
    `;
    }

    renderPagination() {
        const pagination = document.getElementById('pagination');

        if (!this.usersData || this.usersData.totalPages <= 1) {
            pagination.innerHTML = '';
            return;
        }

        const currentPage = this.usersData.number + 1;
        const totalPages = this.usersData.totalPages;

        let paginationHtml = `
            <div class="text-gray-500 text-sm mr-4">
                ${this.getMessage('pagination.page')} ${currentPage} ${this.getMessage('pagination.of')} ${totalPages}
            </div>
        `;

        if (this.usersData.number > 0) {
            paginationHtml += `
                <button onclick="usersPage.goToPage(${currentPage - 1})" 
                        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 cursor-pointer transition-colors">
                    <i class="fas fa-chevron-left mr-1"></i> ${this.getMessage('pagination.previous')}
                </button>
            `;
        } else {
            paginationHtml += `
                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                    <i class="fas fa-chevron-left mr-1"></i> ${this.getMessage('pagination.previous')}
                </span>
            `;
        }

        paginationHtml += '<div class="flex space-x-1">';

        const startPage = Math.max(1, currentPage - 2);
        const endPage = Math.min(totalPages, startPage + 4);

        if (startPage > 1) {
            paginationHtml += `
                <button onclick="usersPage.goToPage(1)" 
                        class="px-3 py-2 rounded hover:bg-gray-100 hover:shadow-md cursor-pointer transition-colors">1</button>
            `;
            if (startPage > 2) {
                paginationHtml += '<span class="px-2 py-2">...</span>';
            }
        }

        for (let i = startPage; i <= endPage; i++) {
            const isActive = i === currentPage;
            paginationHtml += `
                <button onclick="usersPage.goToPage(${i})" 
                        class="px-3 py-2 rounded ${isActive ? 'bg-blue-600 text-white' : 'hover:bg-gray-100'} hover:shadow-md cursor-pointer transition-colors">
                    ${i}
                </button>
            `;
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                paginationHtml += '<span class="px-2 py-2">...</span>';
            }
            paginationHtml += `
                <button onclick="usersPage.goToPage(${totalPages})" 
                        class="px-3 py-2 rounded hover:bg-gray-100 hover:shadow-md cursor-pointer transition-colors">${totalPages}</button>
            `;
        }

        paginationHtml += '</div>';

        if (this.usersData.number < this.usersData.totalPages - 1) {
            paginationHtml += `
                <button onclick="usersPage.goToPage(${currentPage + 1})" 
                        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 cursor-pointer transition-colors">
                    ${this.getMessage('pagination.next')} <i class="fas fa-chevron-right ml-1"></i>
                </button>
            `;
        } else {
            paginationHtml += `
                <span class="px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed">
                    ${this.getMessage('pagination.next')} <i class="fas fa-chevron-right ml-1"></i>
                </span>
            `;
        }

        pagination.innerHTML = paginationHtml;
    }

    goToPage(page) {
        this.currentPage = page;
        this.loadUsers();
    }

    showError(message) {
        const usersList = document.getElementById('usersList');
        const totalResults = document.getElementById('totalResults');
        const pagination = document.getElementById('pagination');

        totalResults.textContent = this.getMessage('users.total.found') + ': 0';
        pagination.innerHTML = '';

        usersList.innerHTML = `
            <div class="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
                <i class="fas fa-exclamation-triangle text-red-500 text-2xl mb-4"></i>
                <h3 class="font-bold text-lg text-red-800 mb-2">${this.getMessage('users.error.title', 'Error Loading Users')}</h3>
                <p class="text-red-700 mb-4">${message || this.getMessage('users.error.description', 'Failed to load users. Please try again.')}</p>
                <button onclick="usersPage.loadUsers()" 
                        class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors">
                    ${this.getMessage('users.try.again', 'Try Again')}
                </button>
            </div>
        `;
    }

    escapeHtml(text) {
        if (!text) return '';

        return text.toString()
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    destroy() {
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }
    }
}

let usersPage;
document.addEventListener('DOMContentLoaded', () => {
    usersPage = new UsersPage();
});

window.addEventListener('beforeunload', () => {
    if (usersPage) {
        usersPage.destroy();
    }
});