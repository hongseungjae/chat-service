fetch(`http://${API_IP}:${API_PORT}/rooms`, {
    method: 'GET',
    //credentials: 'include',
    headers: {
        'Content-Type': 'application/json'
    },
})
    .then(response => response.json())
    .then(rooms => {

        //console.log(rooms[0].roomName);
        const roomlist = document.querySelector('#roomlist');
        roomlist.innerHTML = "";
        let roomsHtml ='';


        for (let i = 0; i < rooms.length; i++) {
            const room = rooms[i];
            let roomAdmin = "방장";

            for(let j = 0; j < room.participantsInfo.length; j++) {
                const participant = room.participantsInfo[j];
                if (participant.roomRole == "room_admin") {
                    roomAdmin = participant.memberName;
                }
            }

        roomsHtml += `
            <tr id="room-${room.id}" onclick="roomJoin(${room.id})" class="alert" role="alert">
            <th scope="row">${room.id}</th>
            <td>${roomAdmin}</td>
            <td>1/${room.maxHeadCount}</td>
            <td>${room.roomName}</td>
            <td>
                <a href="#" class="close" data-dismiss="alert" aria-label="Close">
              <span aria-hidden="true"><i class="fa fa-close"></i></span>
            </a>
          </td>
          </tr>
        `;
        }

        roomlist.innerHTML += roomsHtml;

    });

    function roomJoin(roomId){
            console.log(roomId);
    }

	
