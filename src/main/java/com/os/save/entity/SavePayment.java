package com.os.save.entity;

import com.os.save.dto.SavePaymentDTO;
import com.os.save.dto.SaveProductDTO;
import com.os.user.entity.User;
import com.os.util.enums.BizTo;
import com.os.util.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavePayment {
    @Id
    @Column(nullable = false , name = "s_payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                 // 결제임시저장 IDX

    private String s_paymentName;                    // 결제임시저장 고객명

    private String s_paymentEmail;                   // 결제임시저장 이메일

    private String s_paymentPhone;                   // 결제임시저장 연락처

    private String s_paymentAddress;                 // 결제임시저장 주소

    private String s_paymentTitle;                   // 결제임시저장 제목

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType s_paymentType;                 // 결제임시저장 결제종류(자동결제 / 일반결제)

    @Column(nullable = false)
    private int s_paymentFirstPay;                   // 결제임시저장 처음결제금액

    @Enumerated(EnumType.STRING)

    private BizTo s_paymentBizTo;                    // 결제임시저장 결제구분(BtoC / BtoB)

    private int s_paymentCycle;                      // 결제임시저장 다음결제일(다음달 / 다다음달)

    private int s_paymentDate;                       // 결제임시저장 마지막결제일(1 ~ 31)

    private int s_paymentPay;                        // 결제임시저장 결제금액

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , referencedColumnName = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "savePayment", fetch = FetchType.LAZY,cascade =CascadeType.REMOVE)
    private List<SaveProduct> saveProducts;

    /**
        @method : insertData
        @desc : 받아온 결제등록정보들을 insert(임시저장) 하는 메서드
        @author : 김성민
    */
    public static SavePayment ToEntity(SavePaymentDTO dto, User user) {

        SavePayment savePayment = new SavePayment();

        savePayment.setS_paymentName(dto.getS_paymentName());
        savePayment.setS_paymentEmail(dto.getS_paymentEmail());
        savePayment.setS_paymentPhone(dto.getS_paymentPhone());
        savePayment.setS_paymentAddress(dto.getS_paymentAddress());
        savePayment.setS_paymentTitle(dto.getS_paymentTitle());
        savePayment.setS_paymentType(dto.getS_paymentType());
        savePayment.setS_paymentFirstPay(dto.getS_paymentFirstPay());
        savePayment.setS_paymentBizTo(dto.getS_paymentBizTo());
        savePayment.setS_paymentCycle(dto.getS_paymentCycle());
        savePayment.setS_paymentDate(dto.getS_paymentDate());
        savePayment.setS_paymentPay(dto.getS_paymentPay());
        List<SaveProduct> saveProductList = new ArrayList<>();
        for (SaveProductDTO productDTO : dto.getProductList()) {
            SaveProduct saveProduct = new SaveProduct();
            saveProduct.setS_productTotalItem(productDTO.getS_productTotalItems());
            saveProduct.setS_productPrice(productDTO.getS_productPrice());
            saveProductList.add(saveProduct);
        }

        savePayment.setUser(user);
        savePayment.setSaveProducts(saveProductList);
        return savePayment;
    }
}
