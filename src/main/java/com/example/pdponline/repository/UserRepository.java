package com.example.pdponline.repository;

import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.phoneNumber = ?1 and u.enabled = true")
    User getUserAndEnabledTrue(String phone);

    boolean existsByPhoneNumberAndEnabledTrue(String phone);


    Optional<User> findByPhoneNumber(String phoneNumber);


    Optional<User> findByPhoneNumberAndEnabledTrue(String phoneNumber);

    Optional<User> findByIdAndRole(Long id, Role role);

    List<User> findAllByRoleAndEnabledTrue(Role role);

    long countAllByRoleAndEnabledTrue(Role role);



    @Query(value = """
        select u.* from users u where
            (:keyword IS NULL OR (LOWER(u.first_name) LIKE LOWER(CONCAT('%', :keyword, '%')) or
            LOWER(u.last_name) LIKE LOWER(CONCAT('%', :keyword, '%')))) and
            (:phoneNumber IS NULL OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))) and
            (:role IS NULL OR u.role = :role)
        order by u.created_at desc
        """,
            countQuery = """
        select count(*) from users u where
            (:keyword IS NULL OR (LOWER(u.first_name) LIKE LOWER(CONCAT('%', :keyword, '%')) or
            LOWER(u.last_name) LIKE LOWER(CONCAT('%', :keyword, '%')))) and
            (:phoneNumber IS NULL OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))) and
            (:role IS NULL OR u.role = :role)
        """,
            nativeQuery = true)
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("role") String role, Pageable pageable);

    @Query("select u from User u where (u.firstName like :fullName or u.phoneNumber = :phone) and u.role = :roleName")
    List<User> searchForChat(@Param("fullName") String fullName,
                             @Param("phone") String phone,
                             @Param("roleName") String roleName);

}
