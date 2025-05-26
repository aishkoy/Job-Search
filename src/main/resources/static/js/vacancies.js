document.addEventListener('DOMContentLoaded', function() {
    const STORAGE_KEY = 'vacancy_filters';
    const DEFAULT_FILTERS = {
        categoryId: null,
        sortBy: 'updatedAt',
        sortDirection: 'desc',
        size: 5,
        query: ''
    };

    initializeFilters();

    function initializeFilters() {
        const urlParams = getCurrentUrlParams();
        const savedFilters = getSavedFilters();

        if (hasUrlParams(urlParams)) {
            const currentFilters = mergeFilters(DEFAULT_FILTERS, urlParams);
            saveFilters(currentFilters);
            console.log('Filters loaded from URL and saved:', currentFilters);
        }
        else if (savedFilters && Object.keys(savedFilters).length > 0) {
            applyFiltersToUrl(savedFilters);
            console.log('Filters loaded from localStorage:', savedFilters);
        }

        setupEventListeners();
    }

    function getCurrentUrlParams() {
        const urlParams = new URLSearchParams(window.location.search);
        return {
            categoryId: urlParams.get('categoryId') ? parseInt(urlParams.get('categoryId')) : null,
            sortBy: urlParams.get('sortBy') || null,
            sortDirection: urlParams.get('sortDirection') || null,
            size: urlParams.get('size') ? parseInt(urlParams.get('size')) : null,
            query: urlParams.get('query') || null,
            page: urlParams.get('page') ? parseInt(urlParams.get('page')) : 1
        };
    }

    function hasUrlParams(params) {
        return params.categoryId !== null ||
            params.sortBy !== null ||
            params.sortDirection !== null ||
            params.size !== null ||
            params.query !== null;
    }

    function getSavedFilters() {
        try {
            const saved = localStorage.getItem(STORAGE_KEY);
            return saved ? JSON.parse(saved) : null;
        } catch (error) {
            console.error('Error reading filters from localStorage:', error);
            return null;
        }
    }

    function saveFilters(filters) {
        try {
            const filtersToSave = { ...filters };
            delete filtersToSave.page;

            localStorage.setItem(STORAGE_KEY, JSON.stringify(filtersToSave));
            console.log('Filters saved to localStorage:', filtersToSave);
        } catch (error) {
            console.error('Error saving filters to localStorage:', error);
        }
    }

    function mergeFilters(defaultFilters, newFilters) {
        const merged = { ...defaultFilters };

        Object.keys(newFilters).forEach(key => {
            if (newFilters[key] !== null && newFilters[key] !== undefined && newFilters[key] !== '') {
                merged[key] = newFilters[key];
            }
        });

        return merged;
    }

    function applyFiltersToUrl(filters) {
        const url = new URL(window.location.href);
        const searchParams = new URLSearchParams();

        Object.keys(filters).forEach(key => {
            const value = filters[key];
            if (value !== null && value !== undefined && value !== '' && key !== 'page') {
                searchParams.set(key, value);
            }
        });

        const currentPage = url.searchParams.get('page');
        if (currentPage && currentPage !== '1') {
            searchParams.set('page', currentPage);
        }

        const newUrl = `${url.pathname}${searchParams.toString() ? '?' + searchParams.toString() : ''}`;

        if (newUrl !== window.location.pathname + window.location.search) {
            window.location.href = newUrl;
        }
    }

    function setupEventListeners() {
        const searchForm = document.getElementById('search-form');
        if (searchForm) {
            searchForm.addEventListener('submit', function(e) {
                e.preventDefault();
                const query = this.querySelector('input[name="query"]').value.trim();
                console.log('Search form submitted with query:', query);
                updateFilter('query', query);
            });
        }

        const sortForm = document.getElementById('sort-form');
        if (sortForm) {
            const sortBySelect = sortForm.querySelector('select[name="sortBy"]');
            const sortDirectionButton = sortForm.querySelector('#sort-direction-btn');

            if (sortBySelect) {
                sortBySelect.addEventListener('change', function() {
                    console.log('Sort by changed to:', this.value);
                    updateFilter('sortBy', this.value);
                });
            }

            if (sortDirectionButton) {
                sortDirectionButton.addEventListener('click', function(e) {
                    e.preventDefault();
                    const currentDirection = getCurrentSortDirection();
                    const newDirection = currentDirection === 'desc' ? 'asc' : 'desc';
                    console.log('Sort direction clicked, changing from', currentDirection, 'to', newDirection);
                    updateFilter('sortDirection', newDirection);
                });
            }
        }

        const categoryLinks = document.querySelectorAll('.category-link');
        categoryLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault(); // Предотвращаем стандартный переход
                const href = this.getAttribute('href');
                const url = new URL(href, window.location.origin);
                const categoryId = url.searchParams.get('categoryId');

                console.log('Category link clicked, categoryId:', categoryId);

                if (categoryId) {
                    updateFilter('categoryId', parseInt(categoryId));
                } else {
                    updateFilter('categoryId', null);
                }
            });
        });

        const paginationLinks = document.querySelectorAll('.pagination-link');
        paginationLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                const href = this.getAttribute('href');
                console.log('Pagination link clicked:', href);
            });
        });
    }

    function getCurrentSortDirection() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('sortDirection') || 'desc';
    }

    function updateFilter(key, value) {
        const currentFilters = getCurrentUrlParams();
        const savedFilters = getSavedFilters() || {};

        const updatedFilters = mergeFilters(savedFilters, { [key]: value });

        if (key !== 'page') {
            updatedFilters.page = 1;
        }

        console.log('Updating filter:', key, '=', value);
        console.log('Updated filters:', updatedFilters);

        saveFilters(updatedFilters);
        applyFiltersToUrl(updatedFilters);
    }

    function clearAllFilters() {
        console.log('Clearing all filters');
        localStorage.removeItem(STORAGE_KEY);
        window.location.href = '/vacancies';
    }

    function getCurrentFilters() {
        return getSavedFilters();
    }

    window.VacancyFilters = {
        clearAll: clearAllFilters,
        getCurrent: getCurrentFilters,
        update: updateFilter,
        save: saveFilters
    };

    console.log('Vacancy filters system initialized');
});