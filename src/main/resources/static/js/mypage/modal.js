/** 모달창 이벤트 관련 */
let modalOpenButton = document.getElementById('modalOpenButton');
let modal = document.getElementById('modalContainer');
let modalCloseButton = document.querySelector('.modalClose');

modalCloseButton.addEventListener('click', () => {
    modal.classList.add('hidden');
});

modalOpenButton.addEventListener('click', () => {
    modal.classList.remove('hidden');
});

/** 모달창에서 프로필 사진 / 닉네임 바꾸는 것 관련 */
let nicknameInput = document.getElementById('nickname'); // 닉네임 input
let profileInput = document.getElementById('profile'); // 프로필사진 input

let saveButton = document.querySelector('.modal-li-6 button[type="submit"]');// save 버튼
let nickname = document.getElementById('top-nickname'); // html에서 바꾸려고
let uploadedFileKey = ''; // 전역 변수로 선언하거나, saveButton 이벤트 리스너에서 접근 가능한 범위에 선언합니다.

profileInput.onchange = function uploadImageFile() {
    const fileField = document.getElementById('profile');
    let formData = new FormData();
    formData.append('multipartFiles', fileField.files[0]);

    fetch('/file/uploadImageFile', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('업로드 실패');
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            if (data != null) {
                let imageUrl = data[0];
                uploadedFileKey = encodeURIComponent(imageUrl); // 이미지 URL을 전역 변수에 저장
                return imageUrl;
            } else {
                console.error('파일 업로드가 안 됨 ', data);
            }
        })
        .catch(error => {
            console.error('파일 업로드 실패함 왜 ㅠㅠ ??', error);
        });
}


saveButton.addEventListener('click', (event) => {
    event.preventDefault();

    let newNickname = nicknameInput.value;

    let updatedProfile = {
        nickname: newNickname,
        profile: uploadedFileKey
    };

    console.log(uploadedFileKey) // 새로운 값 들어옴 => 이제 이걸 db 에만 넣어주면 된다.

    fetch('/profileupdate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(updatedProfile)
    })
        .then(response => {
            if (response.status == 200) {
                console.log('저장완료');
                return response.text();
            } else if (response.status == 403) {
                console.log("403 에러 발생");
                return response.text();
            }
        })
        .then(data => {
            console.log('등록됨!!! 바뀜!! ', data);
            nickname.value = '';
            nickname.innerText = newNickname;
            modal.classList.add('hidden');
        })
        .catch(error => {
            console.error('에러 에러 에러 에러 에러 ', error);
        });
});


/*
saveButton.addEventListener('click', (event) => {
    // 기본 폼 제출 동작 막기
    event.preventDefault();

    // 수정된 닉네임 값 가져오기
    let newNickname = nicknameInput.value;

    console.log('nickname' + newNickname);
    console.log('profile >> ' + newImg)

    // 닉네임 정보를 담은 객체 생성
    let updatedProfile = {
        nickname: newNickname,
        profile : newImg
    };

    fetch('/profileupdate', {
        method: 'POST',
        headers: {
            'Content-Type' : 'application/json;charset=utf-8'
            , 'Accept' : 'application/json'
        },
        body: JSON.stringify(updatedProfile)
    })
        .then(response => {
            if(response.status == 200) {
                console.log('저장완료');
                return response.text();
            } else if(response.status == 403) {
                console.log("403 에러 발생")
                return response.text();
            }
        })
        .then(data => {
            // 업데이트 성공함?
            console.log('등록됨!!! 바뀜!! ', data);
            // 모달창 닫아주면서 데이터 업데이트 해야 함
            nickname.value = ''; // 일단 빈 공간으로 만들어주고
            nickname.innerText = newNickname;
            modal.classList.add('hidden');
        })
        .catch(error => {
            console.error('에러 에러 에러 에러 에러 ', error);
        });
});



profileInput.onchange = function uploadImageFile() {
    const fileField = document.getElementById('profile');
    let formData = new FormData();
    formData.append('multipartFiles', fileField.files[0]);

    fetch('/file/uploadImageFile', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('업로드 실패');
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            if (data != null) {
                let imageUrl = data[0];
                uploadedFileKey = encodeURIComponent(imageUrl) // S3에 업로드된 파일의 키 값 저장
                return imageUrl;
            } else {
                console.error('파일 업로드가 안 됨 ', data);
            }
        })
        .catch(error => {
            console.error('파일 업로드 실패함 왜 ㅠㅠ ??', error);
        });
}
*/
