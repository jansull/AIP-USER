package com.os.customer.dto;

import com.os.customer.entity.Customer;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CustomerLoadDTO {
    private Long id;            // 고객 IDX
    private String customerName;               // 고객 이름
    private String customerEmail;              // 고객 이메일
    private String customerPhone;              // 고객 전화번호
    private String customerAddress;

    /**
     * @method : CustomerInfoDTO
     * @desc : Customer Entity 정보 DTO 로 변환
     * @author : LeeChanSin
     */
    public static CustomerLoadDTO CustomerInfoDTO(Customer customer){
        return CustomerLoadDTO.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .customerPhone(customer.getCustomerPhone())
                .customerAddress(customer.getCustomerAddress())
                .build();

    }
}
