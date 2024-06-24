window.addEventListener('scroll', function() {
    // 각 section_box 요소들에 대해 반복
    let sectionBoxes = document.querySelectorAll('.section_box');
    sectionBoxes.forEach(function(sectionBox) {
        // section_box 요소의 상단 위치 계산
        let sectionBoxTop = sectionBox.getBoundingClientRect().top;
        // 윈도우의 높이
        let windowHeight = window.innerHeight;
        // 특정 위치까지 스크롤됐을 때 클래스 추가
        if (sectionBoxTop < windowHeight * 0.75) {
            sectionBox.classList.add('show');
        }
    });
});
