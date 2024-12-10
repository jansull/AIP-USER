package com.os.repository;

import com.os.save.entity.SavePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
public interface SavePaymentRepository extends JpaRepository<SavePayment,Long> {
    /**
        @method : findByUserId
        @desc : 임시저장한 결제등록정보를 select 하는 메서드
        @author : 김성민
    */
    Optional<SavePayment> findByUserId(long userId);

}
