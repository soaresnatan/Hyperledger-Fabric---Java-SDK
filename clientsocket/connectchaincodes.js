var SockJS = require('sockjs-client');
var Stomp = require('stomp-websocket');


function connect() {
    var socket = new SockJS('http://localhost:3000/cdtblockchainwebsocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chaincodes', function (msg) {
           showMessages(msg.body);
        });
    });
}

function showMessages(message) {
    console.log(message);
}

connect();
