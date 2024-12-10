package com.os.toss;

import lombok.Data;

@Data
public class PaymentRequest {
    private String paymentKey;
    private String orderId;
    private String amount;

}