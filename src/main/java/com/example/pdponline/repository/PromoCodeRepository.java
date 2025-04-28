package com.example.pdponline.repository;

import com.example.pdponline.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    @Query("""
    SELECT p FROM PromoCode p
    WHERE (:active IS NULL OR p.active = :active)
      AND (:code IS NULL OR LOWER(p.promoCode) LIKE LOWER(CONCAT('%', :code, '%')))
      AND (:expiryDate IS NULL OR p.expiryDate = :expiryDate)
""")
    List<PromoCode> search(@Param("active") Boolean active,
                           @Param("code") String code,
                           @Param("expiryDate") LocalDate expiryDate);

    boolean existsByPromoCode(String code);

    @Modifying
    @Query("UPDATE PromoCode p SET p.active = false WHERE p.expiryDate < :now AND p.active = true")
    void deactivateExpiredPromoCodes(@Param("now") LocalDate now);

    Optional<PromoCode> findByPromoCode(String text);
}
