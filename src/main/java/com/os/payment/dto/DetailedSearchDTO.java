package com.os.payment.dto;

import lombok.Data;

/***
 * @author : 김홍성 작성자
 */
@Data
public class DetailedSearchDTO {

    private String startDt;
    private String endDt;
    private String status;
    private String docNumber;
    private String customerName;
    private String email;
}
