let num = '';

const init = function (data) {
    num = data;
}

/** 댓글 리스트 **/
const replyList = function () {
    fetch('/replist/' + num, {
        method : 'GET',
        headers: {
            'Accept': 'application/json'
        }
    }).then((response) => {
        if (!response.ok) {
            throw new Error("Fail to load reply list");
        }
        return response.json();
    }).then((data) => {
        data.forEach(item => {
            let ele_p1 = document.createElement('p');
            let ele_p2 = document.createElement('p');
            let ele_ul = document.createElement('ul');
            let ele_li = document.createElement('li');
            let nickname = document.createTextNode('[' + item.nickname + ']');
            let content = document.createTextNode(' ' + item.frcontent);
            let uid = item.uid;
            let frid = item.frid;

            ele_p1.appendChild(nickname);
            ele_p2.appendChild(content);
            ele_p1.appendChild(ele_p2);
            ele_li.appendChild(ele_p1);
            ele_ul.appendChild(ele_li);

            /** 삭제 버튼 추가 **/
            let sessionId = document.getElementById('sessionId').value;
            if (String(sessionId) === String(uid) || String(sessionId) === '100') {
                let deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.id = 'del_btn'
                deleteButton.onclick = function () {
                    if (confirm('정말 삭제할까요?')) {
                        deleteRep(frid);
                    }
                };
                ele_li.appendChild(deleteButton);
            }
            document.getElementById('replyList').appendChild(ele_ul);

        })
    }).catch(error => {
        console.log('Error: ', error);
    }).finally(() => {
        console.log("reply list finally");
    });

}

/** 댓글 삭제 **/
const deleteRep = function (frid) {
    fetch('/repdelete/' + frid, {
        method: 'GET',
    }).then((response) => {
        if (!response.ok) {
            throw new Error('Failed to delete reply');
        }
        return response.text();
    }).then((data) => {
        console.log(data);
        location.reload();
    }).catch(error => {
        console.log('Error: ', error);
    }).finally(() => {
        console.log("reply delete finally");
    });

}


