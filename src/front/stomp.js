var stompClient = null;

var socket = new SockJS(`http://localhost:8080/websocket`);
stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {

    console.log('Connected: ' + frame);
    
    stompClient.subscribe(`/topic/chat/1`, function (receivedMesssage) {
        var message= JSON.parse(receivedMesssage.body);
        //console.log(receivedMesssage.body + "  서버에서 날아온 메시지 ");

        const now = new Date();
        const year = now.getFullYear();
        const month = now.getMonth() + 1;
        const day = now.getDate();
        const hour = now.getHours();
        const minute = now.getMinutes();
        const meridiem = hour >= 12 ? "오후" : "오전";
        const formattedTime = `${year}년 ${month}월 ${day}일 ${meridiem} ${hour % 12}:${minute < 10 ? "0" + minute : minute}`;


        const container = document.querySelector('.chatbox');
        const input = document.getElementById('messageContent');

        const div = document.createElement('div');

        /*if (memberId == message.memberId) {
          div.style.backgroundColor = '#c4e8ed'; 
        }*/
        

        div.className = 'message';
        div.innerHTML = `
          <span class="name">${message.memberName} :</span>
          <span class="text">${message.content}</span>
          <span class="time">${formattedTime}</span>
          `;
        container.appendChild(div);
        input.value = '';
        container.scrollTop = container.scrollHeight;


    });


});