package com.e_learning.controller;

import com.e_learning.model.Category;
import com.e_learning.service.CategoryService;
import com.e_learning.service.ResponseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ResponseService responseService;

    public CategoryController(CategoryService categoryService, ResponseService responseService) {
        this.categoryService = categoryService;
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@Valid @RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return responseService.createSuccessResponse(201, created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return responseService.createSuccessResponse(200, categories, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updatedCategory) {

        Category category = categoryService.updateCategory(id, updatedCategory);
        return responseService.createSuccessResponse(200, category, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return responseService.createSuccessResponse(200, "Category deleted successfully", HttpStatus.OK);
    }

}

