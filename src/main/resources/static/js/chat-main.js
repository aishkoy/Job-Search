document.addEventListener('DOMContentLoaded', async function () {
    const urlParams = new URLSearchParams(window.location.search);
    let currentPage = parseInt(urlParams.get('page')) || 1;
    const pageSize = 5;
    let totalPages = 1;

    await fetchCurrentUser();

    async function fetchCurrentUser() {
        try {
            const response = await fetch('/api/users/current');

            const user = await response.json();
            if (user && user.id) {
                if (window.isEmployerRole) {
                    await loadVacancies(user.id, currentPage, pageSize);
                } else {
                    await loadResumes(user.id, currentPage, pageSize);
                }
            } else {
                console.error('Данные пользователя неполные:', user);
                showError('Не удалось получить данные пользователя');
            }
        } catch (error) {
            console.error('Ошибка получения пользователя:', error);
            showError('Не удалось загрузить данные пользователя');
        }
    }

    async function loadVacancies(userId, page, size) {
        try {
            const response = await fetch(`/api/vacancies/employers/${userId}?page=${page}&size=${size}`);

            if (!response.ok) {
                if (response.status === 404) {
                    showError('У вас нет вакансий!');
                    return;
                }
            }

            const data = await response.json();

            if (data && !data.userId) {
                data.userId = userId;
            }

            renderItems(data, true);
        } catch (error) {
            console.error('Ошибка загрузки вакансий:', error);
            showError('Не удалось загрузить вакансии');
        }
    }

    async function loadResumes(userId, page, size) {
        try {
            const response = await fetch(`/api/resumes/applicants/${userId}?page=${page}&size=${size}`);

            if (!response.ok) {
                if (response.status === 404) {
                    showError('У вас нет резюме!');
                    return;
                }
            }

            const data = await response.json();

            if (data && !data.userId) {
                data.userId = userId;
            }

            renderItems(data, false);
        } catch (error) {
            console.error('Ошибка загрузки резюме:', error);
            showError('Не удалось загрузить резюме');
        }
    }

    function renderItems(data, isEmployer) {
        const container = document.getElementById('chat-items-container');
        if (!container) return;

        container.innerHTML = '';

        if (!data || !data.content) {
            console.error('Неверный формат данных:', data);
            showError('Получены некорректные данные');
            return;
        }

        if (data.content && data.content.length > 0) {
            totalPages = data.totalPages || 1;

            data.content.forEach((item) => {
                if (!item || !item.id) {
                    console.error('Некорректный элемент:', item);
                    return;
                }

                const itemElement = document.createElement('div');
                itemElement.className = 'border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition duration-200';

                const title = getItemTitle(item, isEmployer);
                const subtitle = getItemSubtitle(item, isEmployer);
                const itemId = item.id;

                const roomType = isEmployer ? 'vacancy' : 'resume';
                itemElement.innerHTML = `
                    <a href="/chat/${roomType}/${itemId}" class="block">
                        <div class="flex justify-between items-center">
                            <div>
                                <h3 class="font-medium text-lg text-gray-800">${escapeHtml(title)}</h3>
                                <p class="text-gray-600">${escapeHtml(subtitle)}</p>
                            </div>
                            <div>
                                <span class="text-blue-500"><i class="fas fa-chevron-right"></i></span>
                            </div>
                        </div>
                    </a>
                `;

                container.appendChild(itemElement);
            });

            renderPagination(currentPage, totalPages);
        } else {
            const message = isEmployer
                ? (window.noVacanciesMessage || 'У вас нет вакансий')
                : (window.noResumesMessage || 'У вас нет резюме');
            container.innerHTML = `
                <div class="text-center py-8">
                    <p class="text-gray-500">${message}</p>
                </div>
            `;
        }
    }

    function getItemTitle(item, isEmployer) {
        if (isEmployer) {
            return item.name || 'Без названия';
        } else {
            return item.name || 'Без названия';
        }
    }

    function getItemSubtitle(item, isEmployer) {
        if (isEmployer) {
            if (item.employer && typeof item.employer === 'object') {
                return item.employer.name || '';
            }
            return '';
        } else {
            if (item.applicant && typeof item.applicant === 'object') {
                const firstName = item.applicant.name || '';
                const lastName = item.applicant.surname || '';
                return `${firstName} ${lastName}`.trim();
            }
            return '';
        }
    }

    function renderPagination(currentPage, totalPages) {
        const container = document.getElementById('pagination-container');
        if (!container) return;

        container.innerHTML = '';

        if (totalPages <= 1) return;

        const paginationElement = document.createElement('div');
        paginationElement.className = 'flex space-x-2 items-center';

        const prevButton = document.createElement('button');
        prevButton.className = 'px-3 py-1 rounded border border-gray-300 ' +
            (currentPage <= 1 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100');
        prevButton.innerHTML = '<i class="fas fa-chevron-left"></i>';
        prevButton.disabled = currentPage <= 1;
        if (currentPage > 1) {
            prevButton.addEventListener('click', () => {
                navigateToPage(currentPage - 1);
            });
        }
        paginationElement.appendChild(prevButton);

        const startPage = Math.max(1, currentPage - 2);
        const endPage = Math.min(totalPages, startPage + 4);

        for (let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement('button');
            pageButton.className = 'px-3 py-1 rounded border ' +
                (i === currentPage
                    ? 'bg-blue-500 text-white border-blue-500'
                    : 'border-gray-300 text-gray-700 hover:bg-gray-100');
            pageButton.textContent = i;
            pageButton.addEventListener('click', () => {
                navigateToPage(i);
            });
            paginationElement.appendChild(pageButton);
        }

        const nextButton = document.createElement('button');
        nextButton.className = 'px-3 py-1 rounded border border-gray-300 ' +
            (currentPage >= totalPages ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100');
        nextButton.innerHTML = '<i class="fas fa-chevron-right"></i>';
        nextButton.disabled = currentPage >= totalPages;
        if (currentPage < totalPages) {
            nextButton.addEventListener('click', () => {
                navigateToPage(currentPage + 1);
            });
        }
        paginationElement.appendChild(nextButton);

        container.appendChild(paginationElement);
    }

    function navigateToPage(page) {
        const url = new URL(window.location.href);
        url.searchParams.set('page', page);
        window.location.href = url.toString();
    }

    function showError(message) {
        const container = document.getElementById('chat-items-container');
        if (!container) return;

        const errorMessage = message || window.errorLoadingMessage || 'Ошибка загрузки данных';
        const retryText = window.retryMessage || 'Повторить';

        container.innerHTML = `
            <div class="text-center py-8">
                <p class="text-red-500">${errorMessage}</p>
                <button class="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600" onclick="window.location.reload()">
                    ${retryText}
                </button>
            </div>
        `;
    }

    function escapeHtml(text) {
        if (!text) return '';

        return text
            .toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});