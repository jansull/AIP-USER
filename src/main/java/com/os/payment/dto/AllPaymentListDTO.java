package com.os.payment.dto;

import com.os.payment.entity.Payment;
import com.os.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllPaymentListDTO {
    private LocalDateTime UpdateAt;
    private Long id;
    private String customerName;
    private String paymentTitle;
    private String totalAmount;
    private OrderStatus paymentStatus;
    private char paymentDelYn;

    public static AllPaymentListDTO toAllPaymentListDto(Payment payment) {
        String totalAmount = payment.calculateTotalAmount(payment.getProducts());

        return AllPaymentListDTO.builder()
                .UpdateAt(payment.getUpdateAt())
                .id(payment.getCustomer().getId())
                .customerName(payment.getCustomer().getCustomerName())
                .paymentTitle(payment.getPaymentTitle())
                .totalAmount(totalAmount)
                .paymentStatus(payment.getPaymentStatus())
                .paymentDelYn(payment.getPaymentDelYn())
                .build();

    }
}
