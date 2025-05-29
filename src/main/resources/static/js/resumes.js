class ResumeManager {
    constructor() {
        this.currentFilters = {
            query: '',
            page: 1,
            size: 5,
            categoryId: null,
            sortBy: 'updatedAt',
            sortDirection: 'desc'
        };

        this.filtersReady = false;
        this.abortController = null;
        this.init();
    }

    init() {
        this.waitForFilters().then(() => {
            this.syncWithSavedFilters();
            this.bindEvents();
            this.loadResumes();
        });
    }

    async waitForFilters() {
        return new Promise((resolve) => {
            const checkFilters = () => {
                if (window.ResumeFilters && window.ResumeFilters.isReady()) {
                    resolve();
                } else {
                    setTimeout(checkFilters, 50);
                }
            };
            checkFilters();
        });
    }

    syncWithSavedFilters() {
        const savedFilters = window.ResumeFilters.getCurrent();
        if (savedFilters) {
            this.currentFilters = { ...this.currentFilters, ...savedFilters };
        }

        this.updateUIFromFilters();
        this.filtersReady = true;
    }

    updateUIFromFilters() {
        const searchInput = document.getElementById('search-input');
        if (searchInput && this.currentFilters.query) {
            searchInput.value = this.currentFilters.query;
        }

        const categorySelect = document.getElementById('category-select');
        if (categorySelect) {
            categorySelect.value = this.currentFilters.categoryId || '';
        }

        const sortBySelect = document.getElementById('sort-by-select');
        if (sortBySelect) {
            sortBySelect.value = this.currentFilters.sortBy;
        }

        const sortDirectionBtn = document.getElementById('sort-direction-btn');
        if (sortDirectionBtn) {
            sortDirectionBtn.setAttribute('data-direction', this.currentFilters.sortDirection);
            this.updateSortDirectionIcon(this.currentFilters.sortDirection);
        }

        this.updateClearSearchButton();
    }

    bindEvents() {
        const searchForm = document.getElementById('search-form');
        const searchInput = document.getElementById('search-input');
        const clearSearchBtn = document.getElementById('clear-search-btn');

        let searchTimeout;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(searchTimeout);

            this.showSearchIndicator();

            searchTimeout = setTimeout(() => {
                const query = e.target.value.trim();
                this.updateFilter('query', query);
            }, 300);
        });

        searchForm.addEventListener('submit', (e) => {
            e.preventDefault();
            clearTimeout(searchTimeout);
            const query = searchInput.value.trim();
            this.updateFilter('query', query);
        });

        clearSearchBtn.addEventListener('click', () => {
            clearTimeout(searchTimeout);
            searchInput.value = '';
            this.updateFilter('query', '');
        });

        const sortBySelect = document.getElementById('sort-by-select');
        const sortDirectionBtn = document.getElementById('sort-direction-btn');

        sortBySelect.addEventListener('change', () => {
            this.updateFilter('sortBy', sortBySelect.value);
        });

        sortDirectionBtn.addEventListener('click', () => {
            const currentDirection = sortDirectionBtn.getAttribute('data-direction');
            const newDirection = currentDirection === 'desc' ? 'asc' : 'desc';
            this.updateFilter('sortDirection', newDirection);
        });

        const categorySelect = document.getElementById('category-select');
        categorySelect.addEventListener('change', () => {
            const categoryId = categorySelect.value || null;
            this.updateFilter('categoryId', categoryId);
        });

        const clearFiltersBtn = document.getElementById('clear-filters-btn');
        clearFiltersBtn.addEventListener('click', () => {
            this.clearAllFilters();
        });
    }

    updateFilter(key, value) {
        if (!this.filtersReady) return;

        this.currentFilters[key] = value;

        if (key !== 'page') {
            this.currentFilters.page = 1;
        }

        window.ResumeFilters.update(key, value);

        this.updateUIFromCurrentFilters();

        this.loadResumes();
    }

    updateUIFromCurrentFilters() {
        const sortDirectionBtn = document.getElementById('sort-direction-btn');
        if (sortDirectionBtn) {
            sortDirectionBtn.setAttribute('data-direction', this.currentFilters.sortDirection);
            this.updateSortDirectionIcon(this.currentFilters.sortDirection);
        }

        this.updateClearSearchButton();
    }

    updateClearSearchButton() {
        const clearBtn = document.getElementById('clear-search-btn');
        if (this.currentFilters.query) {
            clearBtn.classList.remove('hidden');
        } else {
            clearBtn.classList.add('hidden');
        }
    }

    updateSortDirectionIcon(direction) {
        const btn = document.getElementById('sort-direction-btn');
        const icon = btn.querySelector('i');

        if (direction === 'desc') {
            icon.className = 'fas fa-sort-amount-down text-gray-600';
        } else {
            icon.className = 'fas fa-sort-amount-up text-gray-600';
        }
    }

    clearAllFilters() {
        window.ResumeFilters.clearAll();

        this.currentFilters = {
            query: '',
            page: 1,
            size: 5,
            categoryId: null,
            sortBy: 'updatedAt',
            sortDirection: 'desc'
        };

        document.getElementById('search-input').value = '';
        document.getElementById('category-select').value = '';
        document.getElementById('sort-by-select').value = 'updatedAt';
        document.getElementById('sort-direction-btn').setAttribute('data-direction', 'desc');

        this.updateClearSearchButton();
        this.updateSortDirectionIcon('desc');

        this.loadResumes();
    }

    showSearchIndicator() {
        const searchForm = document.getElementById('search-form');
        const searchIcon = searchForm.querySelector('.fas.fa-search');

        if (searchIcon && !searchIcon.classList.contains('fa-spinner')) {
            searchIcon.className = 'fas fa-spinner fa-spin text-gray-500 ml-3 mr-2';
        }
    }

    hideSearchIndicator() {
        const searchForm = document.getElementById('search-form');
        const searchIcon = searchForm.querySelector('.fas');

        if (searchIcon && searchIcon.classList.contains('fa-spinner')) {
            searchIcon.className = 'fas fa-search text-gray-500 ml-3 mr-2';
        }
    }

    showLoader() {
        document.getElementById('loading-spinner').classList.remove('hidden');
        document.getElementById('resumes-container').classList.add('hidden');
        document.getElementById('no-resumes').classList.add('hidden');
        document.getElementById('pagination-container').classList.add('hidden');
    }

    hideLoader() {
        document.getElementById('loading-spinner').classList.add('hidden');
        document.getElementById('resumes-container').classList.remove('hidden');
        document.getElementById('pagination-container').classList.remove('hidden');
    }

    showNoResumes() {
        document.getElementById('loading-spinner').classList.add('hidden');
        document.getElementById('resumes-container').classList.add('hidden');
        document.getElementById('pagination-container').classList.add('hidden');

        const noResumesDiv = document.getElementById('no-resumes');
        const titleElement = document.getElementById('no-resumes-title');
        const descriptionElement = document.getElementById('no-resumes-description');

        const hasActiveSearch = this.currentFilters.query && this.currentFilters.query.trim() !== '';
        const hasActiveCategory = this.currentFilters.categoryId !== null;

        if (hasActiveSearch || hasActiveCategory) {
            titleElement.textContent = window.i18n['resumes.search.noResults'];
            descriptionElement.textContent = window.i18n['resumes.search.tryOther'];
        } else {
            titleElement.textContent = window.i18n['resumes.empty.title'];
            descriptionElement.textContent = window.i18n['resumes.empty.description'];
        }

        noResumesDiv.classList.remove('hidden');
    }

    async loadResumes() {
        if (this.abortController) {
            this.abortController.abort();
        }

        this.abortController = new AbortController();

        this.showLoader();
        this.hideSearchIndicator();

        try {
            const params = new URLSearchParams();

            if (this.currentFilters.query) params.append('query', this.currentFilters.query);
            params.append('page', this.currentFilters.page);
            params.append('size', this.currentFilters.size);
            if (this.currentFilters.categoryId) params.append('categoryId', this.currentFilters.categoryId);
            params.append('sortBy', this.currentFilters.sortBy);
            params.append('sortDirection', this.currentFilters.sortDirection);

            const response = await fetch(`/api/resumes?${params.toString()}`, {
                signal: this.abortController.signal
            });

            if (response.status === 404 || !response.ok) {
                this.showNoResumes();
                this.updateResumesCount(0);
                return;
            }

            const data = await response.json();

            if (!data.content || data.content.length === 0) {
                this.showNoResumes();
                this.updateResumesCount(0);
                return;
            }

            this.renderResumes(data);
            this.renderPagination(data);
            this.updateResumesCount(data.totalElements);
            this.hideLoader();

        } catch (error) {
            if (error.name === 'AbortError') {
                console.log('Запрос резюме был отменен');
                return;
            }

            console.error('Ошибка загрузки резюме:', error);
            this.showNoResumes();
            this.updateResumesCount(0);
        } finally {
            this.abortController = null;
        }
    }

    updateResumesCount(count) {
        document.getElementById('resumes-count').textContent =
            `${window.i18n['resumes.count']}: ${count}`;
    }

    renderResumes(data) {
        const container = document.getElementById('resumes-container');
        container.innerHTML = '';

        data.content.forEach(resume => {
            const resumeElement = this.createResumeElement(resume);
            container.appendChild(resumeElement);
        });
    }

    createResumeElement(resume) {
        const div = document.createElement('div');
        div.className = 'bg-white rounded-lg shadow border border-gray-200 hover:shadow-md transition-all duration-200 cursor-pointer group';
        div.onclick = () => window.location.href = `/resumes/${resume.id}`;

        const salary = resume.salary && resume.salary > 0
            ? `${resume.salary.toLocaleString()} ₽`
            : window.i18n['resumes.salary.notSpecified'];

        let totalYears = 0;
        let experienceTag = '';

        if (resume.workExperiences && resume.workExperiences.length > 0) {
            resume.workExperiences.forEach(exp => {
                totalYears += exp.years || 0;
            });
            experienceTag = `<span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                ${window.i18n['resumes.experience.years']}: ${totalYears}
                             </span>`;
        } else {
            experienceTag = `<span class="bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded">
                                ${window.i18n['resumes.experience.none']}
                             </span>`;
        }

        const statusClass = resume.isActive ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600';
        const statusText = resume.isActive
            ? window.i18n['resumes.status.active']
            : window.i18n['resumes.status.inactive'];

        let lastJobInfo = '';
        if (resume.workExperiences && resume.workExperiences.length > 0) {
            const lastJob = resume.workExperiences[0];
            lastJobInfo = `<p class="text-gray-600 text-sm">
                            <span class="font-medium">${window.i18n['resumes.lastPosition']}:</span> ${lastJob.position}
                            ${window.i18n['resumes.lastPosition.at']} ${lastJob.companyName}
                           </p>`;
        } else {
            lastJobInfo = `<p class="text-gray-600 text-sm italic">
                            ${window.i18n['resumes.experience.notSpecified']}
                           </p>`;
        }

        div.innerHTML = `
            <div class="p-4">
                <div class="flex items-center gap-4">
                    <div class="flex-shrink-0">
                        <div class="w-22 h-22 rounded-full overflow-hidden bg-gray-200 hover:ring-2 hover:ring-blue-500 transition-all duration-200"
                             onclick="event.stopPropagation(); window.location.href='/applicants/${resume.applicant.id}'">
                            <img src="/api/users/${resume.applicant.id}/avatar?size=small"
                                 alt="${resume.applicant.name || ''} ${resume.applicant.surname || ''}"
                                 class="w-full h-full object-cover">
                        </div>
                    </div>

                    <div class="flex-1 min-w-0">
                        <div class="flex justify-between items-start">
                            <div class="flex-1">
                                <h3 class="font-bold text-lg text-gray-800 group-hover:text-blue-600 transition-colors duration-200">
                                    ${resume.name}
                                </h3>
                                <p class="text-gray-600 mb-2">${resume.applicant.name || ''} ${resume.applicant.surname || ''}</p>
                                <div class="text-blue-600 font-medium mb-3">
                                    ${salary}
                                </div>

                                <div class="flex flex-wrap gap-2 mb-3">
                                    ${experienceTag}
                                    <span class="${statusClass} text-xs px-2 py-1 rounded">
                                        ${statusText}
                                    </span>
                                    <span class="bg-blue-100 text-blue-700 text-sm px-3 py-1 rounded-full">
                                        <i class="fas fa-tags mr-1"></i>${resume.category.name}
                                    </span>
                                </div>

                                ${lastJobInfo}
                            </div>

                            <div class="flex flex-col items-end text-xs text-gray-400 ml-4">
                                <span class="mb-1">
                                    ${window.i18n['resumes.created']}: ${this.formatDate(resume.createdAt)}
                                </span>
                                <span>
                                    ${window.i18n['resumes.updated']}: ${this.formatDate(resume.updatedAt)}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        return div;
    }

    renderPagination(data) {
        const container = document.getElementById('pagination-container');
        container.innerHTML = '';

        if (data.totalPages <= 1) {
            return;
        }

        const currentPage = data.number + 1;
        const totalPages = data.totalPages;

        const pageInfo = document.createElement('div');
        pageInfo.className = 'text-gray-500 text-sm mr-4';
        pageInfo.textContent = `${window.i18n['pagination.page']} ${currentPage} ${window.i18n['pagination.of']} ${totalPages}`;
        container.appendChild(pageInfo);

        const prevBtn = this.createPaginationButton(
            window.i18n['pagination.previous'],
            currentPage > 1 ? currentPage - 1 : null,
            '<i class="fas fa-chevron-left mr-1"></i> '
        );
        container.appendChild(prevBtn);

        const pagesContainer = document.createElement('div');
        pagesContainer.className = 'flex space-x-1';

        const startPage = Math.max(1, currentPage - 2);
        const endPage = Math.min(totalPages, startPage + 4);

        if (startPage > 1) {
            pagesContainer.appendChild(this.createPageButton(1));
            if (startPage > 2) {
                const dots = document.createElement('span');
                dots.className = 'px-2 py-2';
                dots.textContent = '...';
                pagesContainer.appendChild(dots);
            }
        }

        for (let i = startPage; i <= endPage; i++) {
            pagesContainer.appendChild(this.createPageButton(i, i === currentPage));
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                const dots = document.createElement('span');
                dots.className = 'px-2 py-2';
                dots.textContent = '...';
                pagesContainer.appendChild(dots);
            }
            pagesContainer.appendChild(this.createPageButton(totalPages));
        }

        container.appendChild(pagesContainer);

        const nextBtn = this.createPaginationButton(
            window.i18n['pagination.next'],
            currentPage < totalPages ? currentPage + 1 : null,
            '',
            ' <i class="fas fa-chevron-right ml-1"></i>'
        );
        container.appendChild(nextBtn);
    }

    createPaginationButton(text, page, prefixIcon = '', suffixIcon = '') {
        const button = document.createElement('button');

        if (page) {
            button.className = 'px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 cursor-pointer transition-colors';
            button.onclick = () => this.goToPage(page);
        } else {
            button.className = 'px-4 py-2 bg-gray-300 text-gray-500 rounded cursor-not-allowed';
            button.disabled = true;
        }

        button.innerHTML = prefixIcon + text + suffixIcon;
        return button;
    }

    createPageButton(page, isActive = false) {
        const button = document.createElement('button');
        button.className = `px-3 py-2 rounded transition-colors hover:shadow cursor-pointer ${
            isActive ? 'bg-blue-600 text-white' : 'hover:bg-gray-100'
        }`;
        button.textContent = page;

        if (!isActive) {
            button.onclick = () => this.goToPage(page);
        }

        return button;
    }

    goToPage(page) {
        this.updateFilter('page', page);
    }

    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('ru-RU', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new ResumeManager();
});