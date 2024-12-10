package com.os.autoPayment.dto;

import lombok.Data;

@Data
public class AutoDetailedSearchDTO {
    private String startDt;
    private String endDt;
    private String status;
    private String docNumber;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String transactionStatus;
    private String payType;

}
