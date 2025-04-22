package com.example.pdponline.repository;

import com.example.pdponline.entity.Payment;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query("SELECT p FROM Payment p " +
            "WHERE p.payDate BETWEEN :startDate AND :endDate " +
            "AND (:studentId IS NULL OR p.student.id = :studentId) " +
            "AND (:payType IS NULL OR p.payType = :payType) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:promoCode IS NULL OR (p.promoCode IS NOT NULL AND :promoCode = true) " +
            "                       OR (p.promoCode IS NULL AND :promoCode = false)) " +
            "AND (:startAmount IS NULL OR p.originalAmount >= :startAmount) " +
            "AND (:endAmount IS NULL OR p.originalAmount <= :endAmount) ")
    List<Payment> findPayments(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("payType") PayType payType,
            @Param("status") PaymentStatus status,
            @Param("studentId") Long studentId,
            @Param("promoCode") Boolean promoCode,
            @Param("startAmount") Double startAmount,
            @Param("endAmount") Double endAmount,
            @Param("moduleIds") List<Long> moduleIds
    );

    @Query("SELECT COALESCE(SUM(p.paidAmount), 0) FROM Payment p WHERE p.status = :status")
    double getPaymentSum(@Param("status") PaymentStatus status);

}
