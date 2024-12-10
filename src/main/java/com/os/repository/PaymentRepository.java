package com.os.repository;

import com.os.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Transactional
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByCustomerIdAndPaymentDelYn(Long customerId, char paymentDelYn);

    Page<Payment> findByPaymentDelYn(char n, Pageable pageable);

    Page<Payment> findByPaymentTitleContainingAndPaymentDelYnNot(String keyword, char y, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Payment p WHERE DATE(p.createAt) = :date")
    Long countPaymentsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(p) FROM Payment p WHERE DATE(p.createAt) >= :startDate AND DATE(p.createAt) <= :endDate")
    Long countPaymentsYearByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Payment p JOIN FETCH p.autoPayments a WHERE a.autoStatus = 'paid'")
    List<Payment> findAllPaymentsWithAutoPaymentsAndAutoStatusPaid();

}
