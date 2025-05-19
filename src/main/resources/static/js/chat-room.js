document.addEventListener('DOMContentLoaded', async function() {
    const pathParts = window.location.pathname.split('/');
    const responseId = pathParts[3];

    let stompClient = null;
    let currentUser = null;

    await init();

    async function init() {
        try {
            const [_, user, responseData] = await Promise.all([
                connectToWebSocket(),
                loadCurrentUser(),
                loadResponseDetails(responseId)
            ]);

            await loadMessages(responseId);
        } catch (error) {
            console.error('Error initializing chat:', error);
            showError();
        }
    }

    async function connectToWebSocket() {
        try {
            const socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);

            stompClient.debug = null;

            return new Promise((resolve, reject) => {
                stompClient.connect({}, function(frame) {
                    console.log('Connected to WebSocket');

                    stompClient.subscribe(`/topic/response/${responseId}`, function(message) {
                        try {
                            const receivedMessage = JSON.parse(message.body);
                            addMessageToChat(receivedMessage);
                        } catch (error) {
                            console.error('Error processing received message:', error);
                        }
                    });

                    resolve();
                }, function(error) {
                    console.error('WebSocket connection error:', error);
                    reject(error);
                });
            });
        } catch (error) {
            console.error('Error setting up WebSocket:', error);
            throw error;
        }
    }

    async function loadCurrentUser() {
        try {
            const response = await fetch('/api/users/current');
            if (!response.ok) {
                throw new Error('Ошибка получения данных пользователя');
            }

            const user = await response.json();
            if (!user || !user.id) {
                throw new Error('Получены некорректные данные пользователя');
            }

            currentUser = user;
            return user;
        } catch (error) {
            console.error('Error loading current user:', error);
            throw error;
        }
    }

    async function loadResponseDetails(responseId) {
        try {
            const response = await fetch(`/api/responses/${responseId}`);
            if (!response.ok) {
                throw new Error('Ошибка получения данных ответа');
            }

            const responseData = await response.json();
            if (!responseData) {
                throw new Error('Получены некорректные данные ответа');
            }

            const isEmployer = currentUser.role.name === 'ROLE_EMPLOYER';

            const titleElement = document.getElementById('chat-title');
            const subtitleElement = document.getElementById('chat-subtitle');

            if (titleElement && subtitleElement) {
                if (isEmployer) {
                    titleElement.textContent = responseData.resume?.position || 'Резюме';

                    const firstName = responseData.resume?.applicant?.name || '';
                    const lastName = responseData.resume?.applicant?.surname || '';
                    subtitleElement.textContent = `${firstName} ${lastName}`.trim() || 'Соискатель';
                } else {
                    titleElement.textContent = responseData.vacancy?.name || 'Вакансия';
                    subtitleElement.textContent = responseData.vacancy?.employer?.name || 'Компания';
                }
            }

            const statusElement = document.getElementById('chat-status');
            if (statusElement) {
                if (responseData.isConfirmed) {
                    statusElement.innerHTML = `<span class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full"><i class="fas fa-check-circle mr-1"></i>${messages.statusConfirmed}</span>`;
                } else {
                    statusElement.innerHTML = `<span class="bg-gray-100 text-gray-800 text-xs px-2 py-1 rounded-full"><i class="fas fa-clock mr-1"></i>${messages.statusPending}</span>`;
                }
            }

            return responseData;
        } catch (error) {
            console.error('Error loading response details:', error);
            throw error;
        }
    }

    async function loadMessages(responseId) {
        try {
            const response = await fetch(`/api/messages/responses/${responseId}`);
            if (!response.ok) {
                throw new Error('Ошибка получения сообщений');
            }

            const messages = await response.json();
            const container = document.getElementById('messages-container');
            if (!container) return;

            container.innerHTML = '';

            if (messages && messages.length > 0) {
                messages.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

                messages.forEach(message => {
                    addMessageToChat(message, false);
                });

                scrollToBottom();
            } else {
                container.innerHTML = `
                    <div class="text-center py-8">
                        <p class="text-gray-500">${messages.noMessages}</p>
                    </div>
                `;
            }
        } catch (error) {
            console.error('Error loading messages:', error);
            showError();
        }
    }

    function addMessageToChat(message, isNew = true) {
        if (!message || !message.content) {
            console.error('Некорректное сообщение:', message);
            return;
        }

        const container = document.getElementById('messages-container');
        if (!container) return;

        if (container.querySelector('.text-gray-500')) {
            container.innerHTML = '';
        }

        const messageElement = document.createElement('div');
        const isCurrentUser = message.user?.id === currentUser?.id;

        messageElement.className = `mb-4 ${isCurrentUser ? 'text-right' : 'text-left'}`;

        const timestamp = new Date(message.timestamp);
        const formattedTime = timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        const formattedDate = timestamp.toLocaleDateString();

        let senderName = isCurrentUser
            ? messages.you
            : `${message.user?.name || ''} ${message.user?.surname || ''}`.trim();

        if (!senderName || senderName === messages.you) {
            senderName = isCurrentUser ? messages.you : 'Пользователь';
        }

        messageElement.innerHTML = `
            <div class="inline-block max-w-3/4 ${isCurrentUser ? 'bg-blue-100 text-blue-900' : 'bg-gray-100 text-gray-900'} rounded-lg px-4 py-2 break-words">
                <p>${escapeHtml(message.content)}</p>
                <p class="text-xs ${isCurrentUser ? 'text-blue-600' : 'text-gray-600'} mt-1">${formattedTime}, ${formattedDate}</p>
            </div>
            <div class="text-xs ${isCurrentUser ? 'text-blue-600' : 'text-gray-600'} mt-1">
                ${senderName}
            </div>
        `;

        container.appendChild(messageElement);

        if (isNew) {
            scrollToBottom();

            if (!isCurrentUser && message.id) {
                markMessageAsRead(message.id);
            }
        }
    }

    async function markMessageAsRead(messageId) {
        try {
            await fetch(`/api/messages/${messageId}/read`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                }
            });
        } catch (error) {
            console.error('Error marking message as read:', error);
        }
    }

    function scrollToBottom() {
        const container = document.getElementById('messages-container');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }

    function sendMessage(content) {
        if (!content || !content.trim()) return;

        if (!stompClient) {
            console.error('WebSocket не подключен');
            return;
        }

        const message = {
            content: content.trim(),
            response: { id: responseId },
            isRead: false,
            user: { id: currentUser?.id }
        };

        stompClient.send(`/ws/chat.send.${responseId}`, {}, JSON.stringify(message));

        const messageInput = document.getElementById('message-input');
        if (messageInput) {
            messageInput.value = '';
        }
    }

    function showError() {
        const container = document.getElementById('messages-container');
        if (!container) return;

        container.innerHTML = `
            <div class="text-center py-8">
                <p class="text-red-500">${messages.errorLoading}</p>
                <button class="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600" onclick="window.location.reload()">
                    ${messages.retry}
                </button>
            </div>
        `;
    }

    function escapeHtml(unsafe) {
        if (!unsafe) return '';

        return unsafe
            .toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    const messageForm = document.getElementById('message-form');
    if (messageForm) {
        messageForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const messageInput = document.getElementById('message-input');
            if (messageInput) {
                sendMessage(messageInput.value);
            }
        });
    }

    const messageInput = document.getElementById('message-input');
    if (messageInput) {
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage(this.value);
            }
        });
    }
});