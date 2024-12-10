package com.os.product.service;

import com.os.product.dto.ProductDTO;
import com.os.product.entity.Product;
import com.os.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * @method : productRoad
     * @desc : product 정보 pk로 불러오기
     * @author : LeeChanSin
     */
    public List<ProductDTO> productRoad(Long id){
        List<Product> productList = productRepository.findByPaymentId(id);

        List<ProductDTO> productInfoList = new ArrayList<>(); // productInfoList 변수 선언 및 초기화

        if(!productList.isEmpty()){
            for(Product product : productList){
                ProductDTO productInfo = ProductDTO.productInfoDTO(product);
                productInfoList.add(productInfo);
            }
            return productInfoList;
        } else{
            return new ArrayList<>(); // 빈 리스트 반환
        }
    }
}
