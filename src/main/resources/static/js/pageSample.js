// 사이드바 토글버튼
    document.getElementById('sidebar-action-icon').addEventListener('click', function() {
        document.querySelector('body').classList.toggle('collapsed');
        var sidebarNavText = document.querySelectorAll('.sidebar-nav-text');
        sidebarNavText.forEach(function(element) {
            if (element.style.display === 'none') {
                element.style.display = 'inline';
            } else {
                element.style.display = 'none';
            }
        });
    });

    // 사이드바 결제목록 토글버튼
    document.getElementById('payList-toggle').addEventListener('click', function() {
        // payList-nav 클래스를 가진 요소들을 가져옴
        var payListNavs = document.querySelectorAll('.payList-nav');

        // 각 payList-nav 요소에 대해 hidden 클래스를 추가/제거하여 페이드 인/아웃 처리
        payListNavs.forEach(function(payListNav) {
            payListNav.classList.toggle('hidden');
            payListNav.classList.toggle('visible');
        });
    });


//    // 결제금액 총합
//    let autoPayment = 0;
//    let productList = /** [[${productList}]]; */
//    for (let product of productList) {
//      autoPayment += parseInt(product.amount); // 각 항목의 amount 를 정수형으로 변환하여 더함
//    }
//    console.log(autoPayment);
//    var formattedAutoPayment = numberWithCommas(autoPayment);
//    document.getElementById("autoPayment").value = formattedAutoPayment;


    /**
    * @method : numberWithCommas
    * @desc : 금액 천단위로 콤마
    * @author : LeeChanSin
    */
    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    /**
    * @method : removeCommasAndParseInt
    * @desc : 금액 천단위로 콤마 삭제후 정수로 반환
    * @author : LeeChanSin
    */
    function removeCommasAndParseInt(str) {
        return parseInt(str.replace(/,/g, ''), 10);
    }
    /**
    * @method : dateProcess
    * @desc : 닐찌 형식 변환 yyyy-MM-dd
    * @author : LeeChanSin
    */
    function dateProcess(date){
        return date.split('T')[0];
    }


   const titleRex = /^.{0,100}$/; // 제목 정규식 최대 100 글자 not null
   const addressRex = /^.{0,100}$/; // 주소 정규식 최대 100 글자 not null
   const memoRex = /^.{0,300}$/; // 메모 정규식 최대 300글자 null 가능

   const totalItemsRex2 = /^[0-9]*$/; // 수량 정규식 숫자만 입력 null 가능
   const priceRex2= /^[0-9]*$/; // 금액 정규식 숫자만 입력 null 가능
   // 이름 정규식 최대 16글자, 특수문자 및 숫자 입력 불가
   const nameRex = /^[^\d!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?]{0,16}$/;
   // 이메일 정규식 이메일 형식만 가능 not null
   //const emailRex = /^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
   const emailRex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
   const emailRex2 = /^[\s\S]{0,100}$/; // 100자 이내의 모든것
   // 비밀번호 정규식 13글자 영어+숫자+대문자+특수문자 not null
   const pwdRex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[A-Z])(?=.*[!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?]).{13,}$/;
   const phoneRex = /^[0-9]{11}$/; // 연락처 정규식 숫자만 11 글자만 가능
   const phoneRex2 = /^[\s\S]{0,100}$/; // 100자 이내의 모든것
   const proNameRex = /^[\p{L}\d\s]{0,100}$/u; // 상품명 정규식 특수기호 없이 최대 100글자
   const proNameRex2 = /^[\s\S]{0,100}$/;
   const autoDateRex = /^(0?[0-9]|1\d|2[0-8]|3[01])$/; // 자동결제일 정규식1 0~31까지의 숫자

    /**
        @function : __R()
        @desc : 유효성 체크 함수
        @author : 김성민
    */
    function titleR(text) {
        if(text == ""){
            alert("제목을 입력하세요.");
            return false;
        }
        if (!titleRex.test(text)) {
            alert("제목은 100글자까지 입력할 수 있습니다.");
            return false;
        }
        return true;
    }

    function addressR(text) {
        if(text == ""){
            alert("주소를 입력하세요.");
            return false;
        }
        if (!titleRex.test(text)) {
            alert("주소는 100글자까지만 입력 가능합니다.");
            return false;
        }
        return true;
    }

    function memoR(text){
        if(text == ""){
            alert("메모를 입력하세요.");
            return false;
        }
        if (!memoRex.test(text)) {
            alert("메모는 300자까지만 입력 가능합니다.");
            return false;
        }
        return true;
    }

    function autoMemoR(text){

            if (!memoRex.test(text)) {
                alert("메모는 300자까지만 입력 가능합니다.");
                return false;
            }
            return true;
        }

    function totalItemsR(text) {
        if(text == ""){
            alert("수량을 입력하세요.");
            return false;
        }
        if (!totalItemsRex2.test(text)) {
            alert("수량은 숫자만 입력 가능합니다.");
            return false;
        }
        return true;
    }
    function priceR(text) {

        if (!priceRex2.test(text)) {
            alert("금액(지불액)은 숫자만 입력 가능합니다.");
            return false;
        }
        return true;
    }

    function nameR(text) {
        if(text == ""){
            alert("이름을 입력하세요.");
            return false;
        }
        if (!nameRex.test(text)) {
            alert("이름은 16자만 입력 가능합니다.\n ※숫자,특수문자 미포함");
            return false;
        }
        return true;
    }

    function emailR(text) {
        if(text == ""){
            alert("이메일을 입력하세요.");
            return false;
        }
        if (!emailRex.test(text)) {
            alert("이메일은 이메일 형식으로만 입력 가능합니다.");
            return false;
        }
        return true;
    }

    function pwdR(text) {
        if(text == ""){
            alert("비밀번호를 입력하세요.");
            return false;
        }
        if (!pwdRex.test(text)) {
            alert("비밀번호는 13자의 영어+숫자+대문자(특수문자불가)만 입력 가능합니다.");
            return false;
        }
        return true;
    }

    function phoneR(text) {
        if(text == ""){
            alert("연락처를 입력하세요.");
            return false;
        }
        if (!phoneRex.test(text)) {
            alert("연락처는 11자의 숫자만 입력 가능합니다.");
            return false;
        }
        return true;
    }
    
    function proNameR(text) {
        if(text == ""){
            alert("상품명을 입력하세요.");
            return false;
        }
        if (!proNameRex.test(text)) {
            alert("상품명은 100글자(특수기호불가)까지만 입력 가능합니다.");
            return false;
        }
        return true;
    }




