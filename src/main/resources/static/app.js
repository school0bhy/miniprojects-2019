const roomId = document.body.querySelector('div').dataset.id;

const WebSocket = (function() {
    const SERVER_SOCKET_API = "/websocket";
    const ENTER_KEY = 13;
    let stompClient = null;

    const setConnected = (connected) => {
        if (connected) {
            document.getElementById('connect').setAttribute('disabled', connected)
            document.getElementById('disconnect').removeAttribute('disabled')
            document.getElementById('conversation').style.display = "block"
        } else {
            document.getElementById('disconnect').setAttribute('disabled', "true")
            document.getElementById('connect').removeAttribute('disabled')
            document.getElementById('conversation').style.display = "none"
        }

        document.getElementById('greetings').innerHTML = "";
    }

    const connect = () => {
        let socket = new SockJS(SERVER_SOCKET_API);
        stompClient = Stomp.over(socket);
        // SockJS와 stomp client를 통해 연결을 시도.
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/chat/'+roomId, function (chat) {
                showChat(JSON.parse(chat.body));
            })
        });
    }

    const disconnect = () => {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    const sendChat = () => {
        const chatMessage = document.getElementById('chatMessage').value
        stompClient.send("/app/chat/"+roomId, {}, JSON.stringify({'message': chatMessage}));
    }

    const chatMessageTemplate = (chat) => `
    ${chat.name} : ${chat.message}
    `

    const showChat = (chat) => {
        const messages = document.getElementById('greetings');
        const div = document.createElement('div');
        div.innerHTML = chatMessageTemplate(chat)
        messages.append(div)
    }

    const init = function () {
        const connectButton = document.getElementById('connect');
        connectButton.addEventListener('click', connect);

        const disconnectButton = document.getElementById('disconnect');
        disconnectButton.addEventListener('click', disconnect);

        const sendButton = document.getElementById('chatSend');
        sendButton.addEventListener('click', sendChat);

        connect()
    }

    return {
        init: init,
        disconnect: disconnect
    }
})()

WebSocket.init();