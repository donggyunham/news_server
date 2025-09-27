package com.example.hello.news.repository;

import com.example.hello.news.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
// entity, key value type
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
