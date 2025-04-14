package com.example.pdponline.repository;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, Long> {
    List<DeviceInfo> findAllByUserOrderByLoginTimeAsc(User user);
    boolean existsByUserAndDeviceAgent(User user, String deviceAgent);
}
