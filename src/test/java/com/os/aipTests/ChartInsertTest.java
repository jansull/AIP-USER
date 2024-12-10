package com.os.aipTests;

import com.os.autoPayment.entity.AutoPayment;
import com.os.customer.entity.Customer;
import com.os.memo.entity.Memo;
import com.os.payment.entity.Payment;
import com.os.product.entity.Product;
import com.os.repository.*;
import com.os.user.entity.User;
import com.os.util.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
public class ChartInsertTest {

    @Autowired
    public PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public MemoRepository memoRepository;
    @Autowired
    public AutoPaymentRepository autoPaymentRepository;




    @Test
    void softDeleteTest(){
        //memoRepository.softDeleteById(1L);
        List<Customer> entityList = customerRepository.findByPayments_PaymentDelYn('Y');
        for(Customer list : entityList){
            System.out.println(list.getPayments().getPaymentTitle());
        }
    }

    @Test
    void chart() {
        Optional<User> userOp = userRepository.findById(2L);
        LocalDateTime currentTime = LocalDateTime.now();
        if (userOp.isPresent()) {
            User user = userOp.get();

            // 랜덤 난수 객체 생성
            Random random = new Random();
            for (int i = 1; i < 70; i++) {
                // 월은 1부터 12까지의 랜덤한 값 설정
                int month = random.nextInt(12) + 1;
                // 일은 1부터 28까지의 랜덤한 값 설정 (월에 따라서 최대 일수가 달라질 수 있으니 적절히 수정하세요)
                int day = random.nextInt(28) + 1;
                // 월,날만 랜덤이고, 나머지는 현재 년도, 시간, 분, 초를 가져온다. 그리고, CreateAt이라는 변수에 저장
                LocalDateTime CreateAt = LocalDateTime.of(currentTime.getYear(), month, day, currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
                // Customer 생성
                Customer customer = new Customer();
                // 고객 이름
                customer.setCustomerName(i + "찬신");
                // 고객 이메일 주소
                customer.setCustomerEmail("test" + i + "@naver.com");
                // 고객 전화번호
                customer.setCustomerPhone("0101111" + i);
                // 고객 주소
                customer.setCustomerAddress("숭의" + i + "동");
                // 최초 시간
                customer.setCreateAt(CreateAt);
                // 변경된 시간
                customer.setUpdateAt(CreateAt);
                
                // payment 객체 생성
                Payment payment = new Payment();
                // 결제 제목 설정
                payment.setPaymentTitle("테스트제목" + i);
                // b to b 또는 b to c 거래 유형 설정
                payment.setPaymentBizTo(BizTo.BtoB);
                // 결제 내역 삭제 기본값을 N(No)으로 설정
                payment.setPaymentDelYn('N');
                // 최초 결제 시간
                payment.setCreateAt(CreateAt);
                // 마지막 결제 변경 시간
                payment.setUpdateAt(CreateAt);
                
                // i값이 30까지면 결제 유형을 basic으로 일괄 설정
                if (i < 31) {
                    payment.setPaymentType(OrderType.basic);
                    if (i < 10) {
                        // 그리고 1부터 9까지는 결제 상태를 wait 로,
                        payment.setPaymentStatus(OrderStatus.wait);
                    } else if (i < 20) {
                        // 10부터 19까지는 결제 상태를 paid 로,
                        payment.setPaymentStatus(OrderStatus.paid);
                        // 해당되지 않는 나머지는 결제 상태를 error 로 설정 
                    } else {
                        payment.setPaymentStatus(OrderStatus.error);
                    }
                    // 나머지 결제 유형은 전부 auto로 설정
                } else {
                    payment.setPaymentType(OrderType.auto);
                    // 20부터 42까지 주문상태를 wait 상태로,
                    if (i < 43) {
                        payment.setPaymentStatus(OrderStatus.wait);
                    // 43부터 55까지는 주문상태를 paid 상태로,
                    } else if (i < 56) {
                        payment.setPaymentStatus(OrderStatus.paid);
                    } //  나머지 (56부터 69까지) 는 주문상태를 error 상태로 설정
                    else {
                        payment.setPaymentStatus(OrderStatus.error);
                    }
                }

                List<Product> productList = new ArrayList<>();
                // 제품번호는 1부터 5까지 랜덤으로 생성
                int productNum = random.nextInt(5) + 1;
                for (int j = 1; j <= productNum; j++) {
                    // 새 product 객체 생성
                    Product product = new Product();
                    // 객체 이름을 상품 + j (1~5까지난수) 로 생성
                    product.setProductName("상품" + j);
                    // i 만큼 총 상품 갯수를 더해감
                    product.setProductTotalItems(i);
                    //  그냥 상품가격도 1~5까지의 랜덤숫자로 정했네
                    product.setProductPrice(j);
                    // 둘이 곱하면, 그것이 상품 규모
                    product.setProductAmount(j * i);
                    // 제품 리스트에 제품 추가
                    productList.add(product);
                    // 생성시간에 현재시간
                    product.setCreateAt(currentTime);
                    // 마지막으로 업데이트된 시간
                    product.setUpdateAt(currentTime);
                    product.setPayment(payment); // payment 설정
                }
                payment.setProducts(productList);
                customer.setUser(user);
                payment.setCustomer(customer);
                customer.setPayments(payment);

                // payment 저장
                customerRepository.save(customer);

                // ------------여기서부터 autopay관련-------------
                if (i > 42 && i < 56) {
                    // i가 42 초과이고 56 미만인 경우 AutoPayment를 생성하고 저장
                    AutoPayment autoPayment = new AutoPayment();
                    // 자동결제 최초시간
                    autoPayment.setCreateAt(CreateAt);
                    // 자동결제 업데이트시간
                    autoPayment.setUpdateAt(CreateAt);
                    // 주기적으로 자동결제 날짜를
                    autoPayment.setAutoPayDate(CreateAt.toLocalDate());
                    autoPayment.setAutoPayCount(1);

                    // 저장된 payment 설정
                    autoPayment.setPayment(payment);

                    if (i < 47) {
                        // 47이하까지는 자동(주문)결제상태를 stop으로
                        autoPayment.setAutoStatus(AutoStatus.stop);
                        autoPayment.setAutoOrderStatus(AutoOrderStatus.stop);
                    } else {
                        // 나머지는 자동결제상태를 paid로 설정하는데,
                        autoPayment.setAutoOrderStatus(AutoOrderStatus.paid);
                        autoPayment.setPaymentNextTime(CreateAt.plusMonths(1).withDayOfMonth(5));
                        if (i < 51) {
                            // 47부터 50사이면, 자동갱신상태를 error로
                            autoPayment.setAutoStatus(AutoStatus.error);
                            // 51이후로는 전부 paid 상태로 설정
                        } else {
                            autoPayment.setAutoStatus(AutoStatus.paid);
                        }
                    }
                    // payment 정보를 repository에 저장 
                    paymentRepository.save(payment);
                    // AutoPayment 저장
                    autoPaymentRepository.save(autoPayment);
                }
            }
        }
    }

    @Test
    void insertMemo() {
        Optional<User> userOp = userRepository.findById(1L);
        LocalDateTime currentTime = LocalDateTime.now();
        if(userOp.isPresent()) {
            User user = userOp.get();
            Random random = new Random();
            for(int i=0; i<=50; i++){
                // 월은 1부터 12까지의 랜덤한 값 설정
                int month = random.nextInt(12) + 1;
                // 일은 1부터 28까지의 랜덤한 값 설정 (월에 따라서 최대 일수가 달라질 수 있으니 적절히 수정하세요)
                int day = random.nextInt(28) + 1;
                LocalDateTime CreateAt = LocalDateTime.of(currentTime.getYear(), month, day, currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
                Memo memo = new Memo();
                memo.setMemoContents("메모테스트"+i);
                memo.setMemoDelYn("N");
                memo.setMemoExposeYn("Y");
                memo.setCreateAt(CreateAt);
                memo.setUpdateAt(CreateAt);
                memo.setUser(user);


                memoRepository.save(memo);
            }
        }


    }


}


