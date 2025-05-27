document.addEventListener('DOMContentLoaded', function() {
    fetchCurrentUser()
        .then(user => {
            if (!user) return;

            const isApplicant = user.role.name === 'APPLICANT';
            const userId = user.id;

            if (isApplicant) {
                updateApplicantStats(userId);
            } else {
                updateEmployerStats(userId);
            }

            setupModalEventListeners(user);
        })
        .catch(error => {
            console.error('Ошибка при загрузке информации о пользователе:', error);
        });
});

async function fetchCurrentUser() {
    try {
        const response = await fetch('/api/users/current');
        return await response.json();
    } catch (error) {
        console.error('Ошибка при получении текущего пользователя:', error);
        return null;
    }
}

async function updateApplicantStats(applicantId) {
    try {
        const allResponses = await fetch(`/api/responses/applicants/${applicantId}/count`);
        const responseCount = await allResponses.json();

        const confirmedResponse = await fetch(`/api/responses/applicants/${applicantId}/count?isConfirmed=true`);
        const confirmedCount = await confirmedResponse.json();

        updateStatElements(responseCount, confirmedCount);
    } catch (error) {
        console.error('Ошибка при обновлении статистики соискателя:', error);
    }
}

async function updateEmployerStats(employerId) {
    try {
        const allResponses = await fetch(`/api/responses/employers/${employerId}/count`);
        const responsesCount = await allResponses.json();

        const confirmedResponse = await fetch(`/api/responses/employers/${employerId}/count?isConfirmed=true`);
        const confirmedCount = await confirmedResponse.json();

        updateStatElements(responsesCount, confirmedCount);
    } catch (error) {
        console.error('Ошибка при обновлении статистики работодателя:', error);
    }
}

function updateStatElements(responsesCount, confirmedCount) {
    const responsesElement = document.getElementById('responses');
    if (responsesElement) {
        responsesElement.textContent = responsesCount;
    }

    const responsesApprovedElement = document.getElementById('responses-approved');
    if (responsesApprovedElement) {
        responsesApprovedElement.textContent = confirmedCount;
    }
}

function setupModalEventListeners(currentUser) {
    createModal();

    const responsesBlock = document.getElementById('responses-block');
    const responsesApprovedBlock = document.getElementById('responses-approved-block');

    if (responsesBlock) {
        responsesBlock.addEventListener('click', () => {
            openResponsesModal(currentUser, false);
        });
    }

    if (responsesApprovedBlock) {
        responsesApprovedBlock.addEventListener('click', () => {
            openResponsesModal(currentUser, true);
        });
    }
}

function createModal() {
    const modalHTML = `
        <div id="responsesModal" class="fixed inset-0 bg-black/40 backdrop-blur-sm overflow-y-auto h-full w-full hidden opacity-0 transition-all duration-300 ease-out z-50">
            <div id="modalContent" class="relative top-20 mx-auto p-5 w-11/12 max-w-4xl shadow-lg rounded-md bg-white transform transition-all duration-300 ease-out scale-95 translate-y-4 opacity-0">
                <div class="flex justify-between items-center mb-4">
                    <h3 id="modalTitle" class="text-lg font-medium text-gray-900"></h3>
                    <button id="closeModal" class="text-gray-400 hover:text-gray-600 transition-colors duration-200">
                        <i class="fas fa-times text-xl"></i>
                    </button>
                </div>
                
                <div id="modalBody" class="max-h-96 overflow-y-auto">
                    <div class="flex justify-center items-center py-8">
                        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
                    </div>
                </div>
                
                <div id="modalPagination" class="mt-4 flex justify-center"></div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHTML);

    const modal = document.getElementById('responsesModal');
    const closeButton = document.getElementById('closeModal');
    const modalContent = document.getElementById('modalContent');

    closeButton.addEventListener('click', closeModal);
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !modal.classList.contains('hidden')) {
            closeModal();
        }
    });

    modal.addEventListener('transitionend', function(e) {
        if (e.target === modal && modal.classList.contains('opacity-0')) {
            modal.classList.add('hidden');
        }
    });
}

async function openResponsesModal(currentUser, isConfirmed = false) {
    const modal = document.getElementById('responsesModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalBody = document.getElementById('modalBody');
    const modalContent = document.getElementById('modalContent');

    modal.classList.remove('hidden');

    modal.classList.add('opacity-0');
    modalContent.classList.add('scale-95', 'translate-y-4', 'opacity-0');

    modalTitle.textContent = isConfirmed ? 'Подтвержденные отклики' : 'Все отклики';

    modalBody.innerHTML = `
        <div class="flex justify-center items-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
        </div>
    `;

    await new Promise(resolve => setTimeout(resolve, 10));

    requestAnimationFrame(() => {
        modal.classList.remove('opacity-0');
        modalContent.classList.remove('scale-95', 'translate-y-4', 'opacity-0');
        modalContent.classList.add('scale-100', 'translate-y-0', 'opacity-100');
    });

    try {
        await loadResponses(currentUser, isConfirmed, 1);
    } catch (error) {
        console.error('Ошибка при загрузке откликов:', error);
        modalBody.innerHTML = `
            <div class="text-center py-8">
                <p class="text-red-500">Ошибка при загрузке откликов</p>
                <button onclick="openResponsesModal(${JSON.stringify(currentUser)}, ${isConfirmed})" 
                        class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors duration-200">
                    Повторить
                </button>
            </div>
        `;
    }
}

async function loadResponses(currentUser, isConfirmed, page = 1, size = 5) {
    const isApplicant = currentUser.role.name === 'APPLICANT';
    const userId = currentUser.id;

    let apiUrl;
    if (isApplicant) {
        apiUrl = `/api/responses/applicants/${userId}?page=${page}&size=${size}&isConfirmed=${isConfirmed}`;
    } else {
        apiUrl = `/api/responses/employers/${userId}?page=${page}&size=${size}&isConfirmed=${isConfirmed}`;
    }

    try {
        const response = await fetch(apiUrl);
        const data = await response.json();
        displayResponses(data, currentUser, isConfirmed);
    } catch (error) {
        throw error;
    }
}

function displayResponses(data, currentUser, isConfirmed) {
    const modalBody = document.getElementById('modalBody');
    const modalPagination = document.getElementById('modalPagination');

    if (!data.content || data.content.length === 0) {
        modalBody.innerHTML = `
            <div class="text-center py-8">
                <div class="w-16 h-16 bg-gray-100 text-gray-400 rounded-full flex items-center justify-center mx-auto mb-4">
                    <i class="fas fa-inbox text-2xl"></i>
                </div>
                <p class="text-gray-500">Откликов не найдено</p>
            </div>
        `;
        modalPagination.innerHTML = '';
        return;
    }

    const isApplicant = currentUser.role.name === 'APPLICANT';
    const isEmployer = !isApplicant;

    let responsesHTML = '<div class="space-y-4">';

    data.content.forEach(response => {
        const statusBadge = response.isConfirmed
            ? '<span class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs transition-colors duration-200"><i class="fas fa-check mr-1"></i>Подтверждено</span>'
            : '<span class="px-2 py-1 bg-gray-100 text-gray-800 rounded-full text-xs transition-colors duration-200"><i class="fas fa-clock mr-1"></i>В ожидании</span>';

        responsesHTML += `
            <div class="border rounded-lg p-4 hover:bg-gray-50 transition-all duration-200 hover:shadow-md">
                <div class="flex justify-between items-start mb-3">
                    <div class="flex-1">
        `;

        if (isApplicant) {
            responsesHTML += `
                        <h4 class="font-semibold text-gray-900">${response.vacancy?.name || 'Название не указано'}</h4>
                        <p class="text-gray-600 text-sm">${response.vacancy?.employer?.name || 'Работодатель не указан'}</p>
            `;
            if (response.vacancy?.salary) {
                responsesHTML += `<p class="text-blue-600 text-sm font-medium">${response.vacancy.salary.toLocaleString()} ₽</p>`;
            }
        } else {
            responsesHTML += `
                        <h4 class="font-semibold text-gray-900">${response.resume?.position || 'Позиция не указана'}</h4>
                        <p class="text-gray-600 text-sm">${response.resume?.applicant?.name || ''} ${response.resume?.applicant?.surname || ''}</p>
            `;
            if (response.resume?.salary) {
                responsesHTML += `<p class="text-blue-600 text-sm font-medium">${response.resume.salary.toLocaleString()} ₽</p>`;
            }
        }

        responsesHTML += `
                    </div>
                    <div class="flex flex-col items-end space-y-2">
                        ${statusBadge}
                    </div>
                </div>
                
                <div class="flex justify-between items-center">
                    <div class="flex space-x-2">
        `;

        responsesHTML += `
                        <button onclick="openChat(${response.id})" 
                                class="px-3 py-1.5 bg-blue-500 text-white rounded text-sm hover:bg-blue-600 transition-all duration-200 transform hover:scale-105">
                            <i class="fas fa-comments mr-1"></i>Чат
                        </button>
        `;

        if (isEmployer) {
            const buttonText = response.isConfirmed ? 'Отклонить' : 'Одобрить';
            const buttonIcon = response.isConfirmed ? 'fa-times' : 'fa-check';
            const buttonColor = response.isConfirmed ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600';

            responsesHTML += `
                        <button onclick="approveOrDismissResponse(${response.id})" 
                                class="px-3 py-1.5 ${buttonColor} text-white rounded text-sm transition-all duration-200 transform hover:scale-105">
                            <i class="fas ${buttonIcon} mr-1"></i>${buttonText}
                        </button>
            `;
        }

        responsesHTML += `
                    </div>
                </div>
            </div>
        `;
    });

    responsesHTML += '</div>';
    modalBody.innerHTML = responsesHTML;

    if (data.totalPages > 1) {
        let paginationHTML = '<div class="flex justify-center items-center space-x-2">';

        if (data.number > 0) {
            paginationHTML += `
                <button onclick="loadResponses(${JSON.stringify(currentUser)}, ${isConfirmed}, ${data.number})" 
                        class="px-3 py-1 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 transition-all duration-200 transform hover:scale-105">
                    <i class="fas fa-chevron-left"></i>
                </button>
            `;
        }

        const startPage = Math.max(0, data.number - 2);
        const endPage = Math.min(data.totalPages - 1, data.number + 2);

        for (let i = startPage; i <= endPage; i++) {
            const isCurrentPage = i === data.number;
            paginationHTML += `
                <button onclick="loadResponses(${JSON.stringify(currentUser)}, ${isConfirmed}, ${i + 1})" 
                        class="px-3 py-1 ${isCurrentPage ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'} rounded hover:bg-blue-600 transition-all duration-200 transform hover:scale-105">
                    ${i + 1}
                </button>
            `;
        }

        if (data.number < data.totalPages - 1) {
            paginationHTML += `
                <button onclick="loadResponses(${JSON.stringify(currentUser)}, ${isConfirmed}, ${data.number + 2})" 
                        class="px-3 py-1 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 transition-all duration-200 transform hover:scale-105">
                    <i class="fas fa-chevron-right"></i>
                </button>
            `;
        }

        paginationHTML += '</div>';
        modalPagination.innerHTML = paginationHTML;
    } else {
        modalPagination.innerHTML = '';
    }
}

function closeModal() {
    const modal = document.getElementById('responsesModal');
    const modalContent = document.getElementById('modalContent');

    modal.classList.add('opacity-0');
    modalContent.classList.remove('scale-100', 'translate-y-0', 'opacity-100');
    modalContent.classList.add('scale-95', 'translate-y-4', 'opacity-0');
}

function openChat(responseId) {
    window.location.href = `/chat/room/${responseId}`;
}

async function approveOrDismissResponse(responseId) {
    try {
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-CSRF-TOKEN';

        await fetch(`/api/responses/${responseId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            }
        });

        const currentUser = await fetchCurrentUser();
        if (currentUser) {
            await loadResponses(currentUser, false, 1);
            if (currentUser.role.name === 'APPLICANT') {
                await updateApplicantStats(currentUser.id);
            } else {
                await updateEmployerStats(currentUser.id);
            }
        }

    } catch (error) {
        console.error('Ошибка при обновлении отклика:', error);
        alert('Ошибка при обновлении статуса отклика');
    }
}