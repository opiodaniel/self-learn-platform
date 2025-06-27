package com.ceres.project.models.jpa_helpers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genders {
    male("Male"),
    female("Female"),
    rather_not_say("Rather Not Say");
    public final String niceName;
}
