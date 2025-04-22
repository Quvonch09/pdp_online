package com.example.pdponline.payload;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceInfoDTO implements Serializable {

    private Long id;

    private String deviceAgent;

    private String browser;

    private String os;

    private String deviceType;

    private LocalDateTime loginTime;

    private Long userId;

    private boolean mainDevice;
}