window.onload = function () {

    replyList();

    /** 문의하기 **/
    let qnaBtn = document.getElementById('qna_btn');
    if (qnaBtn) {
        qnaBtn.onclick = function () {
            location.href = '/writeQna';
        }
    } else {
        console.error('Element with id "qna_btn" not found.');
    }

    let distinctUpdate = '';
    let distinctDelete = '';
    /** 글 수정 **/
    let modBtn = document.getElementById('mod_btn');
    let kind = document.getElementById('kind').value;
    if (String(kind) === "e") {
        distinctUpdate = '/boardUpdate/'
        distinctDelete = '/boardDelete/'
    } else {
        distinctUpdate = '/comboardUpdate/'
        distinctDelete = '/com/boardDelete/'
    }

    if (modBtn) {
        modBtn.onclick = function () {
            location.href = distinctUpdate + num;
        }
    } else {
        console.error('Element with id "mod_btn" not found.');
    }

    /** 글 삭제 **/
    let boardDelBtn = document.getElementById('boardDel_btn');
    if (boardDelBtn) {
        boardDelBtn.onclick = function () {
            if (confirm('정말 삭제할까요?')) {
                location.href = distinctDelete + num;
            }
        }
    } else {
        console.error('Element with id "boardDel_btn" not found.');
    }

    /** 댓글 입력 **/
    document.getElementById('append').onclick = function () {
        let sessionId = document.getElementById('sessionId');
        let frcontent = document.getElementById('rep_content');
        let rep = {
            'uid'        : sessionId.value
            , 'frcontent': frcontent.value
            , 'fid'      : num
        };

        fetch('/repinsert', {
            method   : 'POST'
            , headers: {
                'Content-Type': 'application/json;utf-8'
                , 'Accept'    : 'application/json'
            }, body  : JSON.stringify(rep)
        }).then((response) => {
            if (response.status === 200) {
                console.log("Insert success");
                return response.json();
            } else if (response.status === 403) {
                console.log("403error");
                return response.json();
            }
            return response.json();
        }).then((data) => {
            document.querySelector('#replyList').replaceChildren("");
            data.forEach(item => {
                let ele_p1 = document.createElement('p');
                let ele_p2 = document.createElement('p');
                let ele_ul = document.createElement('ul');
                let ele_li = document.createElement('li');
                let nickname = document.createTextNode('[' + item.nickname + ']');
                let content = document.createTextNode(' ' + item.frcontent);
                let uid = item.uid;
                let frid = item.frid;

                ele_p1.appendChild(nickname);
                ele_p2.appendChild(content);
                ele_p1.appendChild(ele_p2);
                ele_li.appendChild(ele_p1);
                ele_ul.appendChild(ele_li);

                /** 삭제 버튼 추가 **/
                let sessionId = document.getElementById('sessionId').value;
                if (String(sessionId) === String(uid) || String(sessionId) === '100') {
                    let deleteButton = document.createElement('button');
                    deleteButton.textContent = '삭제';
                    deleteButton.id = 'del_btn'
                    deleteButton.onclick = function () {
                        if (confirm('정말 삭제할까요?')) {
                            deleteRep(frid);
                        }
                    };
                    ele_li.appendChild(deleteButton);
                }
                document.getElementById('replyList').appendChild(ele_ul);
            })
            document.getElementById('rep_content').value = '';

            /** 댓글 수 fetch 처리 **/
            fetch('/repcount/' + num, {
                method: 'GET'
            }).then((response) => {
                document.querySelector('#reply_count').replaceChildren("");
                if (!response.ok) {
                    throw new Error('Failed to reply count');
                }
                return response.text();
            }).then((data) => {

                let ele_p = document.createElement('p');
                let repCount = document.createTextNode('댓글수: ' + data);

                ele_p.appendChild(repCount);

                document.getElementById('reply_count').appendChild(ele_p);
            }).catch(error => {
                console.log('Error: ', error);
            }).finally(() => {
                console.log("reply count finally");
            });

        }).catch(error => {
            console.log('Error: ', error);
        }).finally(() => {
            console.log("reply insert finally");
        });


    }


    /** 댓글 입력 엔터키 동작 **/
    document.querySelector('#rep_content').addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('#append').onclick();
        }
    });

    /** 좋아요 **/

    // let hasLiked = localStorage.getItem('hasLiked_' + sessionId + '_' + fid) === 'true'; // 로컬 스토리지에서 좋아요 상태 불러오기

    // /** 좋아요 여부 확인 **/
    // updateButton();
    //
    // function updateButton() {
    //     if (hasLiked) {
    //         likeBtn.innerText = '취소';
    //         likeBtn.style.border = '2px solid pink';
    //         likeImg.style.backgroundImage = 'url("../../img/freeboard/heart.png")';
    //     } else {
    //         likeBtn.innerText = '좋아요';
    //         likeBtn.style.border = '2px solid silver';
    //         likeImg.style.backgroundImage = 'url("../../img/freeboard/unheart.png")';
    //     }
    // }
    toggleLike();

    function toggleLike() {
        const likeBtn = document.getElementById('like_btn');
        const likeImg = document.getElementById('like_img');
        const sessionId = document.getElementById('sessionId').value;
        const fid = document.getElementById('fid').value;

        const dto = {'uid': sessionId, 'fid': fid};

        fetch('/love', {
            method : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body   : JSON.stringify(dto)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update Love');
                }
                return response.json(); // Parse response as JSON
            })
            .then(data => {
                // 'data' will now be a boolean value (true or false)
                if (data) {
                    likeBtn.innerText = '취소';
                    likeBtn.style.border = '2px solid pink';
                    likeImg.style.backgroundImage = 'url("../../img/freeboard/heart.png")';
                } else {
                    likeBtn.innerText = '좋아요';
                    likeBtn.style.border = '2px solid silver';
                    likeImg.style.backgroundImage = 'url("../../img/freeboard/unheart.png")';
                }
            })
            .catch(error => {
                console.error('Error updating Love:', error);
            });

    }

    // likeBtn.addEventListener('click', actionLike);
    document.getElementById('like_btn').onclick = function() {
        const likeBtn = document.getElementById('like_btn');
        const likeImg = document.getElementById('like_img');
        const sessionId = document.getElementById('sessionId').value;
        const fid = document.getElementById('fid').value;

        const dto = {'uid': sessionId, 'fid': fid};

        fetch('/loveupdate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dto)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update Love');
                }
                // Check if response is empty
                if (response.status === 204) { // No Content
                    return {}; // Return an empty object
                } else {
                    return response.json(); // Parse JSON if response is not empty
                }
            })
            .then(data => {
                if (data.length > 0) {
                    // Handle response data accordingly
                    if (data) {
                        likeBtn.innerText = '취소';
                        likeBtn.style.border = '2px solid pink';
                        likeImg.style.backgroundImage = 'url("../../img/freeboard/heart.png")';
                    } else {
                        likeBtn.innerText = '좋아요';
                        likeBtn.style.border = '2px solid silver';
                        likeImg.style.backgroundImage = 'url("../../img/freeboard/unheart.png")';
                    }

                } else {
                    console.log('Empty response received');
                }
            })
            .catch(error => {
                console.error('Error updating Love:', error);
            });
        window.location.reload();
    };
};

