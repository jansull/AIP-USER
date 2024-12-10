package com.os.repository;

import com.os.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ProductRepository extends JpaRepository<Product,Long> {
    /**
        @method : findByPaymentId
        @desc : paymentId 를 이용해 결제물품등록정보를 select 하는 메서드
        @author : 김성민
    */
    List<Product> findByPaymentId(Long paymentId);
}
