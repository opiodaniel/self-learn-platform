package com.ceres.project.services;

import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.Category;
import com.ceres.project.repositories.CategoryRepository;
import com.ceres.project.repositories.CourseRepository;
import com.ceres.project.repositories.SystemUserRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import com.ceres.project.utils.gcp_storage.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService extends BaseWebActionsService {

    private final CourseRepository courseRepository;
    private final FileServiceImpl fileService;
    private final CategoryRepository categoryRepository;
    private final SystemUserRepository systemUserRepository;

    // Create Course Category
    public OperationReturnObject createCategory(JSONObject request) {
        requiresAuth();
        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            String name = data.getString("name").trim();
            if (name.isEmpty()) {
                returnObject.setReturnCodeAndReturnMessage(400, "Category name cannot be empty");
                return returnObject;
            }
            // Check for duplicate name (case-insensitive)
            boolean exists = categoryRepository.existsByNameIgnoreCase(name);
            if (exists) {
                returnObject.setReturnCodeAndReturnMessage(409, "Category with the same name already exists");
                return returnObject;
            }
            Category category = new Category();
            category.setName(name);
            Category saved = categoryRepository.save(category);
            returnObject.setCodeAndMessageAndReturnObject(201, "Category created successfully", saved);
        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Failed to create category");
        }
        return returnObject;
    }

    // Get All Category //
    public OperationReturnObject getAllCategory(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            List<Category> categories = categoryRepository.findAll();
            returnObject.setCodeAndMessageAndReturnObject(200, "Category fetched successfully", categories);

        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Unable to fetch categories");
        }

        return returnObject;
    }



    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "createCategory" -> createCategory(request);
            case "getAllCategory" -> getAllCategory(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
