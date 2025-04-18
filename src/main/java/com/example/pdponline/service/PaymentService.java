package com.example.pdponline.service;

import com.example.pdponline.entity.*;
import com.example.pdponline.entity.Module;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.PaymentReq;
import com.example.pdponline.repository.ModuleRepository;
import com.example.pdponline.repository.PaymentRepository;
import com.example.pdponline.repository.PromoCodeRepository;
import com.example.pdponline.repository.StudentValidModulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PromoCodeRepository promoCodeRepository;
    private final StudentValidModulesRepository studentValidModulesRepository;
    private final PaymentRepository paymentRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public ApiResponse<?> pay(PaymentReq req, User user) {

        double summa;
        Optional<PromoCode> byPromoCode = promoCodeRepository.findByPromoCode(req.promoCode());

        List<Module> modules = validateModules(req.moduleIdList());
        if (byPromoCode.isEmpty() && !req.promoCode().isBlank()) {
            return ApiResponse.errorResponse("promoCode topilmadi", 404);
        }

        if (byPromoCode.isEmpty() || req.promoCode().isBlank()) {
//            Agar promoCode yo'q bo'lsa chegirmalarsiz payment saqlanadi
            summa = getModulesPrice(modules);
            for (Module m : modules) {
                Payment payment = paymentRepository.save(Payment.builder().summa(m.getPrice()).moduleId(m.getId()).payType(req.payType()).promoCode(null).reason("Module sotib olish!").userId(user.getId()).build());
                studentValidModulesRepository.save(StudentValidModules.builder().moduleId(m.getId()).studentId(user.getId()).paymentId(payment.getId()).active(true).startDate(LocalDate.now()).endDate(LocalDate.now().plusYears(1)).build());
            }
            return ApiResponse.successResponseForMsg("Module sotib olish!");
        } else {
            //Agar kiritgan promoCode topilmasa Exeptionga otadi!
            PromoCode promoCode = promoCodeRepository.findByPromoCode(req.promoCode()).orElseThrow(
                    () -> RestException.restThrow(ResponseError.NOTFOUND("PromoCode"))
            );
            if (promoCode.isActive()) {
                // Agar promoCode active bo'lsa kiradi va chegirmalar bilan payment saqlanadi
                summa = getModulesPrice(modules, promoCode.getPercentage());

                for (Module m : modules) {
                    double sum = m.getPrice() - (m.getPrice() * (promoCode.getPercentage() / 100));
                    Payment payment = paymentRepository.save(Payment.builder().summa(sum).moduleId(m.getId()).payType(req.payType()).promoCode(promoCode).reason("Module sotib olish! Chegirma bilan").userId(user.getId()).build());
                    studentValidModulesRepository.save(StudentValidModules.builder().moduleId(m.getId()).studentId(user.getId()).paymentId(payment.getId()).active(true).startDate(LocalDate.now()).endDate(LocalDate.now().plusYears(1)).build());
                }
                return ApiResponse.successResponseForMsg("Module sotib olish! Chegirma bilan");
            } else {
//                Agar promoCode active bo'lmasa Exeptionga otadi!
                return ApiResponse.errorResponse("promoCode active emas!", 404);
            }
        }
    }

    //  promoCodesiz summa hisoblaydi
    private double getModulesPrice(List<Module> list) {
        double summa = 0.0;
        for (Module m : list) {
            summa += m.getPrice();
        }
        return summa;
    }

    // promoCode bilan summani hisoblaydi
    private double getModulesPrice(List<Module> list, double percentage) {
        double summa = 0.0;
        for (Module m : list) {
            summa += m.getPrice();
        }
        double discount = summa * (percentage / 100);
        return summa - discount;
    }

    private List<Module> validateModules(List<Long> list) {
        List<Module> modules = moduleRepository.findAllById(list);

        for (Module m : modules) {
            if (!m.isActive()) {
                throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Module active emas!"));
            }
        }

        if (modules.size() != list.size()) {
            throw RestException.restThrow(ResponseError.NOTFOUND("Module"));
        }
        return modules;
    }


}
