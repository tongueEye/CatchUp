let num='';
const init = function (data) {
    num = data;
    console.log(num);
}


const init_json  =function () {
    fetch("/repcontent/"+num, {
        method: 'GET'
        , headers: {
            'Accept': 'application/json'
        }
    }).then(response=>{
        if (!response.ok)
            throw new Error('not load')
        return response.json();
    }).then((data)=>{
        data.forEach(item=>{
            let ele_div=document.createElement('div');

            let ele_h4=document.createElement('h4');
            let ele_txt=document.createTextNode(item);

            ele_h4.appendChild(ele_txt);

            let repText = ele_h4.textContent.toString()
            if(repText!=="null" && repText!==""){
                document.getElementById('rep_box').appendChild(ele_div);
                document.getElementById('rep_box').appendChild(ele_h4);
            }

        })


    }).catch(error=>{
        console.log(error)
    }).finally(()=>{
        console.log('finally');
    })
}


window.onload=function () {
    init_json();

    document.getElementById('rep_btn').addEventListener('click', function () {
        const repContent = document.getElementById('rep_content').value;

        /*
        if (!repContent.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }*/

        fetch("/addrep", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                iid: num,
                repContent: repContent
            })
        }).then(response => {
            if (!response.ok) throw new Error('Failed to post comment');
            return response.json();
        }).then((data) => {

            let repBox = document.getElementById('rep_box');
            repBox.innerHTML = '';

            if (!repContent.trim()){
                repBox.style="display:none";
            } else {
                repBox.style="display:block";
            }

            let ele_div = document.createElement('div');
            let ele_h4 = document.createElement('h4');
            let ele_txt = document.createTextNode(data.repContent);
            ele_h4.appendChild(ele_txt);

            document.getElementById('rep_box').appendChild(ele_div);
            document.getElementById('rep_box').appendChild(ele_h4);
            document.getElementById('rep_content').value = ''; // 입력창 초기화

        }).catch(error => {
            console.log(error);
        });
    });
}