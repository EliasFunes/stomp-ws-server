let stompClient = null;

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
  let socket = new SockJS('/gs-guide-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function (greeting) {
      showGreeting(JSON.parse(greeting.body).content);
    });
  });
}

function connect_user(username) {
  console.log("entra en connect user");
  console.log("username:", username);
  if(username){
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket)
    stompClient.connect({username: username}, (frame) => {
      console.log('Connected: ' + frame);
      stompClient.subscribe("/user/topic/messages", (message) => {
        console.log("llego un mensaje", message);
        console.log("body", message.body);
        showGreeting(message.body);
      })
    })
  } else {
    throw new Error("Username no definido")
  }

}


function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function sendToUser(userName) {
  console.log(`se envia a ${userName}`)
  stompClient.send("/app/hello_user", {}, userName)
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $( "#connect" ).click(function() { connect(); });
  $( "#connect_user" ).click(function() { connect_user($("#user_name").val()) });
  $( "#disconnect" ).click(function() { disconnect(); });
  $( "#send" ).click(function() { sendName(); });
  $( "#send_user" ).click(function() { sendToUser($("#name").val()) });
});