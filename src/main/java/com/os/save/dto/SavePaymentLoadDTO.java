package com.os.save.dto;

import com.os.util.enums.BizTo;
import com.os.util.enums.OrderType;
import lombok.Data;

import java.util.List;

@Data
public class SavePaymentLoadDTO {
    private long id;
    private String s_paymentName;
    private String s_paymentEmail;
    private String s_paymentPhone;
    private String s_paymentAddress;
    private String s_paymentTitle;
    private OrderType s_paymentType;
    private int s_paymentFirstPay;
    private BizTo s_paymentBizTo;
    private int s_paymentCycle;
    private int s_paymentDate;
    private int s_paymentPay;
    private List<SaveProductDTO> productList;

}
