document.addEventListener('DOMContentLoaded', async function() {
    const urlParams = new URLSearchParams(window.location.search);
    let currentPage = parseInt(urlParams.get('page')) || 1;
    const pageSize = 10;
    let totalPages = 1;

    const pathParts = window.location.pathname.split('/');
    const itemType = pathParts[2];
    const itemId = pathParts[3];
    const isEmployer = itemType === 'vacancy';

    let currentUserId;

    await init();

    async function init() {
        const userPromise = loadCurrentUser();

        if (isEmployer) {
            await Promise.all([
                loadVacancyDetails(itemId),
                loadVacancyResponses(itemId, currentPage, pageSize)
            ]);
        } else {
            await Promise.all([
                loadResumeDetails(itemId),
                loadResumeResponses(itemId, currentPage, pageSize)
            ]);
        }

        await userPromise;
    }

    async function loadCurrentUser() {
        try {
            const response = await fetch('/api/users/current');
            if (!response.ok) {
                throw new Error('Ошибка получения данных пользователя');
            }

            const user = await response.json();
            if (user && user.id) {
                currentUserId = user.id;
            }
        } catch (error) {
            console.error('Error fetching current user:', error);
        }
    }

    async function loadVacancyDetails(vacancyId) {
        try {
            const response = await fetch(`/api/vacancies/${vacancyId}`);
            if (!response.ok) {
                throw new Error(`Ошибка получения данных вакансии: ${response.status} ${response.statusText}`);
            }

            const vacancy = await response.json();

            const titleElement = document.getElementById('item-title');
            const subtitleElement = document.getElementById('item-subtitle');

            if (titleElement && subtitleElement) {
                titleElement.textContent = vacancy.name || 'Вакансия';
                subtitleElement.textContent = vacancy.employer?.name || '';
            }
        } catch (error) {
            console.error('Error loading vacancy details:', error);
            showError();
        }
    }

    async function loadResumeDetails(resumeId) {
        try {
            const response = await fetch(`/api/resumes/${resumeId}`);
            if (!response.ok) {
                throw new Error(`Ошибка получения данных резюме: ${response.status} ${response.statusText}`);
            }

            const resume = await response.json();

            const titleElement = document.getElementById('item-title');
            const subtitleElement = document.getElementById('item-subtitle');

            if (titleElement && subtitleElement) {
                titleElement.textContent = resume.name || 'Резюме';

                const firstName = resume.applicant?.name || '';
                const lastName = resume.applicant?.surname || '';
                subtitleElement.textContent = `${firstName} ${lastName}`.trim();
            }
        } catch (error) {
            console.error('Error loading resume details:', error);
            showError();
        }
    }

    async function loadVacancyResponses(vacancyId, page, size) {
        try {
            const response = await fetch(`/api/responses/vacancies/${vacancyId}?page=${page}&size=${size}`);

            if (!response.ok) {
                throw new Error(`Ошибка получения откликов на вакансию: ${response.status} ${response.statusText}`);
            }

            const data = await response.json();
            await renderResponses(data, isEmployer);
        } catch (error) {
            console.error('Подробная ошибка загрузки откликов на вакансию:', error);
            showError('Не удалось загрузить отклики на вакансию. Пожалуйста, проверьте наличие доступа или попробуйте позже.');
        }
    }

    async function loadResumeResponses(resumeId, page, size) {
        try {
            const response = await fetch(`/api/responses/resumes/${resumeId}?page=${page}&size=${size}`);

            if (!response.ok) {
                throw new Error(`Ошибка получения откликов на резюме: ${response.status} ${response.statusText}`);
            }

            const data = await response.json();
            await renderResponses(data, isEmployer);
        } catch (error) {
            console.error('Подробная ошибка загрузки откликов на резюме:', error);
            showError('Не удалось загрузить отклики на резюме. Пожалуйста, проверьте наличие доступа или попробуйте позже.');
        }
    }

    async function renderResponses(data, isEmployer) {
        const container = document.getElementById('responses-container');
        if (!container) return;

        container.innerHTML = '';

        if (data.content && data.content.length > 0) {
            totalPages = data.totalPages || 1;

            const responsePromises = data.content.map(async (response) => {
                if (!response || !response.id) {
                    console.warn('Пропуск некорректного отклика:', response);
                    return null;
                }

                const responseElement = document.createElement('div');
                responseElement.className = 'border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition duration-200';

                const title = getResponseTitle(response, isEmployer);
                const subtitle = getResponseSubtitle(response, isEmployer);
                const responseId = response.id;

                const status = response.isConfirmed
                    ? `<span class="text-green-500 text-sm"><i class="fas fa-check-circle mr-1"></i>${messages.statusConfirmed || 'Подтверждено'}</span>`
                    : `<span class="text-gray-500 text-sm"><i class="fas fa-clock mr-1"></i>${messages.statusPending || 'В ожидании'}</span>`;

                try {
                    const unreadResponse = await fetch(`/api/messages/unread/by-response?responseId=${responseId}&userId=${currentUserId || ''}`);
                    if (!unreadResponse.ok) {
                        throw new Error('Ошибка получения непрочитанных сообщений');
                    }

                    const count = await unreadResponse.json();
                    const unreadBadge = count > 0
                        ? `<span class="bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">${count}</span>`
                        : '';

                    responseElement.innerHTML = `
                        <a href="/chat/room/${responseId}" class="block">
                            <div class="flex justify-between items-center">
                                <div>
                                    <h3 class="font-medium text-lg text-gray-800">${escapeHtml(title)}</h3>
                                    <p class="text-gray-600">${escapeHtml(subtitle)}</p>
                                    <div class="mt-2">${status}</div>
                                </div>
                                <div class="flex items-center">
                                    ${unreadBadge}
                                    <span class="ml-2 text-blue-500"><i class="fas fa-comments"></i></span>
                                </div>
                            </div>
                        </a>
                    `;
                } catch (error) {
                    console.error('Error fetching unread count:', error);

                    responseElement.innerHTML = `
                        <a href="/chat/room/${responseId}" class="block">
                            <div class="flex justify-between items-center">
                                <div>
                                    <h3 class="font-medium text-lg text-gray-800">${escapeHtml(title)}</h3>
                                    <p class="text-gray-600">${escapeHtml(subtitle)}</p>
                                    <div class="mt-2">${status}</div>
                                </div>
                                <div>
                                    <span class="text-blue-500"><i class="fas fa-comments"></i></span>
                                </div>
                            </div>
                        </a>
                    `;
                }

                return responseElement;
            });

            const responseElements = await Promise.all(responsePromises);
            responseElements
                .filter(elem => elem !== null)
                .forEach(elem => container.appendChild(elem));

            renderPagination(currentPage, totalPages);
        } else {
            container.innerHTML = `
                <div class="text-center py-8">
                    <p class="text-gray-500">${messages.noResponses || 'Нет активных откликов'}</p>
                </div>
            `;
        }
    }

    function getResponseTitle(response, isEmployer) {
        if (isEmployer) {
            return response.resume?.position || 'Резюме';
        } else {
            return response.vacancy?.name || 'Вакансия';
        }
    }

    function getResponseSubtitle(response, isEmployer) {
        if (isEmployer) {
            const firstName = response.resume?.applicant?.name || '';
            const lastName = response.resume?.applicant?.surname || '';
            return `${firstName} ${lastName}`.trim();
        } else {
            return response.vacancy?.employer?.name || '';
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
            (currentPage <= 0 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100');
        prevButton.innerHTML = '<i class="fas fa-chevron-left"></i>';
        prevButton.disabled = currentPage <= 0;
        if (currentPage > 0) {
            prevButton.addEventListener('click', () => {
                navigateToPage(currentPage - 1);
            });
        }
        paginationElement.appendChild(prevButton);

        const startPage = Math.max(0, currentPage - 2);
        const endPage = Math.min(totalPages - 1, startPage + 4);

        for (let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement('button');
            pageButton.className = 'px-3 py-1 rounded border ' +
                (i === currentPage
                    ? 'bg-blue-500 text-white border-blue-500'
                    : 'border-gray-300 text-gray-700 hover:bg-gray-100');
            pageButton.textContent = i + 1;
            pageButton.addEventListener('click', () => {
                navigateToPage(i);
            });
            paginationElement.appendChild(pageButton);
        }

        const nextButton = document.createElement('button');
        nextButton.className = 'px-3 py-1 rounded border border-gray-300 ' +
            (currentPage >= totalPages - 1 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100');
        nextButton.innerHTML = '<i class="fas fa-chevron-right"></i>';
        nextButton.disabled = currentPage >= totalPages - 1;
        if (currentPage < totalPages - 1) {
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

    function showError(customMessage) {
        const container = document.getElementById('responses-container');
        if (!container) return;

        const errorMessage = customMessage || messages.errorLoading || 'Ошибка загрузки данных';
        const retryText = messages.retry || 'Повторить';

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