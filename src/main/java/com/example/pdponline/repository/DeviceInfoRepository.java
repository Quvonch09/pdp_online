package com.example.pdponline.repository;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, Long> {
    List<DeviceInfo> findAllByUserOrderByLoginTimeAsc(User user);
    DeviceInfo findByUserAndDeviceAgent(User user, String deviceAgent);
    boolean existsByUserAndDeviceAgent(User user, String deviceAgent);
}
