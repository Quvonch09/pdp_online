package com.example.pdponline.service;

import com.example.pdponline.entity.PromoCode;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.PromoCodeReq;
import com.example.pdponline.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * promocode yaratish
     * @param req promocode saqlash uchun request class
     * @return promocode
     */
    public ApiResponse<?> createPromoCode(PromoCodeReq req) {
        if (req.expiryDate().isBefore(LocalDate.now())) {
            log.error("Noto‘g‘ri expiryDate: {}", req.expiryDate());
            throw RestException.restThrow(ResponseError.DEFAULT_ERROR("Bugungi kundan oldin bo'lishi mumkin emas"));
        }

        String code;
        do {
            code = generateRandomString();
        } while (promoCodeRepository.existsByPromoCode(code));

        PromoCode promoCode = PromoCode.builder()
                .promoCode(code)
                .description(req.description())
                .percentage(req.percentage())
                .active(true)
                .expiryDate(req.expiryDate())
                .build();

        PromoCode saved = promoCodeRepository.save(promoCode);
        log.info("Promo kod saqlandi: {}", saved.getPromoCode());
        return ApiResponse.successResponse(saved);
    }

    /**
     *
     * @param active active/inactive status
     * @param text promocode text
     * @param expiryDate promocode amal qilish muddati
     * @return promocode list
     */
    public ApiResponse<?> getPromoCodes(Boolean active,String text,LocalDate expiryDate){
        List<PromoCode> promoCodes = promoCodeRepository.search(active, text, expiryDate);

        if (promoCodes.isEmpty()) {
            log.error("Promo kodlar topilmadi");
            throw RestException.restThrow(ResponseError.NOTFOUND("Promo kodlar"));
        }

        log.info("Promo kodlar olindi. Soni: {}", promoCodes.size());
        return ApiResponse.successResponse(promoCodes);
    }

    /**
     * promocode active sini false qilish
     * @param id kerakli promocode id si
     * @return promocode o'chirildi nomli response
     */
    public ApiResponse<?> unActive(Long id){
        PromoCode promoCode = promoCodeRepository.findById(id)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Promokod")));

        promoCode.setActive(false);
        promoCodeRepository.save(promoCode);

        log.info("promokod aktivi false qilindi");
        return ApiResponse.successResponseForMsg("Promokod o'chirildi");
    }

    /**
     * avtomatik muddati o'tgan promocode larni o'chirish
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    @Async
    public void autoUnActive() {
        promoCodeRepository.deactivateExpiredPromoCodes(LocalDate.now());
        log.info("Muddati o'tgan promo kodlar o'chirildi");
    }

    /**
     * tasodifiy string promocode generatsiya qilish
     * @return 10 talik string
     */
    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
