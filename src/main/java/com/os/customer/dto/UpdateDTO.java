package com.os.customer.dto;

import com.os.product.dto.ProductDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class UpdateDTO {
    //customer
    private Long customerId; // 고객 id
    private String customerName; // 고객이름
    private String customerEmail; // 고객 이메일
    private String customerAddress; // 고객주소
    private String customerPhone; // 고객 연락처

    //payment
    private Long paymentId;  // 수정할 paymentId
    private String paymentTitle; // 제목
    private String paymentMemo; // 메모

    //product
    private List<Long> productDelCheck; // 삭제할 productIdl
    private List<ProductDTO> productList; // 추가되거나 업데이트할 리스트

    // autoPayment
    private Long autoPaymentId; // 자동결제 id
    private String paymentNextTime; // 다음결제일

    private boolean autoOrBasic;

}
