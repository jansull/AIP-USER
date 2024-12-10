package com.os.repository;

import com.os.save.entity.SaveProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface SaveProductRepository extends JpaRepository<SaveProduct,Long> {
    /**
        @method : findBySavePaymentId
        @desc : 임시저장한 결제물품등록정보를 select 하는 메서드
        @author : 김성민
    */
    List<SaveProduct> findBySavePaymentId(Long s_paymentId);

}
