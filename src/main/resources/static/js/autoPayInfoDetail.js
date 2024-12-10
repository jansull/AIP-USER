// 편집버튼 클릭
let isEditing = false;

/**
* @method : toggleEdit
* @desc : 편집모드 전환
* @author : LeeChanSin
*/
function toggleEdit() {

    if (!isEditing) {
        // 편집 모드
        // 클래스 요소들의 disabled 속성 설정
        const inputs = document.querySelectorAll('.auto-main .editable');
        inputs.forEach(input => {
            input.disabled = false; // 편집 모드에서는 모든 input 활성화
        });

        // 편집 버튼 텍스트 변경
        const editButton = document.querySelector('.auto-header button:nth-of-type(1)');
        editButton.textContent = '저장';


        isEditing = true;
    } else {
        // 저장 모드
        const result = confirm("수정하시겠습니까?");

        // 클래스 요소들의 disabled 속성 설정
        const inputs = document.querySelectorAll('.auto-main .editable');
        inputs.forEach(input => {
            input.disabled = true; // 저장 모드에서는 모든 input 비활성화
        });

        // 편집 버튼 텍스트 변경
        const editButton = document.querySelector('.auto-header button:nth-of-type(1)');
        editButton.textContent = '편집';


        isEditing = false;
        if (!result) {
            alert("취소되었습니다.");
            location.reload(true);
            return;

        }
        let f = document.getElementById("auto-form");
        let nextTime = formatDateTime(f.paymentNextTime.value);
        let param = {
            customerId: f.customerId.value,
            customerName: f.customerName.value,
            customerEmail: f.customerEmail.value,
            customerPhone: f.customerPhone.value,
            customerAddress: f.customerAddress.value,
            paymentId: f.paymentId.value,
            paymentMemo : f.paymentMemo.value,
            paymentNextTime: nextTime,
            autoPaymentId: f.autoPaymentId.value,
            autoOrBasic: false
        }

        if(!nameR(param.customerName)){
            return;
        }
        if(!emailR(param.customerEmail)){
            return;
        }
        if(!phoneR(param.customerPhone)){
            return;
        }
        if(!addressR(param.customerAddress)){
            return;
        }
        if(!autoMemoR(param.paymentMemo)){
            return;
        }


        fetch('/payDetailEdit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => {
            if (response.ok) {
                // 요청이 성공적으로 처리되었을 때의 동작
                alert("수정되었습니다.");
                location.reload(true);
            } else {
                // 요청이 실패했을 때의 동작
                alert("실패했습니다.");

            }
        }).catch(error => {
            console.error('Error:', error);
            alert("알수없는 에러.");
        });


    }
}; // 편집 버튼 end

/**
* @method : fnAutoPayStop
* @desc : 자동결제 중지 메서드 실행
* @author : LeeChanSin
*/
function fnAutoPayStop(){
    let param ={
        autoPaymentId : document.getElementById('autoPaymentId').value,
    }
    console.log(param.autoPaymentId);
    fetch('/autoPayStop', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(param)
            }).then(response => {
                if (response.ok) {
                    // 요청이 성공적으로 처리되었을 때의 동작
                    alert("정지되었습니다.");
                    location.reload(true);
                } else {
                    // 요청이 실패했을 때의 동작
                    alert("실패했습니다.");

                }
            }).catch(error => {
                console.error('Error:', error);
                alert("알수없는 에러.");
            });
}


/**
* @method : formatDateTime
* @desc : 날짜 데이터 LocalDateTime 형식으로 변환
* @author : LeeChanSin
*/
function formatDateTime(dateString) {
    // dateString을 Date 객체로 변환합니다.
    let currentDate = new Date(dateString);

    // 각 부분을 0으로 채워서 포맷합니다.
    let year = currentDate.getFullYear();
    let month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 현재 월을 가져오도록 수정합니다.
    let day = String(currentDate.getDate()).padStart(2, '0');
    let hour = '00';
    let minute = '00';
    let second = '00';
    let milliseconds = '000000';
    // 날짜와 시간을 합쳐서 반환합니다.
    return `${year}-${month}-${day} ${hour}:${minute}:${second}.${milliseconds}`;
}