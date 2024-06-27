let num = '';

const init = function (data) {
    num = data;
}

/** 댓글 리스트 **/
const replyList = function () {
    fetch('/replist/' + num, {
        method   : 'GET'
        , headers: {
            'Accept': 'application/json'
        }
    }).then((response) => {
        if (!response.ok) {
            throw new Error("Fail to load reply list");
        }
        return response.json();
    }).then((data) => {
        data.forEach(item => {
            let ele_span = document.createElement('span');
            let ele_ul = document.createElement('ul');
            let ele_li = document.createElement('li');
            let nickname = document.createTextNode('['+item.nickname + ']');
            let content = document.createTextNode(' '+item.frcontent);
            let uid = item.uid;
            let frid = item.frid;


            ele_span.appendChild(nickname);
            ele_span.appendChild(content);
            ele_li.appendChild(ele_span);
            ele_ul.appendChild(ele_li);

            /** 삭제 버튼 추가 **/
            let sessionId = document.getElementById('sessionId').value;
            if(String(sessionId) === String(uid) || String(sessionId) === '100') {
                let deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.id = 'del_btn'
                deleteButton.onclick = function () {
                    confirm('정말 삭제할까요?');
                    deleteRep(frid);
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
};


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

    /** 글 수정 **/
    let modBtn = document.getElementById('mod_btn');
    if (modBtn) {
        modBtn.onclick = function () {
            location.href = '/boardUpdate/' + num;
        }
    } else {
        console.error('Element with id "mod_btn" not found.');
    }

    /** 글 삭제 **/
    let boardDelBtn = document.getElementById('boardDel_btn');
    if (boardDelBtn) {
        boardDelBtn.onclick = function () {
            location.href = '/boardDelete/' + num;
        }
    } else {
        console.error('Element with id "boardDel_btn" not found.');
    }

    /** 댓글 입력 **/
    document.getElementById('append').onclick = function () {
        let sessionId = document.getElementById('sessionId');
        let frcontent = document.getElementById('rep_content');
        let rep = {
            'uid' : sessionId.value
            , 'frcontent' : frcontent.value
            , 'fid': num
        };

        fetch('/repinsert', {
            method   : 'POST'
            , headers: {
                'Content-Type': 'application/json;utf-8'
                , 'Accept'    : 'application/json'
            }, body : JSON.stringify(rep)
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
            data.forEach(item =>{
                let ele_ul = document.createElement('ul');
                let ele_li = document.createElement('li');
                let nickname = document.createTextNode(item.nickname);
                let content = document.createTextNode(item.frcontent);

                ele_li.appendChild(nickname);
                ele_li.appendChild(content);
                ele_ul.appendChild(ele_li);
                document.getElementById('replyList').appendChild(ele_ul);
                location.reload();
            })
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
    const likeBtn = document.getElementById('like_btn');
    const likeImg = document.getElementById('like_img');
    const sessionId = document.getElementById('sessionId').value;
    const fid = document.getElementById('fid').value;
    let hasLiked = localStorage.getItem('hasLiked_' + sessionId + '_' + fid) === 'true'; // 로컬 스토리지에서 좋아요 상태 불러오기

    /** 좋아요 여부 확인 **/
    updateButton();

    function updateButton() {
        if (hasLiked) {
            likeBtn.innerText = '취소';
            likeBtn.style.backgroundColor = '#C6C6C6';
            likeImg.style.backgroundImage = 'url("../../img/freeboard/unheart.png")';
        } else {
            likeBtn.innerText = '좋아요';
            likeBtn.style.backgroundColor = '#B6BEED';
            likeImg.style.backgroundImage ='url("../../img/freeboard/heart.png")';
        }
    }

    function toggleLike() {
        const dto = { 'uid': sessionId, 'fid': fid };

        fetch('/love', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dto)
        })
            .then(response => {
                if (response.ok) {
                    if (!hasLiked) {
                        console.log('Like successfully');
                        hasLiked = true;
                    } else {
                        console.log('Like Cancel successfully');
                        hasLiked = false;
                    }
                    localStorage.setItem('hasLiked_' + sessionId + '_' + fid, hasLiked); // 좋아요 상태 로컬 스토리지에 저장
                    updateButton();
                } else {
                    console.error('Failed to update Love');
                }
            })
            .catch(error => {
                console.error('Error updating Love:', error);
            });
    }

    likeBtn.addEventListener('click', toggleLike);
};

