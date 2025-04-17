package com.example.pdponline.service;

import com.example.pdponline.entity.PromoCode;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.PromoCodeDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.PromoCodeReq;
import com.example.pdponline.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public ApiResponse<?> createPromoCode(PromoCodeReq req) {
        if (req.expiryDate().isBefore(java.time.LocalDate.now()) || req.expiryDate().isEqual(java.time.LocalDate.now())) {
            throw RestException.restThrow("amal qilish muddati xato!");
        }
        promoCodeRepository.findByPromoCode(req.promoCode()).ifPresent(promoCode -> {
            throw RestException.restThrow("promoCode mavjud!");
        });
        promoCodeRepository.save(PromoCode.builder().promoCode(req.promoCode()).description(req.description()).expiryDate(req.expiryDate()).percentage(req.percentage()).active(true).build());
        return ApiResponse.successResponse("success!");

    }

    public ApiResponse<PromoCodeDTO> getPromoCodeById(Long promoCodeId) {
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("PromoCode")));
        return ApiResponse.successResponse(getParseToPromoCodeDTO(promoCode));
    }

    private PromoCodeDTO getParseToPromoCodeDTO(PromoCode promoCode) {
        return PromoCodeDTO.builder().description(promoCode.getDescription()).expiryDate(promoCode.getExpiryDate()).promoCode(promoCode.getPromoCode()).percentage(promoCode.getPercentage()).active(promoCode.isActive()).build();
    }

    public ApiResponse<List<PromoCodeDTO>> getAllPromoCodes() {
        return ApiResponse.successResponse(promoCodeRepository.findAll().stream()
                .map(this::getParseToPromoCodeDTO)
                .toList());
    }

    public ApiResponse<PromoCodeDTO> getPromoCodeByName(String promoCode) {
        return ApiResponse.successResponse(getParseToPromoCodeDTO(promoCodeRepository.findByPromoCode(promoCode).orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("PromoCode")))));
    }

    public ApiResponse<String> updatePromoCode(Long promoCodeId, PromoCodeReq req) {
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("PromoCode")));
        promoCodeRepository.findByPromoCode(req.promoCode()).ifPresent(promoCode1 -> {
            throw RestException.restThrow("promoCode mavjud!");
        });

        if (req.expiryDate().isBefore(java.time.LocalDate.now()) || req.expiryDate().isEqual(java.time.LocalDate.now())) {
            throw RestException.restThrow("amal qilish muddati xato!");
        }
        promoCode.setPromoCode(req.promoCode());
        promoCode.setDescription(req.description());
        promoCode.setExpiryDate(req.expiryDate());
        promoCode.setPercentage(req.percentage());
        promoCode.setActive(true);
        promoCodeRepository.save(promoCode);
        return ApiResponse.successResponse("success!");
    }

    public ApiResponse<String> deletePromoCode(Long promoCodeIde) {
        PromoCode promoCode = promoCodeRepository.findById(promoCodeIde).orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("PromoCode")));
        promoCodeRepository.delete(promoCode);
        return ApiResponse.successResponse("success!");
    }
}
