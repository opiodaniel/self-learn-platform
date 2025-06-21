package com.ceres.project.models.user_registerform;

import com.ceres.project.models.jpa_helpers.enums.Genders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String first_name, last_name, email, username, role, password, confirm_password;
    private Genders gender_pronoun;
}
