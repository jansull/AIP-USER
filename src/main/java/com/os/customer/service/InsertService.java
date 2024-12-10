package com.os.customer.service;

import com.os.customer.dto.InsertDTO;
import com.os.customer.entity.Customer;
import com.os.product.dto.ProductDTO;
import com.os.payment.entity.Payment;
import com.os.product.entity.Product;
import com.os.user.entity.User;
import com.os.repository.CustomerRepository;
import com.os.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class InsertService {
    private final CustomerRepository customerRepository;

    /**
        @method : insert_basic
        @desc : 받아온 정보를 db에 insert 하는 메서드
        @author : 김성민
    */

    @Transactional
    public void insert_basic(InsertDTO dto, User user) {

            Customer customer = new Customer();
            customer.setCustomerName(dto.getCustomerName());
            customer.setCustomerEmail(dto.getCustomerEmail());
            customer.setCustomerPhone(dto.getCustomerPhone());
            customer.setCustomerAddress(dto.getCustomerAddress());
            customer.setCustomerKey(UUID.randomUUID());

            Payment payment = new Payment();
            payment.setPaymentTitle(dto.getPaymentTitle());
            payment.setPaymentType(dto.getPaymentType());
            payment.setPaymentBizTo(dto.getPaymentBizTo());
            payment.setPaymentDelYn('N');
            payment.setCreateAt(dto.getPaymentCreateAt());
            payment.setPaymentStatus(OrderStatus.wait);
            payment.setPaymentDelYn('N');

            payment.setPaymentFirstPay(dto.getPaymentFirstPay());
            payment.setPaymentMonth(dto.getPaymentMonth());
            payment.setPaymentAutoDate(dto.getAutoDate());

            List<Product> productList = new ArrayList<>();
            for (ProductDTO productDTO : dto.getProductList()) {
                Product product = new Product();
                product.setProductName(productDTO.getProductName());
                product.setProductTotalItems(productDTO.getProductTotalItems());
                product.setProductPrice(productDTO.getProductPrice());
                product.setProductAmount(productDTO.getProductAmount());
                productList.add(product);
                product.setPayment(payment);
            }

            payment.setProducts(productList);
            customer.setUser(user);
            payment.setCustomer(customer);
            customer.setPayments(payment);

            customerRepository.save(customer);

        }
    }

