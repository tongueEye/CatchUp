// 하아아아아아아ㅏㅁㅁ 옮기자아ㅏㅏㅏ

/** 학교 검색하기 */
let school_name = document.getElementById('schoolList');
let school_input = document.getElementById('schoolInput');

let SDSCHULCODE = ''; // 학교코드 정보 담아오기
let sido = '';
let sido_result = document.getElementById('sido');

// 옵션 변경 이벤트 리스너 추가
sido_result.addEventListener('change', function() {
    sido = this.value;
});

function search(target) {
    let school = target.value

    fetch("https://open.neis.go.kr/hub/schoolInfo??KEY=7578fdd86a164ce4b0eebabfdbf51a5&Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE=" + sido +"&SCHUL_NM=" + school)
        .then(response => {
            if (response.status == 200) {
                console.log('호출완료');
                return response.json(); // 응답을 JSON 형식으로 파싱
            } else if (response.status == 403) {
                console.log("403 에러 발생");
                return response.json();
            }
        }).then(data => {
        console.log(data, ">>> data")
        school_name.innerHTML = ''; // 이게 있어야지 검색 결과 초기화 됨

        SDSCHULCODE = data.schoolInfo[1].row.map(school => school.SD_SCHUL_CODE);

        let SCHULNM = data.schoolInfo[1].row.map(school => school.SCHUL_NM); // 학교 이름 뽑아오기
        SCHULNM.forEach((a) => {
            console.log(a)
            let school_li= document.createElement('li');
            school_li.innerText = a;
            school_li.style.cursor = 'pointer'

            school_name.appendChild(school_li);

            // 클릭하면 input 창에 뜨도록 하기
            school_li.addEventListener('click', function () {
                school_input.value = a;
            })
        })

    }).catch(error => {
        console.error('Error:', error);
    });

}

/** 검색한 값 DB에 넘겨서 저장하기 => 저장해야 하는 것 1. 시도교육청코드(ATPT_OFCDC_SC_CODE) 2. 행정표준코드(SD_SCHUL_CODE) */
    // 급식일자는 무조건 오늘 날짜로 설정함 (주말이어도 일단 해) => MLSV_YMD
    // 시간 남으면 토, 일요일일 경우 월요일로 넘겨주는 작업 진행하기

let schoolResult = document.getElementById('schoolResult');
schoolResult.onclick = function uploadSchool() {

    // 시도 결과 받아오기
    let sido_search = sido_result.options[sido_result.selectedIndex].value;

    let school = school_input.value

    /*search(school)
    console.log(SDSCHULCODE, " >>>>>>>>>>>>>>> school");*/

    let schoolData = {
        schoolName: school,
        sidoCode : sido_search
        // sDCode : SDSCHULCODE
    };

    fetch("/api", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(schoolData)
    })
        .then(response => {
            if (response.status == 200) {
                console.log('호출완료');
                return response.json(); // 응답을 JSON 형식으로 파싱
            } else if (response.status == 403) {
                console.log("403 에러 발생");
                return response.json();
            }
        })
}

// 오늘 날짜 출력하기
function getToday(){
    let date = new Date();
    let year = date.getFullYear();
    let month = ("0" + (1 + date.getMonth())).slice(-2);
    let day = ("0" + date.getDate()).slice(-2);
    return year + month + day;
}


let bap_SDCode = [[${bap.sDCode}]];
let bap_sidocode = [[${bap.sidoCode}]];

console.log(bap_SDCode)
console.log(bap_sidocode)


/** 급식 불러오기 */
fetch("https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=7578fdd86a164ce4b0eebabfdbf51a5f&Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE=" + bap_sidocode + "&SD_SCHUL_CODE=" + bap_SDCode + "&MLSV_YMD=" + getToday())
    .then(response => {
        if (response.status == 200) {
            console.log('호출완료');
            return response.json(); // 응답을 JSON 형식으로 파싱
        } else if (response.status == 403) {
            console.log("403 에러 발생");
            return response.json();
        }
    }).then(data => {
    console.log(data, ">>> data")

    let today_bap = document.getElementById('today-bap'); // 최상단 div

    const meals = data.mealServiceDietInfo[1].row;

    let breakfast = "";
    let lunch = "";
    let dinner = "";

    meals.forEach(meal => {
        let mealType = meal.MMEAL_SC_NM; // 조식, 중식, 석식 구분

        // 메뉴에서 괄호 안의 내용과 숫자 및 마침표 제거
        let menuItems = meal.DDISH_NM.replace(/\([^)]*\)|\d+\.*\d*|\./g, '');


        // 각 구분에 맞게 변수에 추가
        if (mealType === "조식") {
            breakfast += menuItems + " ";
            if(breakfast.trim() === '') {
                breakfast += '조식이 없습니다.'
            }
            console.log(breakfast, ">>>>>>>>>>>>>>>>> breakfast ")
        } else if (mealType === "중식") {
            lunch += menuItems + " ";
        } else if (mealType === "석식") {
            dinner += menuItems + " ";
        }
    });

    // 화면에 출력할 부분
    let menuText = `<조식 메뉴> <br/> ${breakfast}<br/><br/>
                        <중식 메뉴> <br/> ${lunch}<br/><br/>
                        <석식 메뉴> <br/> ${dinner}`;


    // HTML에 추가
    let menuDiv = document.createElement('div');
    menuDiv.innerHTML = menuText;
    today_bap.appendChild(menuDiv);

}).catch(error => {
    console.error('Error:', error);
});