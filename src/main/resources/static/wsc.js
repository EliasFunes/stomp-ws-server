let stompClient = null;


//TODO: mientras
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

function connect_user() {
    let token = localStorage.getItem('Auth-Token') // eslint-disable-line
    let socket = new SockJS('http://localhost:8080/wsc');
    stompClient = Stomp.over(socket)
    stompClient.connect({username: "username", 'X-Authorization': token}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/topic/messages", (message) => {
            console.log("llego un mensaje", message);
            console.log("body", message.body);
            showGreeting(message.body);
        })
    })

}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sendToUser(userName) {
    stompClient.send("/app/hello_user", {/*'X-Authorization': token*/}, userName)
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect_user" ).click(function() { connect_user() });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send_user" ).click(function() { sendToUser($("#name").val()) });
});