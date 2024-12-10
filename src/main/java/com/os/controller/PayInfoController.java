package com.os.controller;

import com.os.autoPayment.dto.AutoPaymentDTO;
import com.os.autoPayment.service.AutoPaymentService;
import com.os.customer.dto.CustomerLoadDTO;
import com.os.customer.service.CustomerService;
import com.os.payment.dto.PaymentDTO;
import com.os.payment.service.PaymentService;
import com.os.product.dto.ProductDTO;
import com.os.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


@Controller

public class PayInfoController {

    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final AutoPaymentService autoPaymentService;

    /**
     * @method : PayInfoController
     * @desc : service class 와 토스 api key 생성자 주입
     * @author : LeeChanSin
     */
    public PayInfoController(CustomerService customerService, AutoPaymentService autoPaymentService, @Value("${api.toss.secretKey}") String secretKey, PaymentService paymentService1, ProductService productService) {
        this.customerService = customerService;
        this.autoPaymentService = autoPaymentService;
        this.paymentService = paymentService1;
        this.productService = productService;
        this.secretKey = secretKey;

    }
    private final String secretKey;
    /**
     * @method : payInfoDetail
     * @desc : 결제정보 상세페이지 이동
     * @author : LeeChanSin
     */
    @GetMapping("/payInfoDetail/")
    public String payInfoDetail(@RequestParam long id, Model model) {
        CustomerLoadDTO customerInfo = customerService.customerRoad(id);
        PaymentDTO payInfo = paymentService.paymentRoad(customerInfo.getId());
        if(payInfo == null){
            return "redirect:/dashboard";
        }

        List<ProductDTO> productInfo = productService.productRoad(payInfo.getId());
        model.addAttribute("customerInfo", customerInfo);
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("productInfo", productInfo);

        return "payList/basicPayInfoDetail";
    }

    /**
     * @method : payInfoDetail
     * @desc : 자동결제 상세페이지 이동
     * @author : LeeChanSin
     */
    @GetMapping("/payAutoDetail/")
    public String payAutoDetail(@RequestParam long id, Model model) {
        CustomerLoadDTO customerInfo = customerService.customerRoad(id);
        PaymentDTO payInfo = paymentService.paymentRoad(customerInfo.getId());
        AutoPaymentDTO autoPayInfo = autoPaymentService.autoPayRoad(payInfo.getId());
        if(autoPayInfo == null){
            return "redirect:/dashboard";
        }

        List<ProductDTO> productInfo = productService.productRoad(payInfo.getId());
        model.addAttribute("customerInfo", customerInfo);
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("productInfo", productInfo);
        model.addAttribute("autoPayInfo", autoPayInfo);

        return "payList/autoPayInfoDetail";
    }

    /**
     * @method : receipt
     * @desc : 신용구매도표 상세페이지로 이동
     * @author : LeeChanSin
     */
    @GetMapping("/receipt/")
    public String receipt(@RequestParam("paymentId") Long paymentId,
                          @RequestParam("customerId") Long customerId,
                          Model model) throws IOException {
        String orderId = "order_aip"+paymentId;



        // 토스페이먼츠 API 는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // 시크릿키 encoding
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        HttpURLConnection connection = getHttpURLConnection(encodedBytes, orderId);
        // 응답 코드 확인
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder jsonResponse = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonResponse.append(line);
        }
        reader.close();

// 모델에 추가

        CustomerLoadDTO customerInfo = customerService.customerRoad(customerId);
        PaymentDTO payInfo = paymentService.paymentRoad(customerInfo.getId());
        List<ProductDTO> productInfo = productService.productRoad(payInfo.getId());

        model.addAttribute("cardInfo", jsonResponse);
        model.addAttribute("customerInfo", customerInfo);
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("productInfo", productInfo);

        return "payList/receipt";
    }

    private static HttpURLConnection getHttpURLConnection(byte[] encodedBytes, String orderId) throws IOException {
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API 를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/orders/"+ orderId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        return connection;
    }
}
