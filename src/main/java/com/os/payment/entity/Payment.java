package com.os.payment.entity;

import com.os.autoPayment.entity.AutoPayment;
import com.os.customer.entity.Customer;
import com.os.product.entity.Product;
import com.os.util.common.BaseEntity;
import com.os.util.enums.BizTo;
import com.os.util.enums.OrderStatus;
import com.os.util.enums.OrderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity{
    @Id
    @Column(nullable = false, name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                            // 결제 IDX

    @NotBlank(message = "제목은 필수값입니다.")
    @Length(min = 1,max = 100 ,message = "1~100자 사이로 입력해주세요")
    @Column(nullable = false)
    private String paymentTitle;                                // 결제 제목

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType paymentType;                              // 결제 종류(자동결제 / 일반결제)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BizTo paymentBizTo;                                 // 결제 구분(BtoC / BtoB)

    private String paymentMemo;                                 // 결제 메모

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus paymentStatus;                          // 결제상태(성공 / 불가 / 오류)

    @Column(nullable = false)
    private char paymentDelYn;                                  // 결제 삭제여부(Y / N)

    private int paymentMonth;                                   // 자동결제 다음결제일

    private int paymentAutoDate;

    private int paymentFirstPay;                                // 자동결제 금액

    //////////////////////////////////////////////////////////////////////////////////

    // 고객 IDX
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id",nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY,cascade =CascadeType.REMOVE)
    private AutoPayment autoPayments;

    public String calculateTotalAmount(List<Product> products){
        int totalAmount =  products.stream().mapToInt(Product::getProductAmount).sum();

        return String.valueOf(totalAmount);
    }

    public static LocalDateTime calculateLocalDateTime(int month, int date){
        LocalDate currentDate = LocalDate.now();
        LocalDate calculateDate = currentDate.plusMonths(month).withDayOfMonth(date);

        return LocalDateTime.of(calculateDate, LocalTime.MIN);
    }
}
