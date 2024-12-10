package com.os.util.scheduler;

import com.os.payment.entity.Payment;
import com.os.repository.PaymentRepository;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import com.os.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class AutoPayScheduler {
    private final PaymentRepository paymentRepository;

    /**
     * @method autoPaymentUpdate
     * @desc : 자동결제 스케쥴링
     * @author : LeeChanSin
    */
    @Transactional
    @Scheduled(fixedDelay = 300000) // 5분마다 실행
    public void autoPaymentUpdate() {

        List<Payment> payments = paymentRepository.findAllPaymentsWithAutoPaymentsAndAutoStatusPaid();
        LocalDateTime currentDateTime = LocalDateTime.now(); // 현재 날짜
        Random random = new Random();

        for(Payment payment : payments){

            int randomValue = random.nextInt(100); // 0부터 99까지의 랜덤한 정수 생성
            // auto_payments 의 현재날까가 next_time 의 날짜를 지났다면
            if (payment.getAutoPayments().getPaymentNextTime().isBefore(currentDateTime)
                    || payment.getAutoPayments().getPaymentNextTime().isEqual(currentDateTime)
                    && payment.getAutoPayments().getAutoOrderStatus() == AutoOrderStatus.paid) {
                System.out.println("payment 무와아아앙 조건 완 => " + payment.getAutoPayments().getId());
                if(randomValue < 10){ // 테스트용 => 10% 확률로 자동결제 실패
                    payment.setPaymentStatus(OrderStatus.error);
                    payment.getAutoPayments().setAutoStatus(AutoStatus.error);
                } else{
                    // update count +1 next_time update
                    int updatedAutoPayCount = payment.getAutoPayments().getAutoPayCount() + 1;
                    payment.getAutoPayments().setAutoPayCount(updatedAutoPayCount); // 결제 횟수
                    payment.getAutoPayments().setAutoPayDate(LocalDate.now()); // 결제 날짜
                    payment.getAutoPayments().setPaymentNextTime(Payment.calculateLocalDateTime(payment.getPaymentMonth(),payment.getPaymentAutoDate()));
                    payment.setPaymentStatus(OrderStatus.paid);
                    payment.getAutoPayments().setAutoStatus(AutoStatus.paid);
                }


                paymentRepository.save(payment);

            }
        }
    }
}
