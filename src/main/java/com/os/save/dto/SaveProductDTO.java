package com.os.save.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProductDTO {
    private String s_productName;
    private int s_productTotalItems;
    private int s_productPrice;

}
