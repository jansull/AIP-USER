package com.os.aipTests;

import com.os.customer.entity.Customer;
import com.os.payment.entity.Payment;
import com.os.product.entity.Product;
import com.os.util.enums.BizTo;
import com.os.util.enums.OrderStatus;
import com.os.util.enums.OrderType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsDTOTest {

    @Test
    public void testPaymentDetailsDTOCreation() {
        // 가짜 Customer 객체 생성
        Customer customer = Customer.builder()
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .customerAddress("123 Main St, Any town")
                .build();

        // 가짜 Product 객체 생성
        Product product1 = Product.builder()
                .productName("Product 1")
                .productTotalItems(1)
                .productPrice(100)
                .productAmount(2000)
                .build();

        Product product2 = Product.builder()
                .productName("Product 2")
                .productTotalItems(2)
                .productPrice(50)
                .productAmount(1000)
                .build();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        // 가짜 Payment 객체 생성
        Payment payment = Payment.builder()
                .paymentTitle("Payment")
                .paymentType(OrderType.auto)
                .paymentBizTo(BizTo.BtoC)
                .paymentMemo("Memo")
                .paymentStatus(OrderStatus.paid)
                .paymentDelYn('N')
                .paymentMonth(1)
//                .paymentNextTime(LocalDateTime.now().plusMonths(1))
                .paymentFirstPay(50)
                .products(products)
                .build();


        customer.setPayments(payment);

    }
}
