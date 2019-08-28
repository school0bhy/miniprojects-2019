$(document).ready(function () {
    friendBarModule.init()
})

const friendBarModule = (function () {
    async function loadAllUser() {
        await api.GET("/api/users")
            .then(res => res.json())
            .then(data => {
                for (let i = 0; i < data.length; i++) {
                    createUserDOM(data[i])
                }
                addFriendsAskButtonClickListener()
                addChatStartButtonClickListener()
            })
    }

    async function loadFriends() {
        await api.GET("/api/friends")
            .then(res => res.json())
            .then(data => {
                for (let i = 0; i < data.length; i++) {
                    createFriendDOM(data[i])
                }
                addFriendsRemoveBtnClickListener()
            })
    }

    function createUserDOM(user) {
        document.getElementById('all-user-list').insertAdjacentHTML('beforeend', allUserTemplate(user))
    }

    function createFriendDOM(friend) {
        document.getElementById('friends-list').insertAdjacentHTML('beforeend', friendTemplate(friend))
    }

    function addFriendsAskButtonClickListener() {
        const btns = document.getElementsByClassName('FriendAsk')
        for (let i = 0; i < btns.length; i++) {
            btns[i].addEventListener('click', async function (event) {
                const data = {"receiverId": event.target.parentNode.parentNode.getAttribute("data-id")}
                const res = await api.POST("/api/friends/asks", data)

	            if(res.status == 201) {
	                alert('친구 요청을 보냈습니다.')
	            } else if(res.status == 400) {
                    const error = await res.json()
    	            alert(error.message)
	            }
			})
		}
	}

	function addChatStartButtonClickListener() {
        const userList = document.getElementById('all-user-list')
        userList.addEventListener('click',  function (event) {
            const startMessengerButton = event.target.closest('button')
            if (startMessengerButton != null && startMessengerButton.classList.contains('start-messenger')) {
                console.log('-----------------')
                const ids = new Set([startMessengerButton.closest('div.btn-group').dataset.id]);
                console.log(ids)
                const messengerRequest = {"userIds" : ids}

                api.POST("/messenger", messengerRequest)
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
        })
    }

    function startMessenger(event) {
        console.log("hi")
        const startMessengerButton = event.target.closest('button')
        if (startMessengerButton != null && startMessengerButton.classList.contains('start-messenger')) {
            console.log("hiii")
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

	async function addFriendsRemoveBtnClickListener() {
		const btns = document.getElementsByClassName('FriendRemove')
		for(let i = 0; i < btns.length; i++) {
			btns[i].addEventListener('click', async function(event) {
                const friendId = event.target.parentNode.parentNode.getAttribute("data-id")
                const res = await api.DELETE("/api/friends/" + friendId)

	            if(res.status == 204) {
	                alert('친구가 삭제되었습니다.')
                    const grandParent = event.target.parentNode.parentNode
                    grandParent.parentNode.removeChild(grandParent)
	            } else if(res.status == 400) {
                    const error = await res.json()
    	            alert(error.message)
	            }
			})
		}
	}

    function preventDropdownHide() {
        // $('div.btn-group.dropleft').on('click', function (event) {
        //     let events = $._data(document, 'events') || {}
        //     events = events.click || []
        //     for (let i = 0; i < events.length; i++) {
        //         if (events[i].selector) {
        //             if ($(event.target).is(events[i].selector)) {
        //                 events[i].handler.call(event.target, event)
        //             }
        //
        //             $(event.target).parents(events[i].selector).each(function () {
        //                 events[i].handler.call(this, event)
        //             })
        //         }
        //     }
        //     event.stopPropagation()
        // })
    }

    return {
        init: async function () {
            await loadAllUser()
            await loadFriends()
            preventDropdownHide()
        }
    }
})()