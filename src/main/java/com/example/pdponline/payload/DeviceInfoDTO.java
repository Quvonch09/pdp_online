package com.example.pdponline.payload;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceInfoDTO {

    private Long id;

    private String deviceAgent;

    private String browser;

    private String os;

    private String deviceType;

    private LocalDateTime loginTime;

    private Long userId;
}
