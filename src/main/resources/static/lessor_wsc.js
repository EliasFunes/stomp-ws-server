let stompClient = null;

let globalBlob = null;

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

    //ver si mandar el qrid
    const qrId = $("#id-qr").text()
    let data = {}
    if(qrId) {
        data = {username: "username", 'X-Authorization': token, "qrId": qrId}
    } else {
        data = {username: "username", 'X-Authorization': token}
    }


    stompClient.connect(data, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/topic/messages", (message) => {
            console.log("llego un mensaje", message);

            //TODO:El mensaje a recibir es el user Id de quien se quiere loggear
            // se podria preparar un enpoint aqui que valide los datos y retorne si es valido
            // una vez retorna ejecutar el callback o enpoint que tiene que proveer el tenant para loggear su usuario
            // y como unico parametro el identificador de su usuario

            //TODO: esta funcion pueda esperar una funcion callback que es la funcion que utiliza cada cliente
            // para logear a su usuario.

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

function sendToUser(tokenQr) {
    postData(
        'http://localhost:8080/ws/sendToUser',
        { "tokenQr": tokenQr })
        .then(data => {
            console.log(data)
        })
        .catch(e => {
            console.log("error")
            console.log(e)
        })
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect_user" ).click(function() { connect_user() });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send_user" ).click(function() { sendToUser($("#name").val()) });
});

async function postData(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('Auth-Token'),
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)

    });
    return response.json();
}

