package com.os.util.scheduler;

import com.os.customer.entity.Customer;
import com.os.memo.entity.Memo;
import com.os.repository.CustomerRepository;
import com.os.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SoftDeleteScheduler {
    private final MemoRepository memoRepository;

    private final CustomerRepository customerRepository;

    /**
     * @method permanentlyDeleteSoftDeletedMemo
     * @desc : 데이터 보존기간 이후 hard delete
     * @author : LeeChanSin
     */
    @Scheduled(fixedDelay = 300000) // 5분마다 실행
    public void permanentlyDeleteSoftDeletedMemo() {
        // 삭제된 메모 중에서 삭제 후 5분이 지난 메모를 조회하여 영구적으로 삭제

        // soft delete payment list
        List<Customer> softDeletePaymentList = customerRepository.findByPayments_PaymentDelYn('Y');

        // soft delete memo List
        List<Memo> softDeletedMemoList = memoRepository.findByMemoDelYn("Y"); // 소프트 삭제된 메모 목록 조회
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(5); // 삭제 후 5분 후 시간
        for (Memo memo : softDeletedMemoList) {
            // 소프트 삭제된 후 5분이 경과한 메모를 영구적으로 삭제

            if (memo.getUpdateAt().isBefore(thresholdTime)) {
                memoRepository.delete(memo);
            }
        }
        for(Customer customer : softDeletePaymentList){
            if (customer.getPayments().getUpdateAt().isBefore(thresholdTime)) {
                customerRepository.delete(customer);
            }
        }
    }
}
