package com.os.save.service;

import com.os.save.dto.SavePaymentDTO;
import com.os.save.dto.SavePaymentLoadDTO;
import com.os.save.dto.SaveProductDTO;
import com.os.save.entity.SavePayment;
import com.os.save.entity.SaveProduct;
import com.os.user.entity.User;
import com.os.repository.SavePaymentRepository;
import com.os.repository.SaveProductRepository;
import com.os.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class SavePaymentService {
    private final SavePaymentRepository savePaymentRepository;
    private final SaveProductRepository saveproductRepository;
    private final UserService userService;

    /**
        @method : save
        @desc : 받아온 정보를 db에 insert(임시저장) 하는 메서드
        @author : 김성민
    */

    public boolean save(SavePaymentDTO dto, User user) {
        SavePayment savePayment = SavePayment.ToEntity(dto, user);
        savePaymentRepository.save(savePayment);

        return true;
    }

    /**
        @method : save_update
        @desc : 받아온 정보를 db에 insert 하는 메서드
        @author : 김성민
    */

    public boolean save_update(SavePaymentDTO dto){
        User user = userService.findId();

        if (user != null) {
            Optional<SavePayment> optionalSavePayment = savePaymentRepository.findByUserId(user.getId());

            if (optionalSavePayment.isPresent()) {
                SavePayment savePayment = optionalSavePayment.get();
                List<SaveProduct> savedProducts = savePayment.getSaveProducts();
                List<SaveProductDTO> newProducts = dto.getProductList();

                int savedCount = savedProducts.size();
                System.out.println("저장본 : " + savedCount);
                int newCount = newProducts.size();
                System.out.println("넣을거 : " + newCount);
                if (savedCount > newCount) {
                    // 보낸 정보 개수만큼만 업데이트
                    for (int i = 0; i < newCount; i++) {
                        SaveProduct savedProduct = savedProducts.get(i);
                        SaveProductDTO newProduct = newProducts.get(i);

                        // 업데이트
                        savedProduct.setS_productName(newProduct.getS_productName());
                        savedProduct.setS_productTotalItem(newProduct.getS_productTotalItems());
                        savedProduct.setS_productPrice(newProduct.getS_productPrice());

                        saveproductRepository.save(savedProduct);
                    }
                    for (int i = newCount; i < savedCount; i++) {
                        SaveProduct savedProduct = savedProducts.get(i);
                        saveproductRepository.delete(savedProduct);
                    }
                }
                else {
                    // 보낸 정보 업데이트
                    for (int i = 0; i < savedCount; i++) {
                        SaveProduct savedProduct = savedProducts.get(i);
                        SaveProductDTO newProduct = newProducts.get(i);

                        // 업데이트
                        savedProduct.setS_productName(newProduct.getS_productName());
                        savedProduct.setS_productTotalItem(newProduct.getS_productTotalItems());
                        savedProduct.setS_productPrice(newProduct.getS_productPrice());

                        saveproductRepository.save(savedProduct);
                    }
                    for (int i = savedCount; i < newCount; i++) {
                        SaveProductDTO newProduct = newProducts.get(i);
                        SaveProduct saveProduct = new SaveProduct();
                        saveProduct.setS_productName(newProduct.getS_productName());
                        saveProduct.setS_productTotalItem(newProduct.getS_productTotalItems());
                        saveProduct.setS_productPrice(newProduct.getS_productPrice());
                        saveProduct.setSavePayment(savePayment);

                        saveproductRepository.save(saveProduct);
                    }
                }
                // 새로운 정보로 업데이트합니다.
                savePayment.setS_paymentName(dto.getS_paymentName());
                savePayment.setS_paymentEmail(dto.getS_paymentEmail());
                savePayment.setS_paymentPhone(dto.getS_paymentPhone());
                savePayment.setS_paymentAddress(dto.getS_paymentAddress());
                savePayment.setS_paymentTitle(dto.getS_paymentTitle());
                savePayment.setS_paymentType(dto.getS_paymentType());
                savePayment.setS_paymentFirstPay(dto.getS_paymentFirstPay());
                savePayment.setS_paymentBizTo(dto.getS_paymentBizTo());
                savePayment.setS_paymentCycle(dto.getS_paymentCycle());
                savePayment.setS_paymentDate(dto.getS_paymentDate());
                savePayment.setS_paymentPay(dto.getS_paymentPay());

                // 엔티티를 저장합니다.
                savePaymentRepository.save(savePayment);
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
        @method : load_info
        @desc : 임시저장한 결제등록정보를 select 하는 메서드
        @author : 김성민
    */

    public SavePaymentLoadDTO load_info(long userId) {
        Optional<SavePayment> optionalSavePayment = savePaymentRepository.findByUserId(userId);
        SavePayment savePayment = optionalSavePayment.orElse(null);

        SavePaymentLoadDTO dto = new SavePaymentLoadDTO();
        if (savePayment != null) {
            BeanUtils.copyProperties(savePayment, dto);
        }
        return dto;
    }

    /**
        @method : load_list
        @desc : 임시저장한 결제물품등록정보를 select 하는 메서드
        @author : 김성민
    */
    public List<SaveProduct> load_list(long s_payment_id) {
        return saveproductRepository.findBySavePaymentId(s_payment_id);
    }
}
