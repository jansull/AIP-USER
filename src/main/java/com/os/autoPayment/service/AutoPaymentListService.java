package com.os.autoPayment.service;

import com.os.autoPayment.dto.AutoDetailedSearchDTO;
import com.os.autoPayment.dto.AutoPaymentListDTO;
import com.os.autoPayment.entity.AutoPayment;
import com.os.repository.AutoPaymentRepository;
import com.os.util.enums.AutoOrderStatus;
import com.os.util.enums.AutoStatus;
import com.os.util.enums.BizTo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.os.autoPayment.entity.QAutoPayment.autoPayment;

@Service
@RequiredArgsConstructor
@Transactional
public class AutoPaymentListService {
    private final AutoPaymentRepository autoPaymentRepository;
    private final EntityManager em;

    /**
        @method : findAll
        @desc : 자동결제목록 전체조회하는 메서드
        @author : 김지훈
    */
    public Page<AutoPaymentListDTO> findAll(Pageable pageable) {
        Page<AutoPayment> allPaymentsPage = autoPaymentRepository.findAll(pageable);

        return allPaymentsPage.map(AutoPaymentListDTO::toAutoPaymentListDto);
    }

    /**
        @method : findByNameContaining
        @desc : 검색어로 자동결제목록 중 제목 검색하는 메서드
        @author : 김지훈
    */
    public Page<AutoPaymentListDTO> findByNameContaining(String keyword, Pageable pageable) {
        Page<AutoPayment> allPaymentsPage = autoPaymentRepository.findByPayment_Customer_CustomerNameAndAutoOrderStatus(keyword, AutoOrderStatus.paid, pageable);

        return allPaymentsPage.map(AutoPaymentListDTO::toAutoPaymentListDto);
    }

    /**
        @method : findByNameContaining
        @desc : AutoStatus 가 paid 인 자동결제목록 조회
        @author : 김지훈
    */
    public Page<AutoPaymentListDTO> findByAutoStatus(Pageable pageable) {
        Page<AutoPayment> allPaymentsPage = autoPaymentRepository.findByAutoStatus(AutoStatus.paid, pageable);

        return allPaymentsPage.map(AutoPaymentListDTO::toAutoPaymentListDto);
    }

    /**
        @method : detailSearch
        @desc : 자동결제목록 상세검색하는 메서드
        @author : 김지훈
    */
    public Page<AutoPaymentListDTO> detailSearch(AutoDetailedSearchDTO searchDTO, Pageable pageable) {
        String status = searchDTO.getStatus();                          // autoOrderStatus
        String DocNumber = searchDTO.getDocNumber();                    // paymentId
        String name = searchDTO.getCustomerName();                      // customerName
        String startDt = searchDTO.getStartDt();                        // startDate
        String endDt = searchDTO.getEndDt();                            // endDate
        String phoneNumber = searchDTO.getPhoneNumber();                // customerPhone
        String transactionStatus = searchDTO.getTransactionStatus();    // autoStatus
        String payType = searchDTO.getPayType();                        // paymentBizTo

        JPAQuery<AutoPayment> query = new JPAQuery<>(em);

        query.select(autoPayment)
                .from(autoPayment)
                .where(
                        eqDocNumber(DocNumber)
                        ,likeCustomerName(name)
                        ,eqStatus(status)
                        ,betweenDt(startDt,endDt)
                        ,likePhoneNumber(phoneNumber)
                        ,eqTransactionStatus(transactionStatus)
                        ,eqPayType(payType)
                )
                .fetch();

        long count = query.stream().count();

        List<AutoPayment> resultList = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AutoPaymentListDTO> result = convertToAllPaymentListDto(resultList);

        return new PageImpl<>(result,pageable,count);
    }

    /**
        @method : convertToAllPaymentListDto
        @desc : 자동결제목록을 AutoPaymentListDTO 로 변환하는 메서드
        @author : 김지훈
    */
    private List<AutoPaymentListDTO> convertToAllPaymentListDto(List<AutoPayment> autoPaymentList) {
        return autoPaymentList.stream()
                .map(AutoPaymentListDTO::toAutoPaymentListDto)
                .collect(Collectors.toList());
    }

    /**
        @method : eqDocNumber
        @desc : 주어진 번호에 해당하는 문서번호를 동등하게 비교하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression eqDocNumber(String DocNumber){
        if (StringUtils.hasText(DocNumber)){
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.payment.id.eq(Long.valueOf(DocNumber)));
        }
        return null;
    }

    /**
        @method : likeCustomerName
        @desc : 주어진 이름에 해당하는 고객 이름과 부분 일치하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression likeCustomerName(String name) {
        if (StringUtils.hasText(name)) {
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.payment.customer.customerName.like("%" + name + "%"));
        }
        return null;
    }

    /**
        @method : eqStatus
        @desc : 주어진 상태 값에 해당하는 autoOrderStatus 를 동등하게 비교하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression eqStatus(String status) {
        if (StringUtils.hasText(status)) {
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.autoOrderStatus.eq(AutoOrderStatus.valueOf(status)));
        }
        return null;
    }

    /**
        @method : betweenDt
        @desc : 시작일과 종료일 사이의 날짜 범위를 기준으로 자동 결제 업데이트 시간에 대한 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression betweenDt(String startDt ,String endDt) {
        if (StringUtils.hasText(startDt) && StringUtils.hasText(endDt)) {
            LocalDateTime startDate = LocalDateTime.parse(startDt + "T00:00:00");
            LocalDateTime endDate = LocalDateTime.parse(endDt + "T23:59:59");
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.payment.autoPayments.updateAt.between(startDate, endDate));
        }
        return null;
    }

    /**
        @method : likePhoneNumber
        @desc : 주어진 전화번호에 해당하는 고객 전화번호와 부분 일치하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression likePhoneNumber(String phoneNumber) {
        if (StringUtils.hasText(phoneNumber)) {
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.payment.customer.customerPhone.like("%" + phoneNumber + "%"));
        }
        return null;
    }

    /**
        @method : eqTransactionStatus
        @desc : 주어진 상태 값에 해당하는 autoStatus 를 동등하게 비교하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression eqTransactionStatus(String transactionStatus) {
        if (StringUtils.hasText(transactionStatus)) {
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.autoStatus.eq(AutoStatus.valueOf(transactionStatus)));
        }
        return null;
    }

    /**
        @method : eqPayType
        @desc : 주어진 유형에 해당하는 결제유형을 동등하게 비교하는 조건을 생성하는 메서드
        @author : 김지훈
    */
    private BooleanExpression eqPayType(String payType) {
        if (StringUtils.hasText(payType)) {
            return autoPayment.payment.paymentDelYn.eq('N').and(autoPayment.payment.paymentBizTo.eq(BizTo.valueOf(payType)));
        }
        return null;
    }
}