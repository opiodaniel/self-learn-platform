package com.ceres.project.models.jpa_helpers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseCategory {
    LANGUAGE("language"),
    FRAMEWORK("framework"),
    DATABASE("database"),
    DEVOPS("devops"),
    LIBRARY("library");
    private final String niceName;
}

