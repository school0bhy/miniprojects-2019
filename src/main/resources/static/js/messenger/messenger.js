document.addEventListener("DOMContentLoaded", function() {
    WebSocket.init();
});

let WebSocket = (function() {
    const roomId = document.body.querySelector('div').dataset.id;

    const SERVER_SOCKET_API = "/websocket";
    const ENTER_KEY = 13;
    let stompClient;

    const connect = () => {
        let socket = new SockJS(SERVER_SOCKET_API);
        stompClient = Stomp.over(socket);
        // SockJS와 stomp client를 통해 연결을 시도.
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/chat/'+roomId, function (chat) {
                showChat(JSON.parse(chat.body));
            })
        });
    }

    const showChat = (message) => {
        const messages = document.getElementById('messages')
        messages.appendChild(createMessageDom(message))
    }

    const createMessageDom = (message) => {
        const direction = isMyMessage(message.sender) ? 'right' : 'left'
        const div = document.createElement('div')
        div.innerHTML = messageTemplate
        div.querySelector('.sender').innerText = message.sender.name
        div.querySelector('.text').innerText = message.content
        div.querySelector('li').setAttribute('class', 'message appeared '+direction)
        return div.firstElementChild
    }

    const isMyMessage = (sender) => sender.id == localStorage.loginUserId

    function clear(input) {
        input.value = "";
    }

    const sendChat = () => {
        const chatMessage = document.getElementById('messageInput').value
        stompClient.send("/app/chat/"+roomId, {}, JSON.stringify({'message': chatMessage}));
        clear(document.getElementById('messageInput'))
    }

    const init = function () {
        const sendButton = document.getElementById('sendMessage');
        sendButton.addEventListener('click', sendChat);

        connect()
    }

    return {
        init: init,
    }
})();

