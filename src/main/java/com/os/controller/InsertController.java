package com.os.controller;

import com.os.customer.dto.InsertDTO;
import com.os.customer.service.InsertService;
import com.os.save.dto.SavePaymentDTO;
import com.os.save.dto.SavePaymentLoadDTO;
import com.os.save.dto.SaveProductDTO;
import com.os.save.entity.SaveProduct;
import com.os.user.entity.User;
import com.os.save.service.SavePaymentService;
import com.os.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InsertController {

    private final InsertService paymentService;
    private final SavePaymentService savePaymentService;
    private final UserService userService;

    /**
        @method : insertData
        @desc : 받아온 정보를 db에 insert 하는 메서드
        @author : 김성민
    */
    @PostMapping("/insert")
    public String insertData(@RequestBody @Valid InsertDTO dto) {
        System.out.println(dto.toString());
        User user = userService.findId();
        if (user != null) {
            paymentService.insert_basic(dto, user);
            return "ok";
        } else {
            return "bad";
        }
    }

    /**
        @method : save
        @desc : 받아온 정보를 db에 insert(임시저장) 하는 메서드
        @author : 김성민
    */
    @PostMapping("/save")
    public String save(@RequestBody SavePaymentDTO dto, RedirectAttributes redirectAttributes) {
        User user = userService.findId();
        System.out.println("dto = " + dto.toString());
        boolean res;
        if (user.getSavePayment() == null) {
            res = savePaymentService.save(dto, user);
        } else {
            res = savePaymentService.save_update(dto);
        }
        if(res) {
            redirectAttributes.addFlashAttribute("message", "저장이 완료되었습니다.");
            return "redirect:/insert_form";
        }
        return null;
    }

    /**
        @method : load
        @desc : 임시저장한 정보들을 select 해주는 메서드
        @author : 김성민
    */
    @GetMapping("/load")
    public SavePaymentLoadDTO load(){
        User user = userService.findId();
        long userId = user.getId();

        SavePaymentLoadDTO savePaymentLoad = savePaymentService.load_info(userId);
        long s_payment_id = savePaymentLoad.getId();
        List<SaveProduct> saveProduct = savePaymentService.load_list(s_payment_id);

        List<SaveProductDTO> productDTOList = saveProduct.stream()
                .map(this::convertToDTO) // 변환 메서드 호출
                .collect(Collectors.toList());
        savePaymentLoad.setProductList(productDTOList);
        return savePaymentLoad;
    }

    /**
        @method : load
        @desc : 임시저장한 결제물품정보들을 변환해주는 메서드
        @author : 김성민
    */
    private SaveProductDTO convertToDTO(SaveProduct saveProduct) {
        SaveProductDTO saveProductDTO = new SaveProductDTO();
        saveProductDTO.setS_productName(saveProduct.getS_productName());
        saveProductDTO.setS_productPrice(saveProduct.getS_productPrice());
        saveProductDTO.setS_productTotalItems(saveProduct.getS_productTotalItem());
        return saveProductDTO;
    }
}