package com.os.autoPayment.dto;

import com.os.autoPayment.entity.AutoPayment;
import com.os.util.enums.AutoStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AutoPaymentDTO {
    private Long id;
    private LocalDateTime paymentNextTime;
    private int autoPayCount;
    private AutoStatus autoStatus;
    private LocalDate autoPayDate;

    public static AutoPaymentDTO autoPaymentInfoDTO(AutoPayment autoPayment){
        return AutoPaymentDTO.builder()
                .id(autoPayment.getId())
                .paymentNextTime(autoPayment.getPaymentNextTime())
                .autoPayCount(autoPayment.getAutoPayCount())
                .autoPayDate(autoPayment.getAutoPayDate())
                .autoStatus(autoPayment.getAutoStatus())
                .build();

    }
}
