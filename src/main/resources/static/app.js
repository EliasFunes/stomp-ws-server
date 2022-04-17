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

function connect() {
  localStorage.setItem('Auth-Token', 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbGlhc2YiLCJleHAiOjE2NTAxNTgxNDEsImlhdCI6MTY1MDE0MDE0MX0.xYfTs5-SCBb6kDfirD7IWEa2suW2010ptQTeATsp2rFC0T6Td-YgFqpZmb9xJO-gHIwGGRguZMIBRJhnBvSeKQ')
  let token = localStorage.getItem('Auth-Token') // eslint-disable-line


  let socket = new SockJS('/gs-guide-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({'Auth-Token': token}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function (greeting) {
      showGreeting(JSON.parse(greeting.body).content);
    });
  });
}

function connect_user(username) {
  localStorage.setItem('Auth-Token', 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbGlhc2YiLCJleHAiOjE2NTAyNDc2NzcsImlhdCI6MTY1MDIyOTY3N30._GThxDHp4MAwBqvOSmlaUYg2v3MuvhtMjW-SxRaiIMW91MeQS1mbaVY34kZhhAVQ-bWNAOV8aHa2pOWBeHbYpw')
  let token = localStorage.getItem('Auth-Token') // eslint-disable-line

  console.log("entra en connect user");
  console.log("username:", username);
  if(username){
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket)
    stompClient.connect({username: username, 'X-Authorization': token}, (frame) => {
      console.log('Connected: ' + frame);
      stompClient.subscribe("/user/topic/messages", (message) => {
        console.log("llego un mensaje", message);
        console.log("body", message.body);
        showGreeting(message.body);
      }/*, {'X-Authorization': token}*/)
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
  let token = localStorage.getItem('Auth-Token')
  console.log(`se envia a ${userName}`)
  stompClient.send("/app/hello_user", {/*'X-Authorization': token*/}, userName)
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