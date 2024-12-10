package com.os.controller;

import com.os.autoPayment.dto.AutoDetailedSearchDTO;
import com.os.autoPayment.dto.AutoPaymentListDTO;
import com.os.autoPayment.service.AutoPaymentListService;
import com.os.autoPayment.service.AutoPaymentService;
import com.os.customer.service.CustomerService;
import com.os.memo.dto.MemoDTO;
import com.os.memo.service.MemoService;
import com.os.payment.dto.AllPaymentListDTO;
import com.os.payment.dto.DetailedSearchDTO;
import com.os.payment.dto.PaymentDetailsDTO;
import com.os.payment.service.AllPaymentListService;
import com.os.payment.service.PaymentService;
import com.os.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    public final UserService userService;
    public final AllPaymentListService allPaymentListService;
    public final AutoPaymentListService autoPaymentListService;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final AutoPaymentService autoPaymentService;
    private final MemoService memoService;

    @GetMapping
    public String index() {
        return "login";
    }

    @RequestMapping
    public String index(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return "/dashboard";
    }

    @GetMapping("dashboard")
    public String mainPage(Model model) {
        // 그래프
        List<Long> monthChart = paymentService.getCountsByMonthRange();
        List<Long> yearChart = paymentService.getCountsByYearRange();

        // 월별 통계
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        LocalDateTime startDate = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        long autoError = autoPaymentService.autoError(startDate, endDate);

        long autoSuccess = autoPaymentService.autoSuccess(startDate, endDate);

        long autoStop = autoPaymentService.autoStop(startDate, endDate);

        long autoAll = autoPaymentService.autoAll(startDate, endDate);
        List<MemoDTO> memoList = memoService.findAll(); // MemoService 를 사용하여 모든 메모를 가져옵니다.
        model.addAttribute("MemoList", memoList);

        model.addAttribute("month",month);
        model.addAttribute("autoError",autoError);
        model.addAttribute("autoSuccess",autoSuccess);
        model.addAttribute("autoStop",autoStop);
        model.addAttribute("autoAll",autoAll);

        model.addAttribute("monthChartList",monthChart);
        model.addAttribute("yearChartList",yearChart);
        model.addAttribute("paymentSuccessCount", customerService.countByCustomersByPaid(startDate, endDate));
        model.addAttribute("paymentInsertThisMonth", customerService.thisMonthInsertCount());

        return "dashboard";
    }

    /**
     * @method : chartDetail
     * @desc : 차트 상세 페이지 이동
     * @author : LeeChanSin
     */
    @GetMapping("/chartDetail")
    public String chartDetail(Model model){
        List<Long> monthChart = paymentService.getCountsByMonthRange();
        List<Long> yearChart = paymentService.getCountsByYearRange();

        model.addAttribute("monthChartList",monthChart);
        model.addAttribute("yearChartList",yearChart);
        return "chartDetail";
    }

    @GetMapping("/join")
    public String joinPage() { return "join"; }

    /**
    TODO: 2024-03-22 "paymentDetails.html" In the QR creation logic, I couldn't tell which code uses which code API, so I entered a random URL
          Please implement by adding payload to Payment Details DTO.
    */

    @GetMapping("/paymentDetails")
    public String paymentDetailsPage(@RequestParam Long id , Model model)   {
        PaymentDetailsDTO paymentDetailsDTO = customerService.getDetails(id);
        model.addAttribute("paymentDetailsDTO",paymentDetailsDTO);

        return "paymentDetails";
    }

    /**
     * @method : pageSample
     * @desc : 헤더 사이드바 적용된 예시 페이지 이동
     * @author : LeeChanSin
     */
    @GetMapping("pageSample")
    public String pageSample() {
        return "/pageSample";
    }

    /**
        @method : insert_form
        @desc : /insert_form 으로 이동
        @author : 김성민
    */
    @GetMapping("insert_form")
    public String insert_form(){
        return "insert_form";
    }

    @GetMapping("/list")
    public String findAll(Model model, @PageableDefault(sort = "UpdateAt", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(value = "keyword", required = false) String keyword) {
        Page<AllPaymentListDTO> allPaymentsPage;
        if (keyword != null && !keyword.isEmpty()) {
            allPaymentsPage = allPaymentListService.findByTitleContaining(keyword, pageable);
        } else {
            allPaymentsPage = allPaymentListService.findAll(pageable);
        }
        long payCount = allPaymentsPage.getTotalElements();
        model.addAttribute("payList", allPaymentsPage);
        model.addAttribute("payCount", payCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isListPage", true);

        return "list";
    }

    @PostMapping("/delete/{id}")
    public String deletePayment(@PathVariable String id) {
        Long paymentId = Long.parseLong(id);
        allPaymentListService.updatePaymentDelYnById(paymentId);

        return "redirect:/list";
    }


    @GetMapping("/search")
    public String search(@ModelAttribute("form") DetailedSearchDTO searchDTO,
                         @RequestParam(value = "startDt", required = false) String startDt,
                         @RequestParam(value = "endDt", required = false) String endDt,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "docNumber", required = false) String docNumber,
                         @RequestParam(value = "customerName", required = false) String customerName,
                         @RequestParam(value = "email", required = false) String email,
                         Model model,
                         @PageableDefault(sort = "UpdateAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // 검색 결과 가져오는 로직 구현
        Page<AllPaymentListDTO> allPaymentsPage = allPaymentListService.detailSearch(searchDTO, pageable);
        long payCount = allPaymentsPage.getTotalElements();

        System.out.println("payCount = " + payCount);

        // 검색에 사용된 파라미터들 모델에 추가
        model.addAttribute("payList", allPaymentsPage);
        model.addAttribute("payCount", payCount);
        model.addAttribute("startDt", startDt);
        model.addAttribute("endDt", endDt);
        model.addAttribute("status", status);
        model.addAttribute("docNumber", docNumber);
        model.addAttribute("customerName", customerName);
        model.addAttribute("email", email);
        model.addAttribute("isListPage", false);

        return "list";
    }


    @GetMapping("/autoList")
    public String autoFindAll(Model model, @PageableDefault(sort = "UpdateAt", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "autoStatusOnly", required = false, defaultValue = "false") boolean autoStatusOnly) {
        Page<AutoPaymentListDTO> allPaymentsPage;

        if (autoStatusOnly) {
            allPaymentsPage = autoPaymentListService.findByAutoStatus(pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            allPaymentsPage = autoPaymentListService.findByNameContaining(keyword, pageable);
        } else {
            allPaymentsPage = autoPaymentListService.findAll(pageable);
        }

        long payCount = allPaymentsPage.getTotalElements();
        model.addAttribute("payList", allPaymentsPage);
        model.addAttribute("payCount", payCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("autoStatusOnly", autoStatusOnly);
        model.addAttribute("isListPage", true);

        return "autoList";
    }

    @GetMapping("/autoSearch")
    public String autoSearch(@ModelAttribute("form") AutoDetailedSearchDTO searchDTO,
                         @RequestParam(value = "startDt", required = false) String startDt,
                         @RequestParam(value = "endDt", required = false) String endDt,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "docNumber", required = false) String docNumber,
                         @RequestParam(value = "customerName", required = false) String customerName,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                         @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
                         @RequestParam(value = "payType", required = false) String payType,
                         Model model,
                         @PageableDefault(sort = "UpdateAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // 검색 결과 가져오는 로직 구현
        Page<AutoPaymentListDTO> allPaymentsPage = autoPaymentListService.detailSearch(searchDTO, pageable);
        long payCount = allPaymentsPage.getTotalElements();

        System.out.println("payCount = " + payCount);

        // 검색에 사용된 파라미터들 모델에 추가
        model.addAttribute("payList", allPaymentsPage);
        model.addAttribute("payCount", payCount);
        model.addAttribute("startDt", startDt);
        model.addAttribute("endDt", endDt);
        model.addAttribute("status", status);
        model.addAttribute("docNumber", docNumber);
        model.addAttribute("customerName", customerName);
        model.addAttribute("email", email);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("transactionStatus", transactionStatus);
        model.addAttribute("payType", payType);
        model.addAttribute("isListPage", false);

        return "autoList";
    }
}
