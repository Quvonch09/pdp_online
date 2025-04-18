package com.example.pdponline.repository;

import com.example.pdponline.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> findByPromoCode(String promoCode);

    List<PromoCode> getAllByActive(boolean active);
}
