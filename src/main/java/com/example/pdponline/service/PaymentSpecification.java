package com.example.pdponline.service;

import com.example.pdponline.entity.Payment;
import com.example.pdponline.entity.PaymentModule;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentSpecification {

    public static Specification<Payment> filter(
            LocalDate startDate,
            LocalDate endDate,
            PayType payType,
            PaymentStatus status,
            Long studentId,
            Boolean hasPromoCode,
            Double startAmount,
            Double endAmount,
            List<Long> moduleIds
    ) {
        return (Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Sana bo'yicha filter
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("payDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("payDate"), endDate));
            }

            // To'lov turi
            if (payType != null) {
                predicates.add(cb.equal(root.get("payType"), payType));
            }

            // Status
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Student ID
            if (studentId != null) {
                predicates.add(cb.equal(root.get("student").get("id"), studentId));
            }

            // PromoCode mavjudmi yoki yo'qmi
            if (hasPromoCode != null) {
                if (hasPromoCode) {
                    predicates.add(cb.isNotNull(root.get("promoCode")));
                } else {
                    predicates.add(cb.isNull(root.get("promoCode")));
                }
            }

            // Summaga qarab filter
            if (startAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("originalAmount"), startAmount));
            }
            if (endAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("originalAmount"), endAmount));
            }

            // ModuleId bo'yicha filter qilish (subquery orqali)
            if (moduleIds != null && !moduleIds.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PaymentModule> pmRoot = subquery.from(PaymentModule.class);
                subquery.select(pmRoot.get("payment").get("id"))
                        .where(pmRoot.get("module").get("id").in(moduleIds));
                predicates.add(root.get("id").in(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
