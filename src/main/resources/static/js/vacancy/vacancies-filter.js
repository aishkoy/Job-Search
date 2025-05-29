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
                console.error('Error clearing guest filters:', clearError);
            }
        }
    }

    function initializeFilters() {
        const savedFilters = getSavedFilters();

        if (!savedFilters || Object.keys(savedFilters).length === 0) {
            saveFilters(DEFAULT_FILTERS);
        }
    }

    function getFiltersFromStorage(userId) {
        try {
            const saved = localStorage.getItem(`vacancy_filters_${userId}`);
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
            delete filtersToSave.page;

            localStorage.setItem(`vacancy_filters_${userId}`, JSON.stringify(filtersToSave));
        } catch (error) {
            console.error(`Error saving filters for user ${userId} to localStorage:`, error);
        }
    }

    function saveFilters(filters) {
        if (!currentUserId || !isInitialized) {
            return;
        }
        saveFiltersToStorage(currentUserId, filters);
    }

    function clearFiltersFromStorage(userId) {
        try {
            localStorage.removeItem(`vacancy_filters_${userId}`);
        } catch (error) {
            console.error(`Error clearing filters for user ${userId}:`, error);
        }
    }

    function updateFilter(key, value) {
        if (!isInitialized) {
            return;
        }

        const currentFilters = getSavedFilters() || { ...DEFAULT_FILTERS };

        if (value === null || value === '' || value === undefined) {
            if (key === 'categoryId') {
                currentFilters[key] = null;
            } else if (DEFAULT_FILTERS.hasOwnProperty(key)) {
                currentFilters[key] = DEFAULT_FILTERS[key];
            } else {
                delete currentFilters[key];
            }
        } else {
            currentFilters[key] = value;
        }

        saveFilters(currentFilters);
    }

    function clearAllFilters() {
        if (currentUserId && isInitialized) {
            clearFiltersFromStorage(currentUserId);
            saveFilters(DEFAULT_FILTERS);
        }
    }

    function getCurrentFilters() {
        const saved = getSavedFilters();
        return saved ? { ...DEFAULT_FILTERS, ...saved } : { ...DEFAULT_FILTERS };
    }

    function isReady() {
        return isInitialized;
    }

    function debugGetAllFilters() {
        const allFilters = {};
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);
            if (key && key.startsWith('vacancy_filters_')) {
                try {
                    const value = localStorage.getItem(key);
                    const parsed = JSON.parse(value);
                    if (parsed && typeof parsed === 'object') {
                        allFilters[key] = parsed;
                    }
                } catch (e) {
                    console.error(`Error parsing filters for key ${key}:`, e);
                }
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
        isReady: isReady,
        debug: {
            getAllFilters: debugGetAllFilters,
            getCurrentUserId: debugGetCurrentUserId,
            migrateGuestFilters: migrateGuestFiltersIfNeeded
        }
    };
});