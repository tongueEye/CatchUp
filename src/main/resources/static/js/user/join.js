/** 주소 api */
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

document.addEventListener('DOMContentLoaded', function() {
    /** 아이디 중복체크 */
    // 아이디 중복체크가 되기 전엔 가입하기 버튼 비활성화
    document.querySelectorAll('.join_btn')[0].disabled = true;
    let idvalue;
    document.getElementById('idCk').onclick = function () {
        idvalue = document.getElementById('id').value;
        console.log("Id to check: ", idvalue); // 디버깅용 로그 추가
        fetch("/IdCheck", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',  // 요청을 JSON으로 보내기 위해 설정
            },
            body: JSON.stringify({ id: idvalue })
        }).then(response => {
            if (!response.ok)
                throw new Error('noooooooooo');
            return response.json();
        }).then((data) => {
            // 중복체크 버튼 초기화
            // 기존 span 요소를 찾고, 있으면 제거
            let existingSpan = document.getElementById('id-check-span');
            if (existingSpan) {
                document.getElementById("check").removeChild(existingSpan);
            }
            // 새로운 span 요소를 생성하고 ID 부여
            let checkspan = document.createElement('span');
            checkspan.id = 'id-check-span';

            if (idvalue == '' || idvalue == null) { // 공백일때
                let spacetext = document.createTextNode('아이디를 입력해주세요.');
                checkspan.appendChild(spacetext);
                checkspan.style.color = "red";
            } else { // 입력 했을 때
                if (data == 0) { // 사용 가능한 아이디
                    let oktext = document.createTextNode('사용 가능한 아이디입니다.');
                    checkspan.appendChild(oktext);
                    checkspan.style.color = "green";

                    // 비밀번호 확인 상태를 체크하여 가입하기 버튼 활성화 결정
                    if (document.getElementById('password').value === document.getElementById('passwordCk').value) {
                        document.querySelectorAll('.join_btn')[0].disabled = false; // 가입하기 버튼 활성화
                    }
                } else { // 이미 존재하는 아이디
                    let alreadytext = document.createTextNode('이미 존재하는 아이디입니다.');
                    checkspan.appendChild(alreadytext);
                    checkspan.style.color = "red";

                    document.querySelectorAll('.join_btn')[0].disabled = true; // 가입하기 버튼 비활성화
                }
            }
            document.getElementById("check").appendChild(checkspan);
        }).catch(error => {
            console.log(error);
        }).finally(
            () => console.log('finally')
        )
    }

    /** 비밀번호 확인 */
    function passConfirm() {
        /* 비밀번호, 비밀번호 확인 입력창에 입력된 값을 비교해서 같다면 비밀번호 일치, 그렇지 않으면 불일치 라는 텍스트 출력.*/
        let password = document.getElementById('password'); // 비밀번호
        let passwordCk = document.getElementById('passwordCk'); // 비밀번호 확인
        let confirmMsg = document.getElementById('confirmMsg'); // 확인 메세지

        if (password.value == passwordCk.value) { // password 변수의 값과 passwordConfirm 변수의 값과 동일하다.
            confirmMsg.style.color = "green"; // span 태그의 ID(confirmMsg) 사용
            confirmMsg.innerHTML = "비밀번호 일치"; // innerHTML : HTML 내부에 추가적인 내용을 넣을 때 사용하는 것

            // 아이디 중복 체크 상태를 확인하여 가입하기 버튼 활성화 결정
            if (document.getElementById('id-check-span') && document.getElementById('id-check-span').style.color === "green") {
                document.querySelectorAll('.join_btn')[0].disabled = false; // 가입하기 버튼 활성화
            }
        } else {
            confirmMsg.style.color = "red";
            confirmMsg.innerHTML = "비밀번호 불일치";

            document.querySelectorAll('.join_btn')[0].disabled = true; // 가입하기 버튼 비활성화
        }
    }

    // passConfirm 함수를 비밀번호 확인 입력창에 연결 - 이거 없으면 확인 안해도 그냥 가입됨
    document.getElementById('passwordCk').onkeyup = passConfirm;
    document.getElementById('password').onkeyup = passConfirm;
});



/*

/!** 아이디 중복체크 *!/
//아이디 중복체크가 되기 전엔 가입하기 버튼 비활성화
document.querySelectorAll('.join_btn')[0].disabled=true;
let idvalue;
document.getElementById('idCk').onclick = function () {
    idvalue = document.getElementById('id').value;
    console.log("Id to check: ", idvalue); // 디버깅용 로그 추가
    fetch("/IdCheck", {
        method: "POST"
        , headers: {
            'Content-Type': 'application/json',  // 요청을 JSON으로 보내기 위해 설정
        },
        body: JSON.stringify({ id: idvalue})
    }).then(response => {
        if (!response.ok)
            throw new Error('noooooooooo');
        return response.json();
    }).then((data) => {
        // 중복체크 버튼 초기화
        // 기존 span 요소를 찾고, 있으면 제거
        let existingSpan = document.getElementById('id-check-span');
        if (existingSpan) {
            document.getElementById("check").removeChild(existingSpan);
        }
        // 새로운 span 요소를 생성하고 ID 부여
        let checkspan = document.createElement('span');
        checkspan.id = 'id-check-span';


        if(idvalue == '' || idvalue == null) { //공백일때
            let spacetext = document.createTextNode('아이디를 입력해주세요.');
            checkspan.appendChild(spacetext);
            checkspan.style.color = "red";
        }else { //입력 했을 때
            if (data == 0) { //사용 가능한 아이디
                let oktext = document.createTextNode('사용 가능한 아이디입니다.');
                checkspan.appendChild(oktext);
                checkspan.style.color= "green";

                // 비밀번호 확인 상태를 체크하여 가입하기 버튼 활성화 결정
                if (document.getElementById('password').value === document.getElementById('passwordCk').value) {
                    document.querySelectorAll('.join_btn')[0].disabled = false; // 가입하기 버튼 활성화
                }
            } else { // 이미 존재하는 아이디
                let alreadytext = document.createTextNode('이미 존재하는 아이디입니다.');
                checkspan.appendChild(alreadytext);
                checkspan.style.color= "red";

                document.querySelectorAll('.join_btn')[0].disabled=true; //가입하기 버튼 비활성화
            }
        }
        document.getElementById("check").appendChild(checkspan);
    }).catch(error => {
        console.log(error);
    }).finally(
        () => console.log('finally')
    )
}



/!** 비밀번호 확인 *!/
function passConfirm() {
    /!* 비밀번호, 비밀번호 확인 입력창에 입력된 값을 비교해서 같다면 비밀번호 일치, 그렇지 않으면 불일치 라는 텍스트 출력.*!/
    /!* document : 현재 문서를 의미함. 작성되고 있는 문서를 뜻함. *!/
    /!* getElementByID('아이디') : 아이디에 적힌 값을 가진 id의 value를 get을 해서 password 변수 넣기 *!/
    let password = document.getElementById('password');//비밀번호
    let passwordCk = document.getElementById('passwordCk');//비밀번호 확인
    let confirmMsg = document.getElementById('confirmMsg');//확인 메세지

    if(password.value == passwordCk.value){//password 변수의 값과 passwordConfirm 변수의 값과 동일하다.
        confirmMsg.style.color = "green";/!* span 태그의 ID(confirmMsg) 사용  *!/
        confirmMsg.innerHTML ="비밀번호 일치";/!* innerHTML : HTML 내부에 추가적인 내용을 넣을 때 사용하는 것. *!/

        // 아이디 중복 체크 상태를 확인하여 가입하기 버튼 활성화 결정
        if (document.getElementById('id-check-span') && document.getElementById('id-check-span').style.color === "green") {
            document.querySelectorAll('.join_btn')[0].disabled = false; // 가입하기 버튼 활성화
        }
    }else{
        confirmMsg.style.color = "red";
        confirmMsg.innerHTML ="비밀번호 불일치";

        document.querySelectorAll('.join_btn')[0].disabled=true; //가입하기 버튼 비활성

    }
}*/
