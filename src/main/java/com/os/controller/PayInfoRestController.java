package com.os.controller;

import com.os.customer.dto.UpdateDTO;
import com.os.customer.service.UpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PayInfoRestController {
    private final UpdateService updateService;

    /**
     * @method : updateDetail
     * @desc : 일반결제 정보 변경 service 실행
     * @author : LeeChanSin
     */
    @PostMapping("/payDetailEdit")
    public void updateDetail(@RequestBody @Valid UpdateDTO updateDTO){
        if(updateDTO.isAutoOrBasic()){
            updateService.updateBasic(updateDTO);
        } else{
            updateService.updateAuto(updateDTO);
        }
    }

    /**
     * @method : deleteDetail
     * @desc : 일반결제 정보 soft delete service 실행
     * @author : LeeChanSin
     */
    @PostMapping("/payDetailDel")
    public void deleteDetail(@RequestBody @Valid UpdateDTO updateDTO){

        updateService.delUpdate(updateDTO);
    }

    /**
     * @method : autoPayStop
     * @desc : 자동결제 중지 service 실행
     * @author : LeeChanSin
     */
    @PostMapping("/autoPayStop")
    public void autoPayStop(@RequestBody @Valid UpdateDTO updateDTO){

        updateService.autoPayStop(updateDTO);
    }
}
