package com.example.pdponline.service;

import
        com.example.pdponline.entity.*;
import com.example.pdponline.entity.Module;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.ModuleMapper;
import com.example.pdponline.mapper.PaymentMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ModuleDto;
import com.example.pdponline.payload.PaymentDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.PaymentReq;
import com.example.pdponline.repository.ModuleRepository;
import com.example.pdponline.repository.PaymentModuleRepository;
import com.example.pdponline.repository.PaymentRepository;
import com.example.pdponline.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModuleRepository moduleRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final PaymentModuleRepository paymentModuleRepository;

    /**
     * payment saqlash
     * @param student to'lov qilayotgan student
     * @param req to'lov uchun reuqest malumotlar
     * @param type to'lov turi (naqd yoki payme)
     * @return oxirgi to'lanishi kerakli summa
     */
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

        Payment payment = paymentRepository.save(Payment.builder()
                .originalAmount(totalPrice)
                .paidAmount(paidPrice)
                .payType(type)
                .promoCode(promoCode)
                .status(PaymentStatus.WAITING)
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

        return ApiResponse.successResponse(payment.getId(),"Sotib olish uchun summa");
    }

    /**
     * paymentni tasdiqlash
     * @param paymentId o'zgaradigan payment idsi
     * @param status response statusi
     * @return oxirgi nusxa payment
     */
    public ApiResponse<?> verifyPayment(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Payment")));

        payment.setStatus(status);
        paymentRepository.save(payment);
        log.info("Payment status o'zgartirildi: {}", paymentId);

        List<ModuleDto> moduleDtos = paymentModuleRepository.findByPaymentId(paymentId).stream()
                .map(pm -> ModuleMapper.toDto(pm.getModule()))
                .toList();

        return ApiResponse.successResponse(PaymentMapper.toDto(moduleDtos, payment));
    }

    /**
     * filtr asosida paymentlarni olish
     * @param startDate vaqt oralig'i boshlanish
     * @param endDate vaqt oralig'i tugash
     * @param type naqd yoki payme
     * @param status succes,failed,returned,waiting
     * @param studentId to'lov qilgan student id
     * @param promoCode promocode bo'yicha
     * @param startAmount to'lov oralig'i boshlanish
     * @param endAmount to'lov oralig'i tugash
     * @param moduleIds modullar bo'yicha
     * @return payment lar listi
     */
    @Transactional
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
        Specification<Payment> spec = PaymentSpecification.filter(
                startDate, endDate, type, status,
                studentId, promoCode, startAmount, endAmount, moduleIds);

        List<Payment> payments = paymentRepository.findAll((Sort) spec);

        if (payments.isEmpty()) {
            log.warn("Paymentlar mavjud emas");
            throw RestException.restThrow(ResponseError.NOTFOUND("Payment"));
        }

        List<PaymentDTO> paymentDTOS = payments.stream().map(payment -> {
            List<ModuleDto> moduleDtos = paymentModuleRepository.findByPaymentId(payment.getId()).stream()
                    .map(pm -> ModuleMapper.toDto(pm.getModule()))
                    .toList();
            return PaymentMapper.toDto(moduleDtos, payment);
        }).toList();

        log.info("Paymentlar topildi: {}", paymentDTOS.size());
        return ApiResponse.successResponse(paymentDTOS);
    }
}