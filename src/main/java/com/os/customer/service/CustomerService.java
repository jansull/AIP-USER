package com.os.customer.service;

import com.os.customer.dto.CustomerLoadDTO;
import com.os.customer.entity.Customer;
import com.os.payment.dto.PaymentDetailsDTO;
import com.os.repository.CustomerRepository;
import com.os.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    public PaymentDetailsDTO getDetails(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {

            Customer customer = customerOptional.get();

            return PaymentDetailsDTO.builder()
                    .customer(customer)
                    .build();

        }
        throw new NoSuchElementException("객체가 null 이에요!");
    }


    /**
     @method : thisMonthInsertCount
     @desc : 시작일과 종료일 사이의 날짜 범위내에 등록된 목록의 개수를 구하는 메서드
     @author : 김홍성
     */
    public long thisMonthInsertCount(){
        LocalDateTime now = LocalDateTime.now();
        YearMonth thisMonth = YearMonth.from(now);
        LocalDateTime startOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);
        char delYn = 'N';

        return customerRepository.countByPayments_PaymentDelYnAndCreateAtBetween(delYn,startOfMonth, endOfMonth);
    }

    /**
        @method : countByCustomersByPaid
        @desc : 시작일과 종료일 사이의 날짜 범위내에 등록된 목록의 개수를 구하는 메서드
        @author : 김홍성
    */
    public long countByCustomersByPaid(LocalDateTime startDate, LocalDateTime endDate){
        char delYn = 'N';

        return customerRepository.countByPayments_PaymentStatusAndPayments_PaymentDelYnAndUpdateAtBetween(OrderStatus.paid,delYn,startDate, endDate);
    }

    /**
     * @method : customerRoad
     * @desc : Customer 정보 pk 로 불러오기
     * @author : LeeChanSin
     */
    public CustomerLoadDTO customerRoad(Long id){
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();

            return CustomerLoadDTO.CustomerInfoDTO(customer);
        } else{
            return null;
        }
    }
}
