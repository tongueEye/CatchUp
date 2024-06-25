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
            let ele_li = document.createElement('li');
            let nickname = document.createTextNode(item.nickname);
            let profile = document.createTextNode(item.profile);
            let content = document.createTextNode(item.frcontent);
            let c_date = document.createTextNode(item.frCreateDate);
            let u_date = document.createTextNode(item.frUpdateDate);
            let frid = item.frid;

            ele_li.appendChild(nickname);
            ele_li.appendChild(profile);
            ele_li.appendChild(content);
            ele_li.appendChild(c_date);
            ele_li.appendChild(u_date);

            let deleteButton = document.createElement('button');
            deleteButton.textContent = '삭제';
            deleteButton.id = 'del_btn'
            deleteButton.onclick = function() {
                deleteRep(frid);
            };
            ele_li.appendChild(deleteButton);

            document.getElementById('replyList').appendChild(ele_li);

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
                let ele_li = document.createElement('li');
                let profile = document.createTextNode(item.profile);
                let nickname = document.createTextNode(item.nickname);
                let content = document.createTextNode(item.frcontent);
                let c_date = document.createTextNode(item.frCreateDate);
                let u_date = document.createTextNode(item.frUpdateDate);

                ele_li.appendChild(profile);
                ele_li.appendChild(nickname);
                ele_li.appendChild(content);
                ele_li.appendChild(c_date);
                ele_li.appendChild(u_date);
                document.getElementById('replyList').appendChild(ele_li);
                location.reload();
            })
        }).catch(error => {
            console.log('Error: ', error);
        }).finally(() => {
            console.log("reply insert finally");
        });
    }
}
