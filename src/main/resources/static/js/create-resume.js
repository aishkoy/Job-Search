let contactTypes = [];
let contactCounter;
let workExpCounter;
let educationCounter;

document.addEventListener('DOMContentLoaded', async () => {
    const containers = {
        contacts: document.getElementById('contacts-container'),
        workExperience: document.getElementById('work-experience-container'),
        education: document.getElementById('education-container')
    };

    if (!containers.contacts || !containers.workExperience || !containers.education) {
        console.error('Не найдены контейнеры для секций формы');
        return;
    }

    contactCounter = { value: getLastIndex('contacts') };
    workExpCounter = { value: getLastIndex('workExperiences') };
    educationCounter = { value: getLastIndex('educations') };

    contactTypes = await fetchContactTypes();

    initializeDeleteButtons();
    setupAddButtons(containers);
});


function setupAddButtons(containers) {
    setupAddContactButton(containers.contacts);
    setupAddWorkExperienceButton(containers.workExperience);
    setupAddEducationButton(containers.education);
}


function setupAddContactButton(container) {
    const addContactBtn = document.getElementById('add-contact-btn');
    if (addContactBtn) {
        addContactBtn.addEventListener('click', function(e) {
            e.preventDefault();
            createCard({
                container: container,
                counter: contactCounter,
                titlePath: 'contact.title',
                titleFallback: 'Контакт',
                deleteClass: 'contact-delete-btn',
                fieldPrefix: 'contacts',
                fields: [
                    {
                        label: { path: 'contact.type', fallback: 'Тип контакта' },
                        type: 'select',
                        name: 'contactType.id',
                        options: contactTypes
                    },
                    {
                        label: { path: 'contact.value', fallback: 'Значение контакта' },
                        type: 'text',
                        name: 'contactValue'
                    }
                ]
            });
        });
    }
}

function setupAddWorkExperienceButton(container) {
    const addWorkExpBtn = document.getElementById('add-experience-btn');
    if (addWorkExpBtn) {
        addWorkExpBtn.addEventListener('click', function(e) {
            e.preventDefault();
            createCard({
                container: container,
                counter: workExpCounter,
                titlePath: 'experience.title',
                titleFallback: 'Опыт работы',
                deleteClass: 'work-exp-delete-btn',
                fieldPrefix: 'workExperiences',
                fields: [
                    {
                        label: { path: 'experience.company', fallback: 'Компания' },
                        type: 'text',
                        name: 'companyName'
                    },
                    {
                        label: { path: 'experience.position', fallback: 'Должность' },
                        type: 'text',
                        name: 'position'
                    },
                    {
                        label: { path: 'experience.years', fallback: 'Лет опыта' },
                        type: 'number',
                        name: 'years',
                        min: 0,
                        max: 80
                    },
                    {
                        label: { path: 'experience.responsibilities', fallback: 'Обязанности' },
                        type: 'textarea',
                        name: 'responsibilities',
                        rows: 2
                    }
                ]
            });
        });
    }
}

function setupAddEducationButton(container) {
    const addEducationBtn = document.getElementById('add-education-btn');
    if (addEducationBtn) {
        addEducationBtn.addEventListener('click', function(e) {
            e.preventDefault();
            createCard({
                container: container,
                counter: educationCounter,
                titlePath: 'education.title',
                titleFallback: 'Образование',
                deleteClass: 'education-delete-btn',
                fieldPrefix: 'educations',
                fields: [
                    {
                        label: { path: 'education.institution', fallback: 'Учебное заведение' },
                        type: 'text',
                        name: 'institution'
                    },
                    {
                        label: { path: 'education.program', fallback: 'Программа обучения' },
                        type: 'text',
                        name: 'program'
                    },
                    {
                        label: { path: 'education.startDate', fallback: 'Дата начала' },
                        type: 'date',
                        name: 'startDate'
                    },
                    {
                        label: { path: 'education.endDate', fallback: 'Дата окончания' },
                        type: 'date',
                        name: 'endDate'
                    },
                    {
                        label: { path: 'education.degree', fallback: 'Степень' },
                        type: 'text',
                        name: 'degree'
                    }
                ]
            });
        });
    }
}


function initializeDeleteButtons() {
    setupDeleteButtons({
        selector: '.contact-delete-btn',
        containerId: 'contacts-container',
        fieldPrefix: 'contacts',
        counter: contactCounter
    });

    setupDeleteButtons({
        selector: '.work-exp-delete-btn',
        containerId: 'work-experience-container',
        fieldPrefix: 'workExperiences',
        counter: workExpCounter
    });

    setupDeleteButtons({
        selector: '.education-delete-btn',
        containerId: 'education-container',
        fieldPrefix: 'educations',
        counter: educationCounter
    });
}


function setupDeleteButtons({ selector, containerId, fieldPrefix, counter }) {
    document.querySelectorAll(selector).forEach(button => {
        if (!button.hasAttribute('data-initialized')) {
            button.setAttribute('data-initialized', 'true');
            button.addEventListener('click', function() {
                const card = this.closest('.p-4.border');
                if (card) {
                    card.remove();
                    const container = document.getElementById(containerId);
                    if (container) {
                        if (container.children.length === 0) {
                            container.classList.add('hidden');
                            counter.value = 0;
                        } else {
                            renumberCards(container, fieldPrefix, counter);
                        }
                    }
                }
            });
        }
    });
}


