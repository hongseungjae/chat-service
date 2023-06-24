const urlParams = new URLSearchParams(window.location.search);
const roomId = urlParams.get('roomId');
console.log('roomId : '+ roomId);

var stompClient = null;
var headers = {Authorization: localStorage.getItem('access_token')};

var socket = new SockJS(`http://localhost:8080/websocket`);

stompClient = Stomp.over(socket);

stompClient.connect(headers, (frame) => {
    console.log('Connected: ' + frame);
    
    stompClient.subscribe(`/topic/${roomId}`, function (receivedMesssage) {
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
          <span class="name">${message.sourceName} :</span>
          <span class="text">${message.content}</span>
          <span class="time">${formattedTime}</span>
          `;
        container.appendChild(div);
        input.value = '';
        container.scrollTop = container.scrollHeight;

        if(message.messageType === 'JOIN') {
          const userListElement = document.getElementById('userlist');
          userListElement.innerHTML = '';

          message.userNames.forEach(userName => {
            const userElement = document.createElement('div');
            userElement.setAttribute('id', userName);
            userElement.classList.add('userlist-item');
            userElement.innerText = userName;
            userListElement.appendChild(userElement);
        });
  
        } else if(message.messageType === 'LEAVE') {

          const userListElement = document.getElementById('userlist');
          const user = userListElement.querySelector(`#${message.leavedUserName}`);
          user.remove();

        }



    }, {Authorization: localStorage.getItem('access_token')})
  }, (error) => {
      console.log('연결실패');
      console.log(error)
    });


function sendMessage() {
  const content = document.getElementById('messageContent');
  const writer = document.getElementById('messageWriter');
  const ChatRequest = {
  roomId: roomId,
  content: content.value,
  };

  /*var headers = {
  'header1': 'value1',
  'header2': 'value2',
  };*/

  stompClient.send('/app/chat', {Authorization: localStorage.getItem('access_token')}, JSON.stringify(ChatRequest));

}