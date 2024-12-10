package com.os.payment.service;

import com.os.payment.entity.Payment;
import com.os.payment.dto.PaymentDTO;
import com.os.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    /**
     * @method : paymentRoad
     * @desc : 결제 정보 pk로 불러오기
     * @author : LeeChanSin
     */
    public PaymentDTO paymentRoad(Long id){
        Optional<Payment> paymentOptional = paymentRepository.findByCustomerIdAndPaymentDelYn(id, 'N');

        if(paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();

            return PaymentDTO.payInfoDTO(payment);
        } else{
            return null;
        }
    }

    /**
     * @method : getCountsByMonthRange
     * @desc : 이번달 결제등록 횟수 불러오기
     * @author : LeeChanSin
     */
    public List<Long> getCountsByMonthRange() {

        LocalDateTime startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Long> dailyCounts = new ArrayList<>();


        LocalDate currentDate = startDate.toLocalDate();

        while (!currentDate.isAfter(endDate.toLocalDate())) {
            Long count = paymentRepository.countPaymentsByDate(currentDate);
            dailyCounts.add(count != null ? count : 0L);
            currentDate = currentDate.plusDays(1);
        }

        return dailyCounts;
    }

    /**
     * @method : getCountsByYearRange
     * @desc : 이번년도 결제등록 횟수 불러오기
     * @author : LeeChanSin
     */
    public List<Long> getCountsByYearRange() {
        int nowYear = LocalDateTime.now().getYear(); // 현재 년도

        List<Long> monthCounts = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            LocalDate startDate = LocalDate.of(nowYear, i, 1); // 해당 월의 시작일
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()); // 해당 월의 마지막 날짜

            Long count = paymentRepository.countPaymentsYearByDateRange(startDate, endDate);
            monthCounts.add(count != null ? count : 0L);
        }

        return monthCounts;
    }
}