function renumberCards(container, fieldPrefix, counter) {
    const cards = Array.from(container.querySelectorAll('.p-4.border'));

    cards.forEach((card, index) => {
        const header = card.querySelector('h4');
        if (header) {
            const titleText = header.textContent;
            header.textContent = titleText.replace(/\d+/, index + 1);
        }

        const inputs = card.querySelectorAll(`[name^="${fieldPrefix}["]`);
        inputs.forEach(input => {
            const oldName = input.name;
            input.name = oldName.replace(/\[\d+]/, `[${index}]`);
        });

        const elementsWithId = card.querySelectorAll(`[id^="${fieldPrefix}_"]`);
        elementsWithId.forEach(element => {
            const oldId = element.id;
            const newId = oldId.replace(/\d+$/, index);

            if (element.id !== newId) {
                const label = document.querySelector(`label[for="${oldId}"]`);
                if (label) {
                    label.setAttribute('for', newId);
                }
                element.id = newId;
            }
        });
    });

    counter.value = cards.length;
}


function getMessage(key, fallback) {
    const el = document.querySelector(`#message-storage [data-key="${key}"]`);
    return el?.textContent?.trim() || fallback;
}


function getLastIndex(fieldName) {
    const elements = document.querySelectorAll(`[name^="${fieldName}["]`);

    if (elements.length === 0) return 0;

    let maxIndex = -1;

    elements.forEach(element => {
        const match = element.name.match(/\[(\d+)]/);
        if (match) {
            const index = parseInt(match[1], 10);
            if (!isNaN(index) && index > maxIndex) {
                maxIndex = index;
            }
        }
    });

    return maxIndex + 1;
}


async function fetchContactTypes() {
    try {
        const response = await fetch('/api/contact-types');

        if (!response.ok) {
            throw new Error('Ошибка при загрузке типов контактов');
        }

        return await response.json();
    } catch (error) {
        console.error('Ошибка загрузки типов контактов:', error);
        return [];
    }
}


function createInputHTML(field, inputName) {
    const baseClass = "mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500";

    if (field.type === 'select') {
        const options = field.options.map(opt =>
            `<option value="${opt.id}">${opt.name}</option>`
        ).join('');

        return `<select name="${inputName}" class="${baseClass}">${options}</select>`;
    }

    if (field.type === 'textarea') {
        return `<textarea name="${inputName}" class="${baseClass}" rows="${field.rows || 2}"></textarea>`;
    }

    return `
        <input 
            type="${field.type}" 
            name="${inputName}" 
            class="${baseClass}"
            ${field.min !== undefined ? `min="${field.min}"` : ''}
            ${field.max !== undefined ? `max="${field.max}"` : ''}>
    `;
}


function generateFieldsHTML(fields, fieldPrefix, index) {
    const useVerticalLayout = fields.length > 2;

    if (!useVerticalLayout) {
        return `
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                ${fields.map(field => {
            const labelText = getMessage(field.label.path, field.label.fallback);
            const inputName = `${fieldPrefix}[${index}].${field.name}`;
            return `
                        <div>
                            <label class="block text-sm font-medium text-gray-700">${labelText}</label>
                            ${createInputHTML(field, inputName)}
                        </div>
                    `;
        }).join('')}
            </div>
        `;
    }

    let html = '';

    html += '<div class="grid grid-cols-1 md:grid-cols-2 gap-4">';
    for (let i = 0; i < Math.min(2, fields.length); i++) {
        const field = fields[i];
        const labelText = getMessage(field.label.path, field.label.fallback);
        const inputName = `${fieldPrefix}[${index}].${field.name}`;
        html += `
            <div>
                <label class="block text-sm font-medium text-gray-700">${labelText}</label>
                ${createInputHTML(field, inputName)}
            </div>
        `;
    }
    html += '</div>';

    if (fields.length > 2) {
        const remainingFields = fields.slice(2);
        const colSpan = Math.min(remainingFields.length, 3);
        html += `<div class="grid grid-cols-1 md:grid-cols-${colSpan} gap-4">`;

        remainingFields.forEach(field => {
            const labelText = getMessage(field.label.path, field.label.fallback);
            const inputName = `${fieldPrefix}[${index}].${field.name}`;
            html += `
                <div>
                    <label class="block text-sm font-medium text-gray-700">${labelText}</label>
                    ${createInputHTML(field, inputName)}
                </div>
            `;
        });

        html += '</div>';
    }

    return html;
}


function createCard({ container, counter, titlePath, titleFallback, fields, deleteClass, fieldPrefix }) {
    container.classList.remove('hidden');

    const index = counter.value;
    const title = getMessage(titlePath, `${titleFallback} ${index + 1}`).replace('{0}', index + 1);

    const card = document.createElement('div');
    card.className = 'p-4 border border-gray-200 rounded-lg bg-gray-50';

    card.innerHTML = `
        <div class="flex justify-between items-center mb-3">
            <h4 class="font-medium text-gray-800">${title}</h4>
            <div>
                <button type="button" class="${deleteClass} text-red-500 hover:text-red-700">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        </div>
        <div class="space-y-4">
            ${generateFieldsHTML(fields, fieldPrefix, index)}
        </div>
    `;

    container.appendChild(card);
    counter.value++;

    const deleteButton = card.querySelector(`.${deleteClass}`);
    if (deleteButton) {
        deleteButton.setAttribute('data-initialized', 'true');
        deleteButton.addEventListener('click', function() {
            card.remove();

            if (container.children.length === 0) {
                container.classList.add('hidden');
                counter.value = 0;
            } else {
                renumberCards(container, fieldPrefix, counter);
            }
        });
    }
}