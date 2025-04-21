package com.example.pdponline.service;

import com.example.pdponline.entity.*;
import com.example.pdponline.entity.Module;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.PaymentReq;
import com.example.pdponline.repository.ModuleRepository;
import com.example.pdponline.repository.PaymentModuleRepository;
import com.example.pdponline.repository.PaymentRepository;
import com.example.pdponline.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModuleRepository moduleRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final PaymentModuleRepository paymentModuleRepository;

    @Transactional
    public ApiResponse<?> buyModule(User student, PaymentReq req, PayType type) {
        List<Module> modules = moduleRepository.findAllByIdInAndActiveTrue(req.moduleIds());
        if (modules.size() != req.moduleIds().size()) {
            log.error("Modullar topilmadi: {}",req.moduleIds());
            throw RestException.restThrow(ResponseError.NOTFOUND("Modulelar"));
        }

        double totalPrice = modules.stream().mapToDouble(Module::getPrice).sum();
        PromoCode promoCode = null;
        double paidPrice = totalPrice;

        if (req.promoCode() != null && !req.promoCode().isBlank()) {
            promoCode = promoCodeRepository.findByPromoCode(req.promoCode())
                    .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Promokod")));
            if (!promoCode.isActive()) {
                log.error("Promokod faol emas: {}",req.promoCode());
                throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Promokod aktiv emas"));
            }
            paidPrice -= totalPrice * promoCode.getPercentage() / 100;
        }

        if (req.amount() < paidPrice) {
            log.error("To'lov summasi belgilangan summadan kam: {}",paidPrice);
            throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Summa yetarli emas"));
        }

        Payment payment = paymentRepository.save(Payment.builder()
                .originalAmount(totalPrice)
                .paidAmount(paidPrice)
                .payType(type)
                .promoCode(promoCode)
                .status(PaymentStatus.SUCCESS)
                .student(student)
                .build());
        log.info("To'lov saqlandi: {}",payment.getId());

        paymentModuleRepository.saveAll(modules.stream()
                .map(module -> PaymentModule.builder()
                        .payment(payment)
                        .module(module)
                        .active(true)
                        .priceAtPurchase(module.getPrice())
                        .build())
                .toList());
        log.info("{} ta modul sotib olindi",modules.size());

        return ApiResponse.successResponseForMsg("Modullar sotib olindi");
    }

    public ApiResponse<?> getPayments(
            LocalDate startDate,
            LocalDate endDate,
            PayType type,
            PaymentStatus status,
            Long studentId,
            Boolean promoCode,
            Double startAmount,
            Double endAmount,
            List<Long> moduleIds
    ) {
        List<Payment> payments = paymentRepository.findPayments(startDate, endDate, type, status, studentId, promoCode, startAmount, endAmount, moduleIds);

        if (payments.isEmpty()) {
            log.error("To'lovlar topilmadi");
            throw RestException.restThrow(ResponseError.NOTFOUND("To'lovlar"));
        }

        log.info("To'lovlar olindi");
        return ApiResponse.successResponse(payments);
    }
}