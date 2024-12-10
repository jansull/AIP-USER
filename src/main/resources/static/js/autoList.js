$(function() {
    $("#startDt").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(selectedDate) {
            var endDate = $('#endDt');
            var startDate = $(this).datepicker('getDate');
            startDate.setDate(startDate.getDate() + 1);
            endDate.datepicker('option', 'minDate', startDate);
        }
    });

    $("#endDt").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(selectedDate) {
            var startDate = $('#startDt');
            var endDate = $(this).datepicker('getDate');
            endDate.setDate(endDate.getDate() - 1);
            startDate.datepicker('option', 'maxDate', endDate);
        }
    });
});

function toggleContent() {
    var icon = document.querySelector('.fas');
    icon.classList.toggle('rotate-up');

    var content = document.getElementById("expandableContent");
    content.classList.toggle("active"); // 내용의 확장 여부를 토글
}

function setDateRange(months) {
    var endDate = new Date();
    var startDate = new Date();
    startDate.setMonth(startDate.getMonth() - months);

    // 종료일을 계산된 값으로 설정
    $('#startDt').datepicker('setDate', startDate);

    // 시작일을 오늘 날짜로 설정
    $('#endDt').datepicker('setDate', endDate);

    // 모든 버튼에 대해 비활성화 클래스를 추가
    var buttons = document.querySelectorAll('.date-range-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // 선택된 기간에 해당하는 버튼에 active 클래스 추가
    var activeButton = document.querySelector('.date-range-button[data-months="' + months + '"]');
    if (activeButton) {
        activeButton.classList.add('active');
    }
}

function resetForm() {
    document.getElementById("form").reset();

    // 선택된 날짜 범위를 초기화합니다.
    var buttons = document.querySelectorAll('.date-range-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // 선택된 상태를 초기화하고 색상을 원래대로 변경합니다.
    var buttons = document.querySelectorAll('.status-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // 선택된 상태를 초기화하고 색상을 원래대로 변경합니다.
    var buttons = document.querySelectorAll('.transaction-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // 선택된 상태를 초기화하고 색상을 원래대로 변경합니다.
    var buttons = document.querySelectorAll('.PayType-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // 상태 값을 초기화합니다.
    document.getElementById("status").value = ""; // 상태 값을 빈 문자열로 설정
    document.getElementById("transactionStatus").value = ""; // 상태 값을 빈 문자열로 설정
    document.getElementById("payType").value = ""; // 상태 값을 빈 문자열로 설정
}

function setStatus(status) {
    document.getElementById('status').setAttribute('value', status);
}

function setTransactionStatus(status) {
    document.getElementById('transactionStatus').setAttribute('value', status);
}

function setPayType(type) {
    document.getElementById('payType').setAttribute('value', type);
}

function submitForm() {
    // 폼 요소 가져오기
    var form = document.getElementById("form");

    // 상세유형 입력 필드 가져오기
    var detailTypeInput = document.getElementById('status');

    // 상세유형 입력 값 가져오기
    var detailTypeValue = detailTypeInput.value.trim();

    // 상세유형이 비어있는지 확인
//    if (detailTypeValue === '') {
//        // 상세유형이 비어있으면 알림을 표시하고 제출을 중지
//        alert("상세유형을 입력하세요.");
//        return;
//    }

    // 상세유형이 입력되었으면 폼 제출
    form.submit();
}

function changePageSize() {
    var selectedSize = document.getElementById("pageSelect").value;
    var totalItems = parseInt('${payCount}'); // 전체 아이템 수 가져오기
    var totalPages = Math.ceil(totalItems / selectedSize); // 전체 페이지 수 계산

    var url = new URL(window.location.href);
    url.searchParams.set('size', selectedSize);

    // 현재 페이지가 유효한 경우에만 새로운 페이지 번호를 계산하여 설정
    var currentPage = parseInt(getParameterByName('page')); // 현재 페이지 번호 가져오기
    if (!isNaN(currentPage) && currentPage >= 0) {
        var newPage = Math.min(currentPage, totalPages - 1); // 새로운 페이지 번호 계산
        url.searchParams.set('page', newPage);
    }

    window.location.href = url.toString();
}

// URL에서 특정 파라미터 값을 가져오는 함수
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

document.getElementById("searchInput").addEventListener("keypress", function(event) {
    if (event.keyCode === 13) { // Enter 키의 keyCode는 13입니다.
        event.preventDefault(); // 기본 동작을 막습니다.
        document.getElementById("searchForm").submit(); // 폼을 제출합니다.
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // 상태 버튼 그룹
    var statusButtons = document.querySelectorAll('.status-button');
    statusButtons.forEach(function(button) {
        button.addEventListener("click", function() {
            var status = button.innerText;
            console.log("상태:", status);
            // 클릭된 버튼에만 .active 클래스 추가
            statusButtons.forEach(function(btn) {
                btn.classList.remove('active');
            });
            button.classList.add('active');
        });
    });

    // 거래상태 버튼 그룹
    var transactionButtons = document.querySelectorAll('.transaction-button');
    transactionButtons.forEach(function(button) {
        button.addEventListener("click", function() {
            var status = button.innerText;
            console.log("거래상태:", status);
            // 클릭된 버튼에만 .active 클래스 추가
            transactionButtons.forEach(function(btn) {
                btn.classList.remove('active');
            });
            button.classList.add('active');
        });
    });

    // 결제구분 버튼 그룹
    var payTypeButtons = document.querySelectorAll('.PayType-button');
    payTypeButtons.forEach(function(button) {
        button.addEventListener("click", function() {
            var status = button.innerText;
            console.log("결제구분:", status);
            // 클릭된 버튼에만 .active 클래스 추가
            payTypeButtons.forEach(function(btn) {
                btn.classList.remove('active');
            });
            button.classList.add('active');
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var autoStatusCheckbox = document.getElementById('autoStatusOnly');

    // 로컬 스토리지에서 자동결제 상태 체크 여부 가져오기
    var autoStatusChecked = localStorage.getItem('autoStatusChecked');

    // 로컬 스토리지에 자동결제 상태 체크 여부가 저장되어 있다면 체크박스 상태 업데이트
    if (autoStatusChecked !== null) {
        autoStatusCheckbox.checked = autoStatusChecked === 'true';
    }

    // 자동결제 상태만 보기 체크박스의 상태 변화를 감지하여 폼을 제출하는 함수
    function submitFormOnChange() {
        var autoStatusForm = document.getElementById('autoStatusForm');
        autoStatusForm.submit(); // 폼 제출
    }

    // 자동결제 상태만 보기 체크박스의 상태 변화 이벤트 핸들러 등록
    autoStatusCheckbox.addEventListener('change', function() {
        // 체크박스가 체크되어 있는지 확인하여 true 또는 false로 변환
        var autoStatusChecked = autoStatusCheckbox.checked;

        // 로컬 스토리지에 자동결제 상태 체크 여부 저장
        localStorage.setItem('autoStatusChecked', autoStatusChecked);

        // 폼을 제출
        submitFormOnChange();
    });
});


