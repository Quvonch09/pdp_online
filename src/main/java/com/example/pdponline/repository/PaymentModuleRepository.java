package com.example.pdponline.repository;

import com.example.pdponline.entity.PaymentModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentModuleRepository extends JpaRepository<PaymentModule,Long> {

    List<PaymentModule> findByPaymentId(Long paymentId);
}
