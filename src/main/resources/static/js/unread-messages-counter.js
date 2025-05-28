document.addEventListener('DOMContentLoaded', async function() {
    if (typeof isUserLoggedIn !== 'undefined' && isUserLoggedIn) {
        await fetchUnreadMessagesCount();
        setInterval(fetchUnreadMessagesCount, 60000);
    }

    async function fetchUnreadMessagesCount() {
        try {
            const userResponse = await fetch('/api/users/current');
            if (!userResponse.ok) {
                throw new Error('Ошибка получения пользователя');
            }

            const user = await userResponse.json();
            if (!user || !user.id) {
                throw new Error('Информация о пользователе недоступна');
            }

            const messagesResponse = await fetch(`/api/messages/unread/by-user?userId=${user.id}`);
            if (!messagesResponse.ok) {
                throw new Error('Ошибка получения непрочитанных сообщений');
            }

            const count = await messagesResponse.json();
            updateUnreadCounter(count);
        } catch (error) {
            console.error('Ошибка при получении непрочитанных сообщений:', error);
        }
    }

    function updateUnreadCounter(count) {
        const unreadBadge = document.getElementById('unread-messages-count');
        if (!unreadBadge) return;

        if (count && count > 0) {
            unreadBadge.textContent = count > 99 ? '99+' : count;
            unreadBadge.classList.remove('hidden');
        } else {
            unreadBadge.classList.add('hidden');
        }
    }
});