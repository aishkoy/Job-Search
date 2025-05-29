document.addEventListener('DOMContentLoaded', function() {
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
            console.log('Current user ID for resumes:', currentUserId);

            initializeFilters();
            isInitialized = true;

        } catch (error) {
            console.error('Error initializing resume filters:', error);
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
                return userData && userData.id ? userData.id.toString() : null;
            }
        } catch (error) {
            console.error('Error fetching current user for resumes:', error);
            throw error;
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
            const saved = localStorage.getItem(`resume_filters_${userId}`);
            if (saved === null || saved === 'null') {
                return null;
            }
            return JSON.parse(saved);
        } catch (error) {
            console.error(`Error reading resume filters for user ${userId} from localStorage:`, error);
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

            localStorage.setItem(`resume_filters_${userId}`, JSON.stringify(filtersToSave));
        } catch (error) {
            console.error(`Error saving resume filters for user ${userId} to localStorage:`, error);
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
            localStorage.removeItem(`resume_filters_${userId}`);
        } catch (error) {
            console.error(`Error clearing resume filters for user ${userId}:`, error);
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
            if (key && key.startsWith('resume_filters_')) {
                try {
                    const value = localStorage.getItem(key);
                    const parsed = JSON.parse(value);
                    if (parsed && typeof parsed === 'object') {
                        allFilters[key] = parsed;
                    }
                } catch (e) {
                    console.error(`Error parsing resume filters for key ${key}:`, e);
                }
            }
        }
        return allFilters;
    }

    function debugGetCurrentUserId() {
        return currentUserId;
    }

    window.ResumeFilters = {
        clearAll: clearAllFilters,
        getCurrent: getCurrentFilters,
        update: updateFilter,
        save: saveFilters,
        isReady: isReady,
        debug: {
            getAllFilters: debugGetAllFilters,
            getCurrentUserId: debugGetCurrentUserId
        }
    };
});