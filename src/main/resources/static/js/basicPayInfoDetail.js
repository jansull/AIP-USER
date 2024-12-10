let productDelCheck = [];


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
        const inputs = document.querySelectorAll('.basic-main .editable');
        inputs.forEach(input => {
            input.disabled = false; // 편집 모드에서는 모든 input 비활성화
        });

        // 편집 버튼 텍스트 변경
        const editButton = document.querySelector('.basic-header button:nth-of-type(1)');
        editButton.textContent = '저장';

        // 테두리 보이게 설정
        const tableInputs = document.querySelectorAll('.basic-footer-table input');
        tableInputs.forEach(input => {
            input.style.border = '1px solid black';
            input.style.outline = '1px solid black';
            input.style.backgroundColor = 'white';
        });

        // 행 추가, 삭제 버튼 비활성화
        const addButton = document.querySelector('.basic-footer-header button:nth-of-type(1)');
        const deleteButton = document.querySelector('.basic-footer-header button:nth-of-type(2)');
        addButton.disabled = false;
        deleteButton.disabled = false;

        isEditing = true;
    } else {
        // 저장 모드
        const result = confirm("수정하시겠습니까?");

        // 클래스 요소들의 disabled 속성 설정
        const inputs = document.querySelectorAll('.basic-main .editable');
        inputs.forEach(input => {
            input.disabled = true; // 저장 모드에서는 모든 input 활성화
        });

        // 편집 버튼 텍스트 변경
        const editButton = document.querySelector('.basic-header button:nth-of-type(1)');
        editButton.textContent = '편집';

        // 테두리 안보이게 설정
        const tableInputs = document.querySelectorAll('.basic-footer-table input');
        tableInputs.forEach(input => {
            input.style.border = 'none';
            input.style.outline = 'none';
            input.style.backgroundColor = 'white';
        });


        // 행 추가, 삭제 버튼 비활성화
        const addButton = document.querySelector('.basic-footer-header button:nth-of-type(1)');
        const deleteButton = document.querySelector('.basic-footer-header button:nth-of-type(2)');
        addButton.disabled = true;
        deleteButton.disabled = true;

        isEditing = false;
        if (!result) {
            alert("취소되었습니다.");
            location.reload(true);
            return;

        }

        // product id 체크박스 value 리스트
        // product table 리스트
        // 행 삭제시 다른 삭제된 체크박스 id 배열에 저장



        var productRows = document.querySelectorAll("#basic-proTable tbody tr");
        //            if (productRows.length > 2 && productRows[1].querySelector("input[type='checkbox']").checked) {
        //                alert("결제 물품에 등록된 전체 물품을 결제합니다. 체크박스를 해제해 주세요");
        //                return;
        //            }
        console.log("productRows", productRows.length);
        let productList = [];
        for (var i = 0; i < productRows.length - 1; i++) {
            var productId = productRows[i].querySelector("input[name='productId']");

            var productName = productRows[i].querySelector("input[name='productName']").value;
            var totalItems = productRows[i].querySelector("input[name='productTotalItems']").value;
            var price = productRows[i].querySelector("input[name='productPrice']").value;
            var productAmount = productRows[i].querySelector("input[name='productAmount']").value;



            if (productId.value != 'on' && productId.value != "") {
                var id = productId.value;
            } else {
                var id = 0;
            }

            // 상품정보 정규식 처리
            if(!proNameR(productName)){
                return;
            }
            if(!totalItemsR(totalItems)){
                return;
            }
            if(!priceR(price)){
                return;
            }
            productList.push({
                productName: productName,
                productTotalItems: totalItems,
                productPrice: price,
                productAmount: productAmount,
                id: id
            });



            console.log("productList for", productList[i]);
        }

        let f = document.getElementById("basic-form");

        let param = {
            customerId: f.customerId.value,
            customerName: f.customerName.value,
            customerEmail: f.customerEmail.value,
            paymentTitle: f.paymentTitle.value,
            paymentId: f.paymentId.value,
            productDelCheck: productDelCheck,
            productList: productList,
            autoOrBasic: true
        }
        if(!titleR(param.paymentTitle)){
            return;
        }
        if(!nameR(param.customerName)){
            return;
        }
        if(!emailR(param.customerEmail)){
            return;
        }
        console.log("productList", param.productList);
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


        // update aJax 요청 실행

        // 행 삭제가 있다면 product 는 delete 실행
        // 행 삭제시 product_id 배열에 저장하고 백에서 해당 변수 비어있지 않다면 delete 실헹

    }
}; // 편집 버튼 end

/**
* @method : fnAddTable
* @desc : 상품정보 테이블 행추가
* @author : LeeChanSin
*/
function fnAddTable() {
    // 테이블 요소 가져오기
    const tableBody = document.querySelector('.basic-footer-table tbody');

    // 새로운 행(tr) 요소 생성
    const newRow = document.createElement('tr');

    // 새로운 행에 필요한 셀(td) 요소들 생성
    const checkboxCell = document.createElement('td');
    const noCell = document.createElement('td');
    const productNameCell = document.createElement('td');
    const productTotalItemsCell = document.createElement('td');
    const productPriceCell = document.createElement('td');
    const productAmountCell = document.createElement('td');

    // 각 셀에 input 요소 추가
    checkboxCell.innerHTML = '<input type="checkbox" name="productId">';
    productNameCell.innerHTML = '<label><input type="text" name="productName" class="editable" ></label>';
    productTotalItemsCell.innerHTML = '<label><input type="number" name="productTotalItems" class="editable" onchange="updatePrice(this)"></label>';
    productPriceCell.innerHTML = '<label><input type="number" name="productPrice" class="editable" onchange="updatePrice(this)"></label>';
    productAmountCell.innerHTML = '<label><input type="number" name="productAmount" class="editable" readonly></label>';

    // 새로운 행에 셀 추가
    newRow.appendChild(checkboxCell);
    newRow.appendChild(noCell);
    newRow.appendChild(productNameCell);
    newRow.appendChild(productTotalItemsCell);
    newRow.appendChild(productPriceCell);
    newRow.appendChild(productAmountCell);

    // 마지막 행 가져오기
    const lastRow = tableBody.querySelector('tr:last-of-type');

    // 마지막 행이 있으면
    if (lastRow) {
        // 현재 행의 개수 확인
        const rowCount = tableBody.querySelectorAll('tr').length;

        // 현재 행의 개수에 1을 더하여 새로운 행의 번호로 설정
        noCell.innerText = rowCount.toString();

        // 마지막 행 이전에 새로운 행 추가
        lastRow.insertAdjacentElement('beforebegin', newRow);
    } else {
        // 마지막 행이 없으면 테이블 첫 번째에 새로운 행 추가
        tableBody.appendChild(newRow);
    }
}


/**
* @method : fnDelTable
* @desc : 상품정보 테이블 행삭제
* @author : LeeChanSin
*/
function fnDelTable() {
    const checkedRows = [];

    const tableBody = document.querySelector('.basic-footer-table tbody');
    const checkboxes = tableBody.querySelectorAll('input[type="checkbox"]');

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            // 체크된 체크박스의 value 가져와서 배열에 저장
            const value = checkbox.value;
            // 체크된 행 저장
            checkedRows.push(checkbox.closest('tr'));
            if (checkbox.value != 'on') {
                productDelCheck.push(value); // 전역 변수

            }

        }
    });

    if (checkedRows.length === 0) {
        alert("삭제할 행을 선택해주세요.");
        return;
    }

    checkedRows.forEach(row => {
        row.remove();
    });

    // 선택된 체크박스의 value 출력
    console.log("선택된 체크박스의 value:", productDelCheck);

    // 행 삭제 후 총합 재계산
    updateTotalAmount();


}

/**
* @method : fnDelPay
* @desc : 상품정보 삭제 메서드 실행
* @author : LeeChanSin
*/
function fnDelPay() {
    const result = confirm("삭제하시겠습니까?");
    if (!result) {
        alert("취소되었습니다.");
        return;
    }
    let f = document.getElementById("basic-form");
    let param = {
        paymentId: f.paymentId.value
    }

    fetch('/payDetailDel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(param)
    }).then(response => {
        if (response.ok) {
            // 요청이 성공적으로 처리되었을 때의 동작
            alert("삭제 되었습니다..");
            location.href = "/list"
        } else {
            // 요청이 실패했을 때의 동작
            alert("실패했습니다.");
        }
    }).catch(error => {
        alert("알수없는 에러.");
    });
}

/**
* @method : updatePrice
* @desc : 상품 리스트 총액 계산
* @author : LeeChanSin
*/
function updatePrice(element) {
    const row = element.closest("tr");
    const count = row.querySelector('[name="productTotalItems"]');
    const price = row.querySelector('[name="productPrice"]');
    const amount = row.querySelector('[name="productAmount"]');
    console.log("count", count.value);
    console.log("price", price.value);
    let amountNum = parseFloat(count.value) * parseFloat(price.value); // parseInt 대신 parseFloat 사용

    console.log("amountNum", amountNum);

    amount.value = amountNum;
    updateTotalAmount(); // 총합 업데이트
}

/**
* @method : updateTotalAmount
* @desc : 상품 리스트 총액 계산
* @author : LeeChanSin
*/
function updateTotalAmount() {
    const tableBody = document.querySelector('.basic-footer-table tbody');
    const rows = tableBody.querySelectorAll('tr');
    let totalAmount = 0;

    rows.forEach(row => {
        const amountInput = row.querySelector('[name="productAmount"]');
        if (amountInput) {
            totalAmount += parseFloat(amountInput.value) || 0;
        }
    });

    const totalAmountCell = document.getElementById("totalAmount");
    totalAmountCell.value = totalAmount;
}


/**
* @method : moveQrPay
* @desc : Qr 결제 페이지로 이동
* @author : LeeChanSin
*/
function moveQrPay() {
    let customerId = document.getElementById("customerId").value;

    location.href = "/paymentDetails?id=" + customerId;
}

/**
* @method : moveReceipt
* @desc : 신용구매도표 페이지로 이동
* @author : LeeChanSin
*/
function moveReceipt() {
    let f = document.getElementById("basic-form");

    let paymentId = f.paymentId.value;
    let customerId = f.customerId.value;


    let url = `/receipt/?paymentId=${paymentId}&customerId=${customerId}`;

    console.log("url \n" + url);
    location.href = url;
}