package com.example.pdponline.repository;

import com.example.pdponline.entity.Category;
import com.example.pdponline.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

    @Query(value = """
        SELECT * FROM category c
        WHERE (:active IS NULL OR c.active = :active)
          AND (:type IS NULL OR c.category_type = :type)
    """, nativeQuery = true)
    List<Category> filterCategories(
            @Param("active") Boolean active,
            @Param("type") String type
    );
}
