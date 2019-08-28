
const userList = document.getElementById('all-user-list')

function startMessenger(event) {
    const startMessengerButton = event.target.closest('button')
    if (startMessengerButton != null && startMessenger.classList.contains('start-messenger')) {
        const id = new Set([startMessengerButton.closest('div').closest('div').dataset.id]);

        api.POST("/messenger", id)
            .then(response => {
                if (!response.ok) {
                    throw response;
                }
                return response.text();
            })
            .then(roomId => {
                window.location.href = '/messenger/' + roomId;
            })
            .catch(errorResponse =>
                console.log(errorResponse)
            )
    }
}

userList.addEventListener('click', startMessenger)