package com.os.autoPayment.entity;

import com.os.payment.entity.Payment;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import com.os.util.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutoPayment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "auto_id")
    private Long id;                                    // 자동결제 IDX

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutoOrderStatus autoOrderStatus;            // 자동결제상태

    private String billingKey;
    private LocalDate autoPayDate;                      // 결제된 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutoStatus autoStatus;                      // 자동결제 상태

    @Column(nullable = false)
    private int autoPayCount;                           // 자동결제 횟수

    private LocalDateTime paymentNextTime;              // 자동결제 마지막결제일

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id",nullable = false)
    private Payment payment;

}
