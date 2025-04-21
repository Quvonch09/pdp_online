package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status;
    @NotNull
    private Double amount;
    @NotBlank
    private String account;
    private String storeId;
    private String terminalId;
}
