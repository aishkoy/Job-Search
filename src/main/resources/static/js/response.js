document.addEventListener('DOMContentLoaded', function () {
    const applyBtn = document.getElementById('vacancy-apply-btn');
    if (!applyBtn) return;

    const vacancyId = window.location.pathname.split('/').pop();
    const pageSize = 3;
    let currentUserId = null;
    let currentPage = 1;
    let totalPages = 0;
    let selectedResumeId = null;

    const modal = createModal();
    const elements = {
        modalContent: modal.querySelector('.modal-content'),
        closeBtn: document.getElementById('close-modal'),
        resumesContainer: document.getElementById('resumes-container'),
        noResumesMessage: document.getElementById('no-resumes-message'),
        submitBtn: document.getElementById('submit-apply'),
        paginationContainer: document.getElementById('pagination-container'),
        prevPageBtn: document.getElementById('prev-page'),
        nextPageBtn: document.getElementById('next-page'),
        pageInfo: document.getElementById('page-info')
    };

    applyBtn.addEventListener('click', openModal);
    elements.closeBtn.addEventListener('click', closeModal);
    elements.submitBtn.addEventListener('click', applyForVacancy);
    elements.prevPageBtn.addEventListener('click', () => navigatePage(-1));
    elements.nextPageBtn.addEventListener('click', () => navigatePage(1));
    modal.addEventListener('click', event => {
        if (event.target === modal) closeModal();
    });

    modal.addEventListener('transitionend', function (e) {
        if (e.target === modal && modal.classList.contains('opacity-0')) {
            modal.classList.add('hidden');
        }
    });

    function createModal() {
        const modal = document.createElement('div');
        modal.className = 'fixed inset-0 bg-black/40 backdrop-blur-sm overflow-y-auto h-full w-full hidden opacity-0 transition-opacity duration-300';
        modal.id = 'apply-modal';
        modal.innerHTML = `
            <div class="modal-content relative top-20 mx-auto p-5 w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white transform transition-transform duration-300 scale-95">
                <div class="flex justify-between items-center pb-3 border-b">
                    <h3 class="text-xl font-semibold text-gray-800">${getMessage('resume.choose', 'Выберите резюме для отклика')}</h3>
                    <button id="close-modal" class="text-gray-400 hover:text-gray-500">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                
                <div id="resumes-container" class="mt-4 space-y-4">
                    <div class="flex justify-center">
                        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                    </div>
                </div>
                
                <div id="pagination-container" class="mt-4 flex justify-center items-center space-x-2 hidden">
                    <button id="prev-page" class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <span id="page-info" class="text-sm text-gray-600">Страница 1 из 1</span>
                    <button id="next-page" class="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
                
                <div class="mt-4 flex justify-center items-center">
                    <p id="no-resumes-message" class="text-red-500 hidden">
                        ${getMessage('resume.empty', 'У вас нет резюме. ')}${getMessage('resume.create', 'Создайте резюме, чтобы откликнуться на вакансию.')}
                    </p>
                </div>
                
                <div class="mt-4 flex justify-center pt-3 border-t">
                    <button id="submit-apply" class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition disabled:opacity-50 disabled:cursor-not-allowed" disabled>
                        ${getMessage('apply', 'Откликнуться')}
                    </button>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        return modal;
    }

    async function openModal() {
        modal.classList.remove('hidden');

        modal.offsetWidth;
        modal.classList.add('opacity-100');
        modal.classList.remove('opacity-0');

        elements.modalContent.classList.add('scale-100');
        elements.modalContent.classList.remove('scale-95');

        try {
            currentUserId = await fetchCurrentUser();
            await fetchResumes(currentUserId, currentPage, pageSize);
        } catch (error) {
            handleError(error, getMessage('user.error', 'Не удалось загрузить данные пользователя.'));
        }
    }

    function closeModal() {
        modal.classList.remove('opacity-100');
        modal.classList.add('opacity-0');

        elements.modalContent.classList.remove('scale-100');
        elements.modalContent.classList.add('scale-95');

        resetModal();
    }

    function resetModal() {
        elements.resumesContainer.innerHTML = getLoaderHTML();
        elements.noResumesMessage.classList.add('hidden');
        elements.submitBtn.disabled = true;
        elements.paginationContainer.classList.add('hidden');
        selectedResumeId = null;
    }

    function getLoaderHTML() {
        return `
            <div class="flex justify-center">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
            </div>
        `;
    }

    function getCsrfToken() {
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
        return {token, header};
    }

    async function fetchCurrentUser() {
        try {
            const response = await fetch('/api/users/current');
            if (!response.ok) throw new Error(getMessage('user.error', 'Не удалось получить данные пользователя'));
            const user = await response.json();
            return user.id;
        } catch (error) {
            console.error('Ошибка при получении данных пользователя:', error);
            throw error;
        }
    }

    async function fetchResumes(userId, page, size) {
        elements.resumesContainer.innerHTML = getLoaderHTML();

        try {
            const response = await fetch(`/api/resumes/applicants/${userId}?page=${page}&size=${size}`);

            if (!response.ok) {
                if (response.status === 404) {
                    showNoResumesMessage();
                    return;
                }
                throw new Error(getMessage('resume.error', 'Не удалось загрузить резюме'));
            }

            const data = await response.json();

            if (data.content && data.content.length > 0) {
                displayResumes(data.content);
                totalPages = data.totalPages;
                updatePagination();
            } else {
                showNoResumesMessage();
            }
        } catch (error) {
            console.error('Ошибка при загрузке резюме:', error);
            showErrorMessage(getMessage('resume.error', 'Не удалось загрузить резюме. Пожалуйста, попробуйте еще раз позже.'));
        }
    }

    function displayResumes(resumes) {
        elements.resumesContainer.innerHTML = '';

        resumes.forEach(resume => {
            const card = createResumeCard(resume);
            elements.resumesContainer.appendChild(card);
        });

        elements.paginationContainer.classList.remove('hidden');
    }

    function createResumeCard(resume) {
        const card = document.createElement('div');
        card.className = 'p-4 border border-gray-200 rounded-lg hover:border-blue-400 cursor-pointer transition';
        card.dataset.resumeId = resume.id;

        let experienceHTML = `<p class="text-gray-500">${getMessage('work-exp.empty', 'Нет опыта работы')}</p>`;
        if (resume.workExperiences && resume.workExperiences.length > 0) {
            const exp = resume.workExperiences[0];
            experienceHTML = `
                <div class="mt-2">
                    <p class="text-sm text-gray-600">${exp.companyName} • ${exp.years}</p>
                </div>
            `;
        }

        const categoryName = resume.category.name;

        card.innerHTML = `
            <div class="flex items-start">
                <div class="flex-1">
                    <div class="flex items-center">
                        <input type="radio" name="resume" id="resume-${resume.id}" value="${resume.id}" class="mr-3">
                        <div>
                            <h4 class="font-semibold text-gray-800">${resume.name}</h4>
                            <p class="text-sm text-gray-600">${categoryName}</p>
                        </div>
                    </div>
                    ${experienceHTML}
                </div>
            </div>
        `;

        card.addEventListener('click', () => selectResume(card, resume.id));
        return card;
    }

    function selectResume(card, resumeId) {
        document.querySelectorAll('input[name="resume"]').forEach(input => {
            input.checked = false;
        });

        card.querySelector('input[type="radio"]').checked = true;

        document.querySelectorAll('#resumes-container > div').forEach(div => {
            div.classList.remove('border-blue-500', 'bg-blue-50');
        });

        card.classList.add('border-blue-500', 'bg-blue-50');

        selectedResumeId = resumeId;
        elements.submitBtn.disabled = false;
    }

    function updatePagination() {
        elements.pageInfo.textContent = `${getMessage('page', 'Страница')} ${currentPage} ${getMessage('of', 'из')} ${totalPages}`;
        elements.prevPageBtn.disabled = currentPage <= 1;
        elements.nextPageBtn.disabled = currentPage >= totalPages;
    }

    async function navigatePage(direction) {
        const newPage = currentPage + direction;
        if (newPage >= 1 && newPage <= totalPages) {
            currentPage = newPage;
            await fetchResumes(currentUserId, currentPage, pageSize);
        }
    }

    function showNoResumesMessage() {
        elements.resumesContainer.innerHTML = `
            <div class="text-center p-6">
                <div class="text-amber-500 text-3xl mb-3">
                    <i class="fas fa-file-alt"></i>
                </div>
                <p class="text-gray-700 font-medium mb-2">${getMessage('resume.empty', 'У вас нет резюме')}</p>
                <p class="text-gray-500 text-sm mb-4">${getMessage('resume.create', 'Создайте резюме, чтобы откликнуться на вакансию')}</p>
                <a href="/resumes/create" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md transition">
                    <i class="fas fa-plus mr-2"></i>${getMessage('resume.create.submit', 'Создать резюме')}
                </a>
            </div>
        `;
        elements.paginationContainer.classList.add('hidden');
    }

    function showErrorMessage(message) {
        elements.resumesContainer.innerHTML = `
            <div class="text-center p-4">
                <div class="text-red-500 text-xl mb-2">
                    <i class="fas fa-exclamation-circle"></i>
                </div>
                <p class="text-red-600 font-medium">${message}</p>
            </div>
        `;
    }

    function handleError(error, defaultMessage) {
        let errorMessage = defaultMessage;

        try {
            const errorData = JSON.parse(error.message);
            if (errorData.response && errorData.response.errors && errorData.response.errors.length) {
                errorMessage = errorData.response.errors[0];
            } else if (errorData.title) {
                errorMessage = errorData.title;
            }
        } catch (e) {
            errorMessage = error.message;
        }

        console.error('Ошибка:', errorMessage);
        showErrorMessage(errorMessage);
    }

    async function applyForVacancy() {
        if (!selectedResumeId) return;

        elements.submitBtn.disabled = true;
        elements.submitBtn.innerHTML = `
            <div class="flex items-center">
                <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                <span>${getMessage('sending', 'Отправка...')}</span>
            </div>
        `;

        try {
            const {token, header} = getCsrfToken();
            const headers = {'Content-Type': 'application/json'};
            if (token && header) headers[header] = token;

            const response = await fetch(`/api/responses/${vacancyId}?resumeId=${selectedResumeId}`, {
                method: 'POST',
                headers,
                credentials: 'include'
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            showSuccessMessage();
            setTimeout(closeModal, 2000);
        } catch (error) {
            elements.submitBtn.disabled = false;
            elements.submitBtn.innerHTML = getMessage('apply', 'Откликнуться');

            handleError(error, getMessage('fail', 'Не удалось отправить отклик'));
        }
    }

    function showSuccessMessage() {
        elements.resumesContainer.innerHTML = `
            <div class="text-center p-4">
                <div class="text-green-500 text-xl mb-2">
                    <i class="fas fa-check-circle"></i>
                </div>
                <p class="text-green-600 font-medium">${getMessage('success', 'Ваше резюме успешно отправлено!')}</p>
            </div>
        `;

        elements.paginationContainer.classList.add('hidden');
        elements.submitBtn.innerHTML = getMessage('ready', 'Готово');
        elements.submitBtn.classList.remove('bg-green-500', 'hover:bg-green-600');
        elements.submitBtn.classList.add('bg-gray-400');
    }
});

function getMessage(key, fallback) {
    const el = document.querySelector(`#message-storage [data-key="${key}"]`);
    return el?.textContent?.trim() || fallback;
}