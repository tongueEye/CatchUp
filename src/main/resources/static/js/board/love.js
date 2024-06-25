// window.onload = function() {
//     const loveBtn = document.getElementById('love_btn');
//     const sessionId = document.getElementById('sessionId').value;
//     const fid = document.getElementById('fid').value;
//     let hasLiked = false;
//
//     function toggleLike() {
//         const dto = { 'uid': sessionId
//             , 'fid': fid
//         };
//
//         if (!hasLiked) {
//             fetch('/love', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json'
//                 },
//                 body: JSON.stringify(dto)
//             })
//                 .then(response => {
//                     if (response.ok) {
//                         console.log('Recommendation inserted successfully');
//                         hasLiked = true;
//                         loveBtn.innerText = '취소';
//                     } else {
//                         console.error('Failed to insert recommendation');
//                     }
//                 })
//                 .catch(error => {
//                     console.error('Error inserting recommendation:', error);
//                 });
//         } else {
//             fetch('/love', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json'
//                 },
//                 body: JSON.stringify(dto)
//             })
//                 .then(response => {
//                     if (response.ok) {
//                         console.log('Recommendation deleted successfully');
//                         hasLiked = false;
//                         loveBtn.innerText = '좋아요';
//                     } else {
//                         console.error('Failed to delete recommendation');
//                     }
//                 })
//                 .catch(error => {
//                     console.error('Error deleting recommendation:', error);
//                 });
//         }
//     }
//
//     loveBtn.addEventListener('click', toggleLike);
// };

window.onload = function() {
    const loveBtn = document.getElementById('love_btn');
    const sessionId = document.getElementById('sessionId').value;
    const fid = document.getElementById('fid').value;
    let hasLiked = localStorage.getItem('hasLiked_' + sessionId + '_' + fid) === 'true'; // 로컬 스토리지에서 좋아요 상태 불러오기

    updateButton();

    function updateButton() {
        if (hasLiked) {
            loveBtn.innerText = '취소';
        } else {
            loveBtn.innerText = '좋아요';
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
                        console.log('Recommendation inserted successfully');
                        hasLiked = true;
                    } else {
                        console.log('Recommendation deleted successfully');
                        hasLiked = false;
                    }
                    localStorage.setItem('hasLiked_' + sessionId + '_' + fid, hasLiked); // 좋아요 상태 로컬 스토리지에 저장
                    updateButton();
                } else {
                    console.error('Failed to update recommendation');
                }
            })
            .catch(error => {
                console.error('Error updating recommendation:', error);
            });
    }

    loveBtn.addEventListener('click', toggleLike);
};
