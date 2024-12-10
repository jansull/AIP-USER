// 현재 월을 가져오는 함수
function getCurrentMonth() {
    var today = new Date();
    var currentMonth = today.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
    return currentMonth;
}

// N월 값을 업데이트하는 함수
function updateMonthText() {
    var currentMonth = getCurrentMonth();
    var nMonthDivs = document.querySelectorAll('.n-month'); // N월을 표시하는 div 요소들을 선택

    // 각 N월 div에 현재 월 값을 반영
    nMonthDivs.forEach(function(div) {
        div.textContent = currentMonth + "월";
    });
}

// 페이지가 로드될 때 N월 값을 업데이트
window.onload = function() {
    updateMonthText();
};

function getFirstAndLastDateOfMonth() {
    var today = new Date();
    var year = today.getFullYear();
    var month = today.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌

    // 해당 월의 첫째 날을 계산
    var firstDayOfMonth = new Date(year, month - 1, 1);

    // 해당 월의 마지막 날을 계산
    var lastDayOfMonth = new Date(year, month, 0);

    // 첫째 날과 마지막 날을 YYYY-MM-DD 형식의 문자열로 반환
    return {
        firstDay: formatDate(firstDayOfMonth),
        lastDay: formatDate(lastDayOfMonth)
    };
}

// 날짜를 YYYY-MM-DD 형식의 문자열로 포맷팅하는 함수
function formatDate(date) {
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 1을 더해줌
    var day = String(date.getDate()).padStart(2, '0');
    return year + '-' + month + '-' + day;
}

// 현재 월의 첫째 날과 마지막 날로 설정된 링크로 이동하는 함수
function redirectToCurrentMonthLink() {
    var dates = getFirstAndLastDateOfMonth();
    var link = "/search?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=&docNumber=&customerName=&email=&continue";
    location.href = link; // 페이지 이동
}

function redirectToCurrentMonthSuccess() {
     var dates = getFirstAndLastDateOfMonth();
     var link2 = "/search?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=paid&docNumber=&customerName=&email=&continue";
     location.href = link2; // 페이지 이동
}


function autoError() {
     var dates = getFirstAndLastDateOfMonth();
     var link2 = "/autoSearch?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=&docNumber=&customerName=&email=&phoneNumber=&transactionStatus=error&continue";
     location.href = link2; // 페이지 이동
}
function autoSuccess() {
     var dates = getFirstAndLastDateOfMonth();
     var link2 = "/autoSearch?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=&docNumber=&customerName=&email=&phoneNumber=&transactionStatus=paid&continue";
     location.href = link2; // 페이지 이동
}
function autoStop() {
     var dates = getFirstAndLastDateOfMonth();
     var link2 = "/autoSearch?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=&docNumber=&customerName=&email=&phoneNumber=&transactionStatus=stop&continue";
     location.href = link2; // 페이지 이동
}
function autoAll() {
     var dates = getFirstAndLastDateOfMonth();
     var link2 = "/autoSearch?startDt=" + dates.firstDay + "&endDt=" + dates.lastDay + "&status=&docNumber=&customerName=&email=&phoneNumber=&transactionStatus=&continue";
     location.href = link2; // 페이지 이동
}