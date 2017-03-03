var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        sendRequest();

        stompClient.subscribe('/rx/comment', function (response) {
            showResult($.parseJSON(response.body))
        });

        stompClient.subscribe('/rx/post', function (response) {
            showResult($.parseJSON(response.body))
        });

    });
}

function sendRequest() {
    var pathName = window.location.pathname;
    switch (pathName) {
        case '/rx/comment':
            stompClient.send("/app/service/rx/comment", {}, JSON.stringify({'request': 'get comments via rx'}));
            break;
        case '/rx/post':
            stompClient.send("/app/service/rx/post", {}, JSON.stringify({'request': 'get posts via rx'}));
            break;
        case '/rx/mix':
            stompClient.send("/app/service/rx/mix", {}, JSON.stringify({'request': 'get posts and comments both via rx'}));
            break;
        case '/rx/post/comment':
            stompClient.send("/app/service/rx/post/comment", {}, JSON.stringify({'request': 'get post\'s comments via rx'}));
            break;
        default:
    }
}

var messages = [];
function showResult(message) {
    messages.push(message);
    $('body').html(JSON.stringify(messages));
    $(document).scrollTop($(document).height());
}

$(function () {
    connect();
});