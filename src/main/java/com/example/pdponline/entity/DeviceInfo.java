package com.example.pdponline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceAgent;
    private String browser;
    private String os;
    private String deviceType;

    private LocalDateTime loginTime;

    @ManyToOne
    private User user;

    private boolean main;


    public DeviceInfo(String deviceAgent, String browser, String os, String deviceType, LocalDateTime loginTime, User user) {
        this.deviceAgent = deviceAgent;
        this.browser = browser;
        this.os = os;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
        this.user = user;
    }
}
