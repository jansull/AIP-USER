package com.os.repository;

import com.os.autoPayment.entity.AutoPayment;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@NonNullApi
public interface AutoPaymentRepository extends JpaRepository<AutoPayment,Long> {
    long countByAutoStatusAndUpdateAtBetween(AutoStatus autoStatus, LocalDateTime startDate, LocalDateTime endDate);

    long countByUpdateAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Optional<AutoPayment> findByPaymentId(Long id);

    Page<AutoPayment> findAll(Pageable pageable);

    Page<AutoPayment> findByPayment_Customer_CustomerNameAndAutoOrderStatus(String keyword, AutoOrderStatus autoOrderStatus, Pageable pageable);

    Page<AutoPayment> findByAutoStatus(AutoStatus autoStatus, Pageable pageable);
}
