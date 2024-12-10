package com.os.customer.dto;

import com.os.product.dto.ProductDTO;
import com.os.util.enums.BizTo;
import com.os.util.enums.OrderType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InsertDTO {

    private LocalDateTime paymentCreateAt;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String customerAddress;

    private String paymentTitle;

    private OrderType paymentType;

    private BizTo paymentBizTo;

    private int paymentMonth;

    private int autoDate;

    private int paymentFirstPay;

    private List<ProductDTO> productList;
}
