        /**
            @function : validateInput
            @desc : 입력값이 숫자로만 이루어져있는지 유효성 검사하는 함수
            @author : 김성민
        */
        function validateInput(input) {
            return /^\d+$/.test(input) ? input : "";
        }

        /**
            @function : updateTotalItems
            @desc : 수량이 입력되면 금액을 출력하는 함수
            @author : 김성민
        */
        function updateTotalItems(row) {
            const table = document.getElementById("product");
            let totalAmount = 0;
            for (let i = 1; i < table.rows.length - 1; i++) {
                const amountInput = table.rows[i].cells[2].querySelector("input").value;
                if (!validateInput(amountInput)) {
                    document.getElementById("totalTotalItems").innerHTML = "";
                    return;
                }
                totalAmount += parseInt(amountInput);
            }
            if (isNaN(totalAmount)) {
                document.getElementById("totalTotalItems").innerHTML = "";
            } else {
                document.getElementById("totalTotalItems").innerHTML = totalAmount;
            }
            const amountInput = row.cells[2].querySelector("input").value;
            const priceInput = row.cells[3].querySelector("input").value;
            const totalInput = row.cells[4].querySelector("input");
            if (validateInput(amountInput)) {
                const amount = parseInt(amountInput); // 수량 값
                const price = parseInt(priceInput); // 단가 값
                const total = amount * price;
                if (isNaN(total)) {
                    totalInput.value = "";
                } else {
                    totalInput.value = total;
                }
                const table = document.getElementById("product");
                let tTotal = 0;
                for (let i = 1; i < table.rows.length - 1; i++) {
                    const amountInput = table.rows[i].cells[4].querySelector("input").value;
                    if (!isNaN(amountInput)) {
                        tTotal += parseInt(amountInput);
                    }
                }
                if (isNaN(tTotal)) {
                    document.getElementById("final").innerHTML = "";
                } else {
                    document.getElementById("final").innerHTML = tTotal;
                }
            } else {
                totalInput.value = "";
                document.getElementById("final").innerHTML = "";
                return;
            }
        }

        /**
            @function : updatePrice
            @desc : 단가이 입력되면 금액을 출력하는 함수
            @author : 김성민
        */
        function updatePrice(row) {
            const table = document.getElementById("product");
            let totalPrice = 0;
            for (let i = 1; i < table.rows.length - 1; i++) {
                const priceInput = table.rows[i].cells[3].querySelector("input").value;
                if (!validateInput(priceInput)) {
                    document.getElementById("totalPrice").innerHTML = "";
                }
                totalPrice += parseInt(priceInput);
            }
            if (isNaN(totalPrice)) {
                document.getElementById("totalPrice").innerHTML = "";
            } else {
                document.getElementById("totalPrice").innerHTML = totalPrice;
            }
            const amountInput = row.cells[2].querySelector("input").value;
            const priceInput = row.cells[3].querySelector("input").value;
            const totalInput = row.cells[4].querySelector("input");
            if (validateInput(priceInput)) {
                const amount = parseInt(amountInput); // 수량 값
                const price = parseInt(priceInput); // 단가 값
                const total = amount * price;
                if (isNaN(total)) {
                    totalInput.value = "";
                } else {
                    totalInput.value = total;
                }
                const table = document.getElementById("product");
                let tTotal = 0;
                for (let i = 1; i < table.rows.length - 1; i++) {
                    const amountInput = table.rows[i].cells[4].querySelector("input").value;
                    if (!isNaN(amountInput)) {
                        tTotal += parseInt(amountInput);
                    }
                }
                if (isNaN(tTotal)) {
                    document.getElementById("final").innerHTML = "";
                } else {
                    document.getElementById("final").innerHTML = tTotal;
                }
            } else {
                totalInput.value = "";
                document.getElementById("final").innerHTML = "";
                return;
            }
        }

        let num = 2;


        /**
            @function : addRow
            @desc : 행추가 버튼 클릭시 행을 추가하는 함수
            @author : 김성민
        */
        function addRow() {
            const table = document.getElementById("product");
            var deleteRow_btn = document.getElementById("deleteRow_btn");
            const newRow = table.insertRow(num);

            const newCell1 = newRow.insertCell(0);
            const newCell2 = newRow.insertCell(1);
            const newCell3 = newRow.insertCell(2);
            const newCell4 = newRow.insertCell(3);
            const newCell5 = newRow.insertCell(4);

            const input1 = document.createElement("input");
            input1.setAttribute("type", "checkbox");
            newCell1.appendChild(input1);

            const input2 = document.createElement("input");
            input2.setAttribute("type", "text");
            input2.setAttribute("name", "productName");
            newCell2.appendChild(input2);

            const input3 = document.createElement("input");
            input3.setAttribute("type", "text");
            input3.setAttribute("name", "productTotalItems");
            input3.addEventListener("input", function() {
                updateTotalItems(newRow);
            });
            newCell3.appendChild(input3);

            const input4 = document.createElement("input");
            input4.setAttribute("type", "text");
            input4.setAttribute("name", "productPrice");
            input4.addEventListener("input", function() {
                updatePrice(newRow);
            });
            newCell4.appendChild(input4);

            const input5 = document.createElement("input");
            input5.setAttribute("type", "text");
            input5.setAttribute("name", "productAmount");
            input5.setAttribute("readonly", true);
            newCell5.appendChild(input5);

            num++;

            deleteRow_btn.disabled = false;
            document.getElementById("final").innerHTML = "";
            // 새로운 행 추가 후에도 1번 행에 대해 calculateAmount 호출
            updateTotalItems(newRow);
            updatePrice(newRow);
        }

        /**
            @function : deleteRow
            @desc : 행삭제 버튼 클릭 시 체크된 행을 삭제하는 함수
            @author : 김성민
        */
        function deleteRow(rowNum) {
            const table = document.getElementById('product');
            var deleteRow_btn = document.getElementById("deleteRow_btn");
            let numRows = table.rows.length - 2; // 첫 행과 마지막 행은 무시하기 위해 -2
            let deletedRows = 0; // 삭제된 행의 개수를 저장하는 변수 추가
            for (let i = numRows; i > 0; i--) {
                const checkbox = table.rows[i].cells[0].querySelector("input[type='checkbox']");
                if (checkbox.checked) {
                    table.deleteRow(i);
                    numRows--;
                    deletedRows++; // 삭제된 행의 개수 증가
                }
            }
            num -= deletedRows; // 삭제된 행의 개수만큼 num 변수에서 빼기

            if (numRows == 0) {
                deleteRow_btn.disabled = true;
                document.getElementById("totalTotalItems").innerHTML = "";
                document.getElementById("totalPrice").innerHTML = "";
                document.getElementById("final").innerHTML = "";
            } else {
                for (let i = 1; i <= numRows; i++) {
                    const row = table.rows[i];
                    updatePrice(row);
                    updateTotalItems(row);
                }
            }
        }

        /**
            @function : cycle
            @desc : 자동결제 체크박스 체크시 자동결제세부사항 disabled 해제하는 함수
            @author : 김성민
        */
        function cycle() {
            var paymentTypeCheck = document.getElementById("paymentType");
            var firstPay_check = document.getElementById("firstPay_check");
            var autoMonth = document.getElementById("paymentMonth");
            var autoDate = document.getElementById("autoDate");
            var paymentFirstPay = document.getElementById("paymentFirstPay");

            if (paymentTypeCheck.checked) {
                firstPay_check.disabled = false;
                paymentMonth.disabled = false;
                autoDate.disabled = false;
                paymentFirstPay.disabled = false;
            } else {
                firstPay_check.disabled = true;
                paymentMonth.disabled = true;
                autoDate.disabled = true;
                paymentFirstPay.disabled = true;
                document.getElementById("paymentFirstPay").value = 0;
                document.getElementById("autoDate").value = 0;
            }
        }

        /**
            @function : send
            @desc : 결제정보들을 등록하는 함수
            @author : 김성민
        */
        function send() {
            var f = document.getElementById('paymentForm');
            var CreateAt = new Date().toISOString().slice(0, 16);
            var name = f.customerName.value;
            var email = f.customerEmail.value;
            var phone = f.customerPhone.value;

            var address = f.customerAddress.value;
            var title = f.paymentTitle.value;
            var type = f.paymentType.checked ? "auto" : "basic";
            var bizTo = f.paymentBizTo.value;

            var month = f.paymentMonth.value == "다음달" ? 1 : 2;
            var autoDate = f.autoDate.value;
            var paymentFirstPay = f.paymentFirstPay.value;
//            alert(type);
//            alert(month);
//            alert(autoDate);
//            alert(paymentFirstPay);
            if (type == "basic") {
                month = 0;
                autoDate = 0;
                paymentFirstPay = 0;
            }
//            alert(type);
//            alert(month);
//            alert(autoDate);
//            alert(paymentFirstPay);
            if (!nameR(name)) {
                return;
            }
            if (!emailR(email)) {
                return;
            }
            if (!phoneR(phone)) {
                return;
            }
            if (!addressR(address)) {
                return;
            }
            if (!titleR(title)) {
                return;
            }
            if (!autoDateR(autoDate)) {
                return;
            }
            if (!priceR(paymentFirstPay)) {
                return;
            }



            var productRows = document.querySelectorAll("#product tbody tr");
            if (productRows.length > 2 && productRows[1].querySelector("input[type='checkbox']").checked) {
                alert("결제 물품에 등록된 전체 물품을 결제합니다. 체크박스를 해제해 주세요");
                return;
            }
            var final = document.getElementById("final").innerHTML;

            var productList = [];
            for (var i = 1; i < productRows.length - 1; i++) {
                var productName = productRows[i].querySelector("input[name='productName']").value;
                if (!proNameR(productName)) {
                    return;
                }
                var totalItems = productRows[i].querySelector("input[name='productTotalItems']").value;
                if (!totalItemsR(totalItems)) {
                    return;
                }
                var price = productRows[i].querySelector("input[name='productPrice']").value;
                if (!priceR(price)) {
                    return;
                }
                var productAmount = productRows[i].querySelector("input[name='productAmount']").value;
                productList.push({
                    productName: productName,
                    productTotalItems: totalItems,
                    productPrice: price,
                    productAmount: productAmount
                });
            }

            var data = {
                paymentCreateAt: CreateAt,
                customerName: name,
                customerEmail: email,
                customerPhone: phone,
                customerAddress: address,
                paymentTitle: title,
                paymentType: type,
                paymentBizTo: bizTo,
                paymentMonth: month,
                autoDate: autoDate,
                paymentFirstPay: paymentFirstPay,
                productList: productList
            };

            // JSON 데이터를 서버에 전송
            fetch('/insert', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then(response => {
                if (response.ok) {
                    // 요청이 성공적으로 처리되었을 때의 동작
                    console.log('Request succeeded');
                    alert("저장성공");
                    window.location.href = "dashboard";
                } else {
                    // 요청이 실패했을 때의 동작
                    console.error('Request failed');
                    alert("요청실패");
                    return;
                }
            }).catch(error => {
                console.error('Error:', error);
            });
        }

        /**
            @function : toggleAllCheckboxes
            @desc : 최상위 체크박스를 체크시 모든 체크박스를 체크하는 함수
            @author : 김성민
        */
        function toggleAllCheckboxes() {
            var masterCheckbox = document.getElementById('masterCheckbox');
            var checkboxes = document.querySelectorAll("#product input[type='checkbox']");
            checkboxes.forEach(function(checkbox) {
                checkbox.checked = masterCheckbox.checked;
            });
        }

        window.addEventListener('DOMContentLoaded', function() {
            document.getElementById('masterCheckbox').addEventListener('click', toggleAllCheckboxes);
        });

        /**
            @function : save
            @desc : 결제정보들을 임시저장테이블에 임시저장하는 함수
            @author : 김성민
        */
        function save() {
            var before = document.getElementById("before");
            var f = document.getElementById('paymentForm');
            var name = f.customerName.value;
            var email = f.customerEmail.value;
            var phone = f.customerPhone.value;
            var address = f.customerAddress.value;
            var title = f.paymentTitle.value;
            var type = f.paymentType.checked ? "auto" : "basic";
            var firstPayCheck = f.firstPay_check.checked ? 1 : 0;
            var bizTo = f.paymentBizTo.value;

            var month = f.paymentMonth.value == "다음달" ? 1 : 2;
            var autoDate = f.autoDate.value;
            var paymentFirstPay = f.paymentFirstPay.value;
            
            if (type == "basic") {
                month = 0;
                autoDate = 0;
                paymentFirstPay = 0;
            }
            if (!nameRs(name)) {
                return;
            }
            if (!emailRs(email)) {
                return;
            }
            if (!phoneRs(phone)) {
                return;
            }
            if (!addressRs(address)) {
                return;
            }
            if (!titleRs(title)) {
                return;
            }
            if(!autoDateR(autoDate)){
                return;
            }
            if (!priceRs(paymentFirstPay)) {
                return;
            }


            var productRows = document.querySelectorAll("#product tbody tr");
            var productList = [];
            for (var i = 1; i < productRows.length - 1; i++) {
                var productName = productRows[i].querySelector("input[name='productName']").value;
                if(!proNameRs(productName)){return;}
                var totalItems = productRows[i].querySelector("input[name='productTotalItems']").value;
                if(!totalItemsRs(totalItems)){return;}
                var price = productRows[i].querySelector("input[name='productPrice']").value;
                if(!priceRs(price)){return;}
                productList.push({ s_productName: productName, s_productTotalItems: totalItems, s_productPrice: price});
            }

            var data = {
                s_paymentName: name,
                s_paymentEmail: email,
                s_paymentPhone: phone,
                s_paymentAddress: address,
                s_paymentTitle: title,
                s_paymentType: type,
                s_paymentFirstPay: firstPayCheck,
                s_paymentBizTo: bizTo,
                s_paymentCycle: month,
                s_paymentDate: autoDate,
                s_paymentPay: paymentFirstPay,
                productList: productList
            };

                // JSON 데이터를 서버에 전송
                fetch('/save', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                }).then(response => {
                    if (response.ok) {
                        // 요청이 성공적으로 처리되었을 때의 동작
                        console.log('Request succeeded');
                        alert('임시저장 성공!');
                        before.disabled = false;
                    } else {
                        // 요청이 실패했을 때의 동작
                        console.error('Request failed');

                    }
                }).catch(error => {
                    console.error('Error:', error);
                });
            }

            /**
                @function : load
                @desc : 임시저장된 정보들을 불러와 덮어쓰기 해주는 함수
                @author : 김성민
            */
            function load() {
                fetch('/load')
                    .then(response => response.json())
                    .then(data => {
                        // savePaymentLoad 에서 데이터 추출
                        const s_paymentName = data.s_paymentName;
                        const s_paymentEmail = data.s_paymentEmail;
                        const s_paymentPhone = data.s_paymentPhone;
                        const s_paymentAddress = data.s_paymentAddress;
                        const s_paymentTitle = data.s_paymentTitle;
                        const s_paymentType = data.s_paymentType;
                        const s_paymentFirstPay = data.s_paymentFirstPay;
                        const s_paymentBizTo = data.s_paymentBizTo;
                        const s_paymentCycle = data.s_paymentCycle;
                        const s_paymentDate = data.s_paymentDate;
                        const s_paymentPay = data.s_paymentPay;

                        // HTML 에 데이터 표시
                        document.getElementById('customerName').value = s_paymentName;
                        document.getElementById('customerEmail').value = s_paymentEmail;
                        document.getElementById('customerPhone').value = s_paymentPhone;
                        document.getElementById('customerAddress').value = s_paymentAddress;
                        document.getElementById('paymentTitle').value = s_paymentTitle;
                        if (s_paymentType == 'auto') {
                            document.getElementById('paymentType').checked = true;
                            cycle();
                        }else{
                            document.getElementById('paymentType').checked = false;
                            cycle();
                        }
                        if (s_paymentFirstPay == 1) {
                            document.getElementById('firstPay_check').checked = true;
                        }else{
                            document.getElementById('firstPay_check').checked = false;
                        }
                        const bizTo = document.getElementsByName('paymentBizTo');
                        for (let i = 0; i < bizTo.length; i++) {
                            if (bizTo[i].value === s_paymentBizTo) {
                                bizTo[i].checked = true;
                            }
                        }
                        if (s_paymentCycle === 1) {
                            document.getElementById('paymentMonth').selectedIndex = 0;
                        } else if (s_paymentCycle === 2) {
                            document.getElementById('paymentMonth').selectedIndex = 1;
                        }
                        if (s_paymentDate != 0) {
                            document.getElementById('autoDate').value = s_paymentDate;
                        }
                        if (s_paymentPay != 0) {
                            document.getElementById('paymentFirstPay').value = s_paymentPay;
                        }

                        // saveProductList 에서 데이터 추출
                        const productList = data.productList;

                        // 기존 테이블의 tbody 를 가져옴
                        const tbody = document.querySelector('#product tbody');
                        // tbody 초기화
                        const rows = Array.from(tbody.children).slice(1, -1);
                        if (rows.length > 0) {
                            rows.forEach(row => row.remove());
                        }
                        num = 1;
                        // HTML 에 리스트 데이터 표시

                        productList.forEach(product => {
                            const newRow = tbody.insertRow(num);

                            const newCell1 = newRow.insertCell(0);
                            const newCell2 = newRow.insertCell(1);
                            const newCell3 = newRow.insertCell(2);
                            const newCell4 = newRow.insertCell(3);
                            const newCell5 = newRow.insertCell(4);

                            const input1 = document.createElement("input");
                            input1.setAttribute("type", "checkbox");
                            newCell1.appendChild(input1);

                            const input2 = document.createElement("input");
                            input2.setAttribute("type", "text");
                            input2.setAttribute("name", "productName");
                            input2.value = product.s_productName;
                            newCell2.appendChild(input2);

                            const input3 = document.createElement("input");
                            input3.setAttribute("type", "text");
                            input3.setAttribute("name", "productTotalItems");
                            input3.value = product.s_productTotalItems;
                            input3.addEventListener("input", function() {
                                updateTotalItems(newRow);
                            });
                            newCell3.appendChild(input3);

                            const input4 = document.createElement("input");
                            input4.setAttribute("type", "text");
                            input4.setAttribute("name", "productPrice");
                            input4.value = product.s_productPrice;
                            input4.addEventListener("input", function() {
                                updatePrice(newRow);
                            });
                            newCell4.appendChild(input4);

                            const input5 = document.createElement("input");
                            input5.setAttribute("type", "text");
                            input5.setAttribute("name", "productAmount");
                            input5.setAttribute("readonly", true);
                            newCell5.appendChild(input5);

                            num++;

                            deleteRow_btn.disabled = false;
                            document.getElementById("final").innerHTML = "";
                            // 새로운 행 추가 후에도 1번 행에 대해 calculateAmount 호출
                            updateTotalItems(newRow);
                            updatePrice(newRow);
                        });
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            }

            /*
                @function : __Rs
                @desc : 임시저장에 관련된 유효성 체크 함수
                @author : 김성민
            */
            function titleRs(text) {
                if (!titleRex.test(text)) {
                    alert("제목 임시저장은 100자 이내여야합니다.");
                    return false;
                }
                return true;
            }

            function addressRs(text) {
                if (!titleRex.test(text)) {
                    alert("주소 임시저장은 100자 이내여야합니다.");
                    return false;
                }
                return true;
            }



            function totalItemsRs(text) {
                if (!totalItemsRex2.test(text)) {
                    alert("수량 임시저장은 숫자만 입력 가능합니다.");
                    return false;
                }
                return true;
            }

            function priceRs(text) {

                if (!priceRex2.test(text)) {
                    alert("단가 임시저장은 숫자만 입력 가능합니다.");
                    return false;
                }
                return true;
            }

            function nameRs(text) {
                if (!nameRex.test(text)) {
                    alert("16자만 입력 가능합니다.");
                    return false;
                }
                return true;
            }

            function emailRs(text) {
                if (!emailRex2.test(text)) {
                    alert("이메일 임시저장은 100자 이내여야합니다.");
                    return false;
                }
                return true;
            }

            function phoneRs(text) {
                if (!phoneRex2.test(text)) {
                    alert("연락처 임시저장은 100자 이내여야합니다..");
                    return false;
                }
                return true;
            }

            function proNameRs(text) {
                if (!proNameRex.test(text)) {
                    alert("상품명 임시저장은 100자 이내여야합니다.");
                    return false;
                }
                return true;
            }
            function autoDateR(text) {
                if (!autoDateRex.test(text)) {
                    alert("자동결제일 은 0에서 31사이의 숫자만 가능합니다..");
                    return false;
                }
                return true;
            }
