function createRoom() {
    // 팝업 창에서 입력한 방 이름 가져오기
    var roomName = window.document.getElementById("roomName").value;
    var roomHeadCount = window.document.getElementById("roomHeadCount").value;

    // 방 이름이 비어있지 않은 경우에만 방 만들기
    if (roomName.trim() !== "" && roomHeadCount.trim() !== "") {

        let room = {
            'memberId': 1,
            'roomName': roomName,
            'maxHeadCount': roomHeadCount
        };

        fetch(`http://${API_IP}:${API_PORT}/rooms`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                //'withCredentials': 'true'
            },
            body: JSON.stringify(room)
        })
            .then(response => {
                if (response.status == 401 || response.status == 403) {
                    alert('로그인을 해주세요.');
                    console.log(response);
                } else if (!response.ok) {
                    response.json().then(error => {
                        alert(error.message);
                    });
                } else {
                    alert('채팅방을 생성하였습니다.');
                    opener.parent.location.reload();
                    window.close();
                }
            }).catch(error => console.error(error));

        
    } else {
        alert("올바른 값을 입력해주세요.");
    }
}