package com.example.pdponline.repository;

import com.example.pdponline.entity.PaymentModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentModuleRepository extends JpaRepository<PaymentModule,Long> {

    List<PaymentModule> findByPaymentId(Long paymentId);

    @Query(value = """
            SELECT EXISTS(
                SELECT 1
                FROM payment_modules pm
                         JOIN payment p ON p.id = pm.payment_id
                WHERE p.student_id = :studentId
                  AND pm.module_id = :moduleId
            ) AS purchased
            """, nativeQuery = true)
    boolean existsByStudent_idAndModule_id(Long studentId, Long moduleId);
}
