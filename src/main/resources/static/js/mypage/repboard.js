document.addEventListener("DOMContentLoaded", function() {
    const elements = document.querySelectorAll('.span-1');
    elements.forEach(function(element) {
        if (element.offsetWidth > 200) { // 예시로 너비가 200px 이상일 때 처리
            element.style.whiteSpace = 'nowrap';
            element.style.overflow = 'hidden';
            element.style.textOverflow = 'ellipsis';
            element.nextElementSibling.style.display = 'inline-block'; // .span-2 요소 옆에 표시
        }
    });
});
