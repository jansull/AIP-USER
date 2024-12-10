package com.os.payment.dto;

import com.os.payment.entity.Payment;
import com.os.util.enums.BizTo;
import com.os.util.enums.OrderStatus;
import com.os.util.enums.OrderType;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentDTO {
    private Long id;                                // 결제 IDX
    private String paymentTitle;                    // 결제 제목
    private OrderType paymentType;                  // 결제 종류(자동결제 / 일반결제)
    private BizTo paymentBizTo;                     // 결제 구분(BtoC / BtoB)
    private String paymentMemo;                     // 결제 메모
    private LocalDateTime paymentCreateAt;        // 결제 생성시간
    private OrderStatus paymentStatus;              // 결제상태(성공 / 불가 / 오류)
    private LocalDateTime paymentUpdateAt;        // 수정시간
    private char paymentDelYn;                      // 결제 삭제여부(Y / N)
    private int paymentMonth;                       // 자동결제 다음결제일
    private LocalDateTime paymentNextTime;          // 자동결제 마지막결제일
    private int paymentFirstPay;
    private String totalAmount;

    /**
     * @method : payInfoDTO
     * @desc : payment Entity 정보 DTO 로 변환
     * @author : LeeChanSin
     */
    public static PaymentDTO payInfoDTO(Payment payment){
        String totalAmount = payment.calculateTotalAmount(payment.getProducts());

        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentTitle(payment.getPaymentTitle())
                .paymentType(payment.getPaymentType())
                .paymentBizTo(payment.getPaymentBizTo())
                .paymentMemo(payment.getPaymentMemo())
                .paymentCreateAt(payment.getCreateAt())
                .paymentStatus(payment.getPaymentStatus())
                .paymentUpdateAt(payment.getUpdateAt())
                .paymentDelYn(payment.getPaymentDelYn())
                .paymentMonth(payment.getPaymentMonth())
                .totalAmount(totalAmount)
                .paymentFirstPay(payment.getPaymentFirstPay())
                .build();

    }
}
