package com.example.pdponline.repository;

import com.example.pdponline.entity.Category;
import com.example.pdponline.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

    List<Category> findByActive(boolean active);

    List<Category> findByCategoryType(CategoryType categoryType);
}
