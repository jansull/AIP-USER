package com.os.autoPayment.dto;

import com.os.autoPayment.entity.AutoPayment;
import com.os.util.enums.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutoPaymentListDTO {

    private AutoStatus autoStatus;
    private AutoOrderStatus autoOrderStatus;
    private String customerName;
    private String customerEmail;
    @Enumerated(EnumType.STRING)
    private BizTo paymentBizTo;
    private String totalAmount;
    private LocalDateTime paymentNextTime;
    private LocalDateTime UpdateAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus paymentStatus;
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderType paymentType;
    private String customerPhone;

    public static AutoPaymentListDTO toAutoPaymentListDto(AutoPayment autoPayment) {
        String totalAmount = autoPayment.getPayment().calculateTotalAmount(autoPayment.getPayment().getProducts());
        return AutoPaymentListDTO.builder()
                .paymentBizTo(autoPayment.getPayment().getPaymentBizTo())
                .autoStatus(autoPayment.getAutoStatus())
                .customerName(autoPayment.getPayment().getCustomer().getCustomerName())
                .customerEmail(autoPayment.getPayment().getCustomer().getCustomerEmail())
                .totalAmount(totalAmount)
                .paymentNextTime(autoPayment.getPaymentNextTime())
                .UpdateAt(autoPayment.getUpdateAt())
                .autoOrderStatus(autoPayment.getAutoOrderStatus())
                .id(autoPayment.getId())
                .id(autoPayment.getPayment().getCustomer().getId())
                .paymentType(autoPayment.getPayment().getPaymentType())
                .customerPhone(autoPayment.getPayment().getCustomer().getCustomerPhone())
                .build();
    }
}
