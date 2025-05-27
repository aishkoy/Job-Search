document.addEventListener('DOMContentLoaded', function() {
    const GUEST_USER_KEY = 'guest';
    const DEFAULT_FILTERS = {
        categoryId: null,
        sortBy: 'updatedAt',
        sortDirection: 'desc',
        size: 5,
        query: ''
    };

    let currentUserId = null;
    let isInitialized = false;

    initializeWithUser();

    async function initializeWithUser() {
        try {
            currentUserId = await getCurrentUser();
            console.log('Current user ID:', currentUserId);

            if (currentUserId !== GUEST_USER_KEY) {
                migrateGuestFiltersIfNeeded();
            }

            initializeFilters();
            isInitialized = true;

        } catch (error) {
            currentUserId = GUEST_USER_KEY;
            initializeFilters();
            isInitialized = true;
        }
    }

    async function getCurrentUser() {
        try {
            const response = await fetch('/api/users/current', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin'
            });

            if (response.ok) {
                const userData = await response.json();
                console.log('User data received:', userData);
                return userData && userData.id ? userData.id.toString() : GUEST_USER_KEY;
            } else {
                return GUEST_USER_KEY;
            }
        } catch (error) {
            console.error('Error fetching current user:', error);
            return GUEST_USER_KEY;
        }
    }

    function migrateGuestFiltersIfNeeded() {
        try {
            const guestFilters = getFiltersFromStorage(GUEST_USER_KEY);
            const migrationKey = `migration_done_${currentUserId}`;

            const migrationDone = localStorage.getItem(migrationKey);

            if (guestFilters && Object.keys(guestFilters).length > 0 && !migrationDone) {
                const userFilters = getFiltersFromStorage(currentUserId);

                if (!userFilters || Object.keys(userFilters).length === 0) {
                    saveFiltersToStorage(currentUserId, guestFilters);
                }

                localStorage.setItem(migrationKey, 'true');
            }
            if (guestFilters) {
                clearFiltersFromStorage(GUEST_USER_KEY);
            }

        } catch (error) {
            try {
                clearFiltersFromStorage(GUEST_USER_KEY);
            } catch (clearError) {
            }
        }
    }

    function initializeFilters() {
        const urlParams = getCurrentUrlParams();
        const savedFilters = getSavedFilters();

        if (hasUrlParams(urlParams)) {
            const currentFilters = mergeFilters(DEFAULT_FILTERS, urlParams);
            saveFilters(currentFilters);
        }
        else if (savedFilters && Object.keys(savedFilters).length > 0) {
            applyFiltersToUrl(savedFilters);
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

    function getFiltersFromStorage(userId) {
        try {
            const saved = localStorage.getItem(userId);
            if (saved === null || saved === 'null') {
                return null;
            }
            return JSON.parse(saved);
        } catch (error) {
            console.error(`Error reading filters for user ${userId} from localStorage:`, error);
            return null;
        }
    }

    function getSavedFilters() {
        if (!currentUserId) return null;
        return getFiltersFromStorage(currentUserId);
    }

    function saveFiltersToStorage(userId, filters) {
        try {
            const filtersToSave = { ...filters };
            localStorage.setItem(userId, JSON.stringify(filtersToSave));
        } catch (error) {
            console.error(`Error saving filters for user ${userId} to localStorage:`, error);
        }
    }

    function saveFilters(filters) {
        if (!currentUserId) {
            return;
        }
        saveFiltersToStorage(currentUserId, filters);
    }

    function clearFiltersFromStorage(userId) {
        try {
            localStorage.removeItem(userId);
        } catch (error) {
            console.error(`Error clearing filters for user ${userId}:`, error);
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
                if (key === 'sortBy' && value === 'updatedAt') return;
                if (key === 'sortDirection' && value === 'desc') return;
                if (key === 'size' && value === 5) return;

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
                updateFilter('query', query);
            });
        }

        const sortForm = document.getElementById('sort-form');
        if (sortForm) {
            const sortBySelect = sortForm.querySelector('select[name="sortBy"]');
            const sortDirectionButton = sortForm.querySelector('#sort-direction-btn');

            if (sortBySelect) {
                sortBySelect.addEventListener('change', function() {
                    updateFilter('sortBy', this.value);
                });
            }

            if (sortDirectionButton) {
                sortDirectionButton.addEventListener('click', function(e) {
                    e.preventDefault();
                    const currentDirection = getCurrentSortDirection();
                    const newDirection = currentDirection === 'desc' ? 'asc' : 'desc';
                    updateFilter('sortDirection', newDirection);
                });
            }
        }

        const categoryLinks = document.querySelectorAll('.category-link');
        categoryLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const href = this.getAttribute('href');
                const url = new URL(href, window.location.origin);
                const categoryIdParam = url.searchParams.get('categoryId');

                if (!categoryIdParam) {
                    clearCategoryFilter();
                } else {
                    updateFilter('categoryId', parseInt(categoryIdParam));
                }
            });
        });
    }

    function getCurrentSortDirection() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('sortDirection') || 'desc';
    }

    function updateFilter(key, value) {
        if (!isInitialized) {
            return;
        }

        const currentFilters = getCurrentUrlParams();
        const savedFilters = getSavedFilters() || {};

        const updatedFilters = mergeFilters(savedFilters, currentFilters);
        updatedFilters[key] = value;

        if (key !== 'page') {
            updatedFilters.page = 1;
        }

        saveFilters(updatedFilters);
        applyFiltersToUrl(updatedFilters);
    }

    function clearCategoryFilter() {
        if (!isInitialized) {
            return;
        }

        const currentFilters = getCurrentUrlParams();
        const savedFilters = getSavedFilters() || {};

        const updatedFilters = mergeFilters(savedFilters, currentFilters);
        updatedFilters.categoryId = null;
        updatedFilters.page = 1;

        saveFilters(updatedFilters);
        applyFiltersToUrl(updatedFilters);
    }

    function clearAllFilters() {
        if (currentUserId) {
            clearFiltersFromStorage(currentUserId);
        }
        window.location.href = '/vacancies';
    }

    function getCurrentFilters() {
        return getSavedFilters();
    }

    function debugGetAllFilters() {
        const allFilters = {};
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);
            try {
                const value = localStorage.getItem(key);
                const parsed = JSON.parse(value);
                if (parsed && typeof parsed === 'object' && (parsed.sortBy || parsed.categoryId !== undefined)) {
                    allFilters[key] = parsed;
                }
            } catch (e) {
            }
        }
        return allFilters;
    }

    function debugGetCurrentUserId() {
        return currentUserId;
    }

    window.VacancyFilters = {
        clearAll: clearAllFilters,
        getCurrent: getCurrentFilters,
        update: updateFilter,
        save: saveFilters,
        debug: {
            getAllFilters: debugGetAllFilters,
            getCurrentUserId: debugGetCurrentUserId,
            migrateGuestFilters: migrateGuestFiltersIfNeeded
        }
    };
});