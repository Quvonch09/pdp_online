package com.example.pdponline.payload.req;

import com.example.pdponline.entity.enums.PayType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Builder
public record PaymentReq(
        @NotNull(message = "module id bo'sh bo'lishi mumkin emas!")
        List<Long> moduleIdList,
        @DefaultValue("PAYme")
        PayType payType,
        String promoCode
) {
}
