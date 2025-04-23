package com.example.pdponline.repository;

import com.example.pdponline.entity.Payment;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long>, JpaSpecificationExecutor<Payment> {

    @Query(value = """
SELECT DISTINCT p.*
FROM payment p
         LEFT JOIN payment_modules pm ON pm.payment_id = p.id
         LEFT JOIN promo_code pc ON p.promo_code_id = pc.id
         LEFT JOIN module m ON pm.module_id = m.id
WHERE (:startDate IS NULL OR :endDate IS NULL OR p.pay_date BETWEEN :startDate AND :endDate)
  AND (:studentId IS NULL OR p.student_id = :studentId)
  AND (:payType IS NULL OR p.pay_type = :payType)
  AND (:status IS NULL OR p.status = :status)
  AND (
    :promoCode IS NULL OR
    (:promoCode = true AND pc.id IS NOT NULL) OR
    (:promoCode = false AND pc.id IS NULL)
  )
  AND (:startAmount IS NULL OR p.original_amount >= :startAmount)
  AND (:endAmount IS NULL OR p.original_amount <= :endAmount)
  AND (
    :moduleIds IS NULL OR (
        p.id =  (select p1.id from payment p1 join public.payment_modules pm2 on p1.id = pm2.payment_id
                        where pm2.module_id in (:moduleIds))
    )
)
""", nativeQuery = true)
    List<Payment> findPayments(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("payType") String payType,
            @Param("status") String status,
            @Param("studentId") Long studentId,
            @Param("promoCode") Boolean promoCode,
            @Param("startAmount") Double startAmount,
            @Param("endAmount") Double endAmount,
            @Param("moduleIds") List<Long> moduleIds
    );


    @Query("SELECT COALESCE(SUM(p.paidAmount), 0) FROM Payment p WHERE p.status = :status")
    double getPaymentSum(@Param("status") PaymentStatus status);

    List<Payment> findByStudentId(Long studentId);

}
