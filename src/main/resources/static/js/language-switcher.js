document.addEventListener('DOMContentLoaded', function() {
    console.log('Language switcher initialized');

    initializeLanguageSwitcher();

    function initializeLanguageSwitcher() {
        setupDropdownToggle();
        setupOutsideClickHandler();
        setupLanguageOptions();
    }

    function setupDropdownToggle() {
        const languageButton = document.getElementById('language-menu-button');

        if (languageButton) {
            languageButton.addEventListener('click', function() {
                const dropdown = document.getElementById('language-dropdown');
                if (dropdown) {
                    dropdown.classList.toggle('hidden');
                    console.log('Language dropdown toggled');
                }
            });
        }
    }

    function setupOutsideClickHandler() {
        document.addEventListener('click', function(event) {
            const dropdown = document.getElementById('language-dropdown');
            const button = document.getElementById('language-menu-button');

            if (dropdown && button) {
                if (!dropdown.contains(event.target) && !button.contains(event.target)) {
                    dropdown.classList.add('hidden');
                }
            }
        });
    }

    function setupLanguageOptions() {
        const languageOptions = document.querySelectorAll('.language-option');

        languageOptions.forEach(option => {
            option.addEventListener('click', function(e) {
                e.preventDefault();

                const selectedLang = this.getAttribute('data-lang');
                console.log('Language option clicked:', selectedLang);

                changeLanguage(selectedLang);
            });
        });
    }

    function changeLanguage(language) {
        const currentUrl = new URL(window.location.href);
        const csrfToken = getCsrfToken();

        console.log('Changing language to:', language);
        console.log('CSRF Token:', csrfToken);

        fetch('/api/users/language?lang=' + language, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-XSRF-TOKEN': csrfToken || ''
            },
            credentials: 'same-origin'
        })
            .then(response => {
                console.log('Language update response status:', response.status);

                if (!response.ok) {
                    throw new Error('Failed to update language. Status: ' + response.status);
                }

                return response;
            })
            .catch(error => {
                console.error('Error updating language:', error);
            })
            .finally(() => {
                redirectWithLanguage(currentUrl, language);
            });
    }

    function getCsrfToken() {
        const csrfInput = document.querySelector('input[name="_csrf"]');
        const csrfMeta = document.querySelector('meta[name="_csrf"]');

        if (csrfInput) {
            return csrfInput.value;
        }

        if (csrfMeta) {
            return csrfMeta.getAttribute('content');
        }

        console.warn('CSRF token not found');
        return '';
    }

    function redirectWithLanguage(currentUrl, language) {
        currentUrl.searchParams.set('lang', language);

        const newUrl = currentUrl.toString();
        console.log('Redirecting to:', newUrl);

        window.location.href = newUrl;
    }

    window.LanguageSwitcher = {
        changeLanguage: changeLanguage,
        getCsrfToken: getCsrfToken
    };
});