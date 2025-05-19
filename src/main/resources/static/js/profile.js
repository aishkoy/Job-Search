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
        })
        .catch(error => {
            console.error('Ошибка при загрузке информации о пользователе:', error);
        });
});

async function fetchCurrentUser() {
    try {
        const response = await fetch('/api/users/current');
        if (!response.ok) {
            throw new Error('Не удалось получить информацию о пользователе');
        }
        return await response.json();
    } catch (error) {
        console.error('Ошибка при получении текущего пользователя:', error);
        return null;
    }
}

async function updateApplicantStats(applicantId) {
    try {
        const allResponses = await fetch(`/api/responses/applicants/${applicantId}/count`);
        if (!allResponses.ok) {
            throw new Error('Не удалось получить общее количество откликов');
        }
        const responseCount = await allResponses.json();

        const confirmedResponse = await fetch(`/api/responses/applicants/${applicantId}/count?isConfirmed=true`);
        if (!confirmedResponse.ok) {
            throw new Error('Не удалось получить количество подтверждённых откликов');
        }
        const confirmedCount = await confirmedResponse.json();

        updateStatElements(responseCount, confirmedCount);
    } catch (error) {
        console.error('Ошибка при обновлении статистики соискателя:', error);
    }
}

async function updateEmployerStats(employerId) {
    try {
        const allResponses = await fetch(`/api/responses/employers/${employerId}/count`);
        if (!allResponses.ok) {
            throw new Error('Не удалось получить общее количество откликов');
        }
        const responsesCount = await allResponses.json();

        const confirmedResponse = await fetch(`/api/responses/employers/${employerId}/count?isConfirmed=true`);
        if (!confirmedResponse.ok) {
            throw new Error('Не удалось получить количество подтверждённых откликов');
        }
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