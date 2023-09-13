 const socketUrl = 'ws://192.168.1.25:8081/ws-login';
 const stompClient = new StompJs.Client({
     brokerURL: socketUrl;
 });
   stompClient.onConnect = (frame) => {
          setConnected(true);
          console.log('Connected: ' + frame);
          stompClient.subscribe('/topic/successfull', (greeting) => {
              showGreeting(JSON.parse(greeting.body));
          });
      };
     function afterWindowLoad() {
     stompClient.activate();
     }
     function showGreeting(message) {
         $("#greetings").append("<tr><td>" + message + "</td></tr>");
     }