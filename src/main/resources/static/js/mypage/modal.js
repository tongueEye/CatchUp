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
let profile = document.querySelector('.top-profile');
let modalImg = document.getElementById('modal-img');

let uploadedFileKey = ''; // 전역 변수로 선언하거나, saveButton 이벤트 리스너에서 접근 가능한 범위에 선언합니다.

profileInput.onchange = function uploadImageFile() {
    const fileField = document.getElementById('profile');
    let formData = new FormData();
    formData.append('multipartFiles', fileField.files[0]);

    fetch('/file/uploadImageFile/profile', {
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
                uploadedFileKey = 'https://kdt-java5-2.s3.ap-northeast-2.amazonaws.com/' + encodeURIComponent(imageUrl.replace('%2F', '/')); // 이미지 URL을 전역 변수에 저장
                imageUrl = 'https://kdt-java5-2.s3.ap-northeast-2.amazonaws.com/' + encodeURIComponent(imageUrl.replace('%2F', '/'));

                /* 모달 부분 */
                modalImg.value = '';
                modalImg.src = uploadedFileKey
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

            /* profile_top 애들 */
            nickname.value = '';
            nickname.innerText = newNickname;
            profile.src = '';
            profile.src = uploadedFileKey;
            modal.classList.add('hidden');
        })
        .catch(error => {
            console.error('에러 에러 에러 에러 에러 ', error);
        });
});
