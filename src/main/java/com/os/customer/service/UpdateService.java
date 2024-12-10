package com.os.customer.service;

import com.os.customer.dto.UpdateDTO;
import com.os.customer.entity.Customer;
import com.os.product.dto.ProductDTO;
import com.os.autoPayment.entity.AutoPayment;
import com.os.payment.entity.Payment;
import com.os.product.entity.Product;
import com.os.repository.AutoPaymentRepository;
import com.os.repository.CustomerRepository;
import com.os.repository.PaymentRepository;
import com.os.repository.ProductRepository;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UpdateService {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final AutoPaymentRepository autoPaymentRepository;

    /**
     * @method : updateBasic
     * @desc : 일반결제 정보 변경
     * @author : LeeChanSin
     */
    public void updateBasic(UpdateDTO updateDTO){
        if(!updateDTO.getProductDelCheck().isEmpty()){

            for(Long id : updateDTO.getProductDelCheck()){
                deleteProduct(id);
            }
        }

        Optional<Customer> customerOptional = customerRepository.findById(updateDTO.getCustomerId());

        if (customerOptional.isPresent()) {

            Customer customer= customerOptional.get();

            customer.setCustomerName(updateDTO.getCustomerName());
            customer.setCustomerEmail(updateDTO.getCustomerEmail());
            Optional<Payment> paymentOptional = paymentRepository.findByCustomerIdAndPaymentDelYn(customer.getId(), 'N');

            if(paymentOptional.isPresent()){
                Payment payment = paymentOptional.get();
                payment.setPaymentTitle(updateDTO.getPaymentTitle());
                List<Product> productList = productRepository.findByPaymentId(payment.getId());
                List<ProductDTO> updatedProductList = updateDTO.getProductList();

                for (ProductDTO productDTO : updatedProductList) {

                    if(productDTO.getId() == 0){

                        saveProduct(productDTO, payment);
                    }
                }
                // 제품 목록을 반복하며 업데이트 수행
                for (Product product : productList) {
                        // 업데이트할 제품을 찾습니다.
                        Optional<ProductDTO> updatedProductDTOOptional = updatedProductList.stream()
                                .filter(dto -> dto.getId().equals(product.getId()))
                                .findFirst();

                        if (updatedProductDTOOptional.isPresent()) {
                            ProductDTO updatedProductDTO = updatedProductDTOOptional.get();
                            product.setProductName(updatedProductDTO.getProductName());
                            product.setProductTotalItems(updatedProductDTO.getProductTotalItems());
                            product.setProductPrice(updatedProductDTO.getProductPrice());
                            product.setProductAmount(updatedProductDTO.getProductAmount());
                        }
                    payment.setProducts(productList);
                }

                Optional<AutoPayment> autoPaymentOptional = autoPaymentRepository.findByPaymentId(payment.getId());
                if(autoPaymentOptional.isPresent()){
                    AutoPayment autoPayment = autoPaymentOptional.get();
                    autoPayment.setPaymentNextTime(LocalDateTime.parse(updateDTO.getPaymentNextTime()));
                    payment.setAutoPayments(autoPayment);
                }

                payment.setCustomer(customer);
                customer.setPayments(payment);

                customerRepository.save(customer);

            }
        }
    }

    /**
     * @method : deleteProduct
     * @desc : product 정보 삭제
     * @author : LeeChanSin
     */
    public void deleteProduct(Long id){

        productRepository.deleteById(id);
    }

    /**
     * @method : savePro
     * @desc : product 정보 insert
     * @author : LeeChanSin
     */
    public void saveProduct(ProductDTO productDTO, Payment payment){
        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .productTotalItems(productDTO.getProductTotalItems())
                .productPrice(productDTO.getProductPrice())
                .productAmount(productDTO.getProductAmount())
                .payment(payment)
                .build();

        productRepository.save(product);

    }
    /**
     * @method : delUpdate
     * @desc : payment 정보 soft delete
     * @author : LeeChanSin
     */
    public void delUpdate(UpdateDTO updateDTO) {
        Long paymentId = updateDTO.getPaymentId();

        // paymentId를 이용하여 엔티티를 조회합니다.
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            // 변경하고자 하는 컬럼 값을 업데이트합니다.
            payment.setPaymentDelYn('Y');

            // 업데이트된 엔티티를 저장합니다.
            paymentRepository.save(payment);

        }
    }


    /**
     * @method : updateAuto
     * @desc : 자동결제 정보 변경
     * @author : LeeChanSin
     */
    public void updateAuto(UpdateDTO updateDTO){

        Optional<Customer> customerOptional = customerRepository.findById(updateDTO.getCustomerId());

        if (customerOptional.isPresent()) {

            Customer customer= customerOptional.get();

            customer.setCustomerName(updateDTO.getCustomerName());
            customer.setCustomerEmail(updateDTO.getCustomerEmail());
            customer.setCustomerAddress(updateDTO.getCustomerAddress());
            customer.setCustomerPhone(updateDTO.getCustomerPhone());
            Optional<Payment> paymentOptional = paymentRepository.findByCustomerIdAndPaymentDelYn(customer.getId(), 'N');

            if(paymentOptional.isPresent()){
                Payment payment = paymentOptional.get();
                payment.setPaymentMemo(updateDTO.getPaymentMemo());

                Optional<AutoPayment> autoPaymentOptional = autoPaymentRepository.findByPaymentId(payment.getId());
                if(autoPaymentOptional.isPresent()){
                    AutoPayment autoPayment = autoPaymentOptional.get();
                    String dateString = updateDTO.getPaymentNextTime().replace(" ", "T");
                    autoPayment.setPaymentNextTime(LocalDateTime.parse(dateString));

                    payment.setAutoPayments(autoPayment);
                    payment.setCustomer(customer);
                    customer.setPayments(payment);

                    customerRepository.save(customer);
                }

            }
        }
    }

    /**
     * @method : autoPayStop
     * @desc : 자동결제 중지
     * @author : LeeChanSin
     */
    public void autoPayStop(UpdateDTO updateDTO){

        Optional<AutoPayment> autoPaymentOptional = autoPaymentRepository.findById(updateDTO.getAutoPaymentId());
        if(autoPaymentOptional.isPresent()){
            AutoPayment autoPayment = autoPaymentOptional.get();
            autoPayment.setAutoStatus(AutoStatus.stop);
            autoPayment.setAutoOrderStatus(AutoOrderStatus.stop);
            autoPayment.setPaymentNextTime(null);
            autoPaymentRepository.save(autoPayment);
        }
    }
}
