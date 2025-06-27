package com.ceres.project.repositories;


import com.ceres.project.models.database.Category;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JetRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);

}


