package com.os.autoPayment.service;

import com.os.autoPayment.dto.AutoPaymentDTO;
import com.os.autoPayment.entity.AutoPayment;
import com.os.payment.entity.Payment;
import com.os.repository.AutoPaymentRepository;
import com.os.repository.PaymentRepository;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import com.os.util.enums.OrderStatus;
import com.os.util.enums.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class    AutoPaymentService {
    private final AutoPaymentRepository autoPaymentRepository;
    private final PaymentRepository paymentRepository;

    /**
        @method : UpdatePaid
        @desc : 자동결제테이블에 insert 하는 메서드
        @author : 김성민
    */
    public void UpdatePaid(Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);

        if (paymentOptional.isPresent()){

            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(OrderStatus.paid);

            if(payment.getPaymentType()== OrderType.auto) {

                AutoPayment autoPayment = new AutoPayment();
                autoPayment.setAutoStatus(AutoStatus.paid);
                autoPayment.setAutoPayCount(1);
                autoPayment.setPaymentNextTime(Payment.calculateLocalDateTime(payment.getPaymentMonth(),payment.getPaymentAutoDate()));
                autoPayment.setAutoPayDate(LocalDate.now());
                autoPayment.setAutoOrderStatus(AutoOrderStatus.paid);
                autoPayment.setPayment(payment);

                autoPaymentRepository.save(autoPayment);
            }
            paymentRepository.save(payment);
        }
    }

    /**
        @method : autoSuccess
        @desc : 자동결제목록 중 시작일과 종료일 사이의 날짜 범위내에서 AutoStatus 가 paid 인 목록의 갯수를 구하는 메서드
        @author : 김성민
    */
    public long autoSuccess(LocalDateTime startDate, LocalDateTime endDate){
        return autoPaymentRepository.countByAutoStatusAndUpdateAtBetween(AutoStatus.paid, startDate, endDate);
    }

    /**
        @method : autoStop
        @desc : 자동결제목록 중 시작일과 종료일 사이의 날짜 범위내에서 AutoStatus 가 stop 인 목록의 갯수를 구하는 메서드
        @author : 김성민
    */
    public long autoStop(LocalDateTime startDate, LocalDateTime endDate){
        return autoPaymentRepository.countByAutoStatusAndUpdateAtBetween(AutoStatus.stop, startDate, endDate);
    }

    /**
        @method : autoError
        @desc : 자동결제목록 중 시작일과 종료일 사이의 날짜 범위내에서 AutoStatus 가 error 인 목록의 갯수를 구하는 메서드
        @author : 김성민
    */
    public long autoError(LocalDateTime startDate, LocalDateTime endDate){
        return autoPaymentRepository.countByAutoStatusAndUpdateAtBetween(AutoStatus.error, startDate, endDate);
    }

    /**
        @method : autoAll
        @desc : 자동결제목록 중 시작일과 종료일 사이의 날짜 범위내에서 목록의 갯수를 구하는 메서드
        @author : 김성민
    */
    public long autoAll(LocalDateTime startDate, LocalDateTime endDate){
        return autoPaymentRepository.countByUpdateAtBetween(startDate, endDate);
    }

    /**
     @method : autoAll
     @desc : autoPayment entity 정보 dto 로 변환
     @author : 이찬신
     */
    public AutoPaymentDTO autoPayRoad(Long id){
        Optional<AutoPayment> autoPaymentOptional = autoPaymentRepository.findByPaymentId(id);
        if(autoPaymentOptional.isPresent()){
            AutoPayment autoPayment = autoPaymentOptional.get();
            return AutoPaymentDTO.autoPaymentInfoDTO(autoPayment);
        }
        return null;
    }
}


