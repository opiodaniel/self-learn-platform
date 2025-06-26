package com.ceres.project.services.auth;

import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.config.ApplicationConf;
import com.ceres.project.config.JwtUtility;
import com.ceres.project.models.database.SystemRoleModel;
import com.ceres.project.models.database.SystemRolePermissionAssignmentModel;
import com.ceres.project.models.database.SystemUserModel;
import com.ceres.project.models.jpa_helpers.enums.AppDomains;
import com.ceres.project.models.jpa_helpers.enums.Genders;
import com.ceres.project.models.user_registerform.UserForm;
import com.ceres.project.permissions.DefaultRoles;
import com.ceres.project.repositories.RoleRepository;
import com.ceres.project.repositories.SystemRolePermissionRepository;
import com.ceres.project.repositories.SystemUserRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.*;
import com.ceres.project.utils.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthService extends BaseWebActionsService {
    private final AuthenticationManager authenticationManager;
    private final ApplicationConf userDetailService;
    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;
    private final SystemUserRepository systemUserRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final SystemRolePermissionRepository  systemRolePermissionRepository;


    public OperationReturnObject login(JSONObject request) {
        OperationReturnObject res = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            System.out.println(data.toString());
            requires(List.of("email", "password"), data);
            String username = data.getString("email");
            System.out.println(username);
            String password = data.getString("password");
            System.out.println(password);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            final SystemUserModel userDetails = userDetailService.loadUserByUsername(username);

            final String accessToken = jwtUtility.generateToken(userDetails);

            var userRole = roleRepository.findByRoleCode(userDetails.getRoleCode()).orElseThrow(() -> new NotFound404("Could not resolve user's role."));
            var permissions = systemRolePermissionRepository.findAllByRoleCode(userRole.getRoleCode()).stream()
                    .map(SystemRolePermissionAssignmentModel::getPermissionCode).toList();
//
            AppDomains userRoleDomain = userRole.getRoleDomain();

            JSONObject extra = new JSONObject();

            Map<String, Object> response = new HashMap<>();
            response.put("id", userDetails.getId());
            //response.put("fullName", userDetails.getFull_name());
            response.put("email", userDetails.getEmail());
            response.put("isActive", userDetails.getIsActive());
            response.put("role", userRole.getRoleCode());
            response.put("domain", userRoleDomain);
            //response.put("avatar", userDetails.getAvatar());
            response.put("servspaceID", userDetails.getServpaceId());

            response.put("access_token", accessToken);
            response.put("permissions", permissions);

            res.setReturnCodeAndReturnMessage(200, "Welcome back " + userDetails.getFirstName() + " " + userDetails.getLastName());
            res.setReturnObject(response);

            return res;
        } catch (Exception e) {
            res.setReturnCodeAndReturnMessage(403, e.getMessage());
            return res;
        }
    }

    @Transactional
    public SystemUserModel saveUser(UserForm request) throws NotAcceptable406 {

        try {
            Optional<SystemUserModel> dupe = systemUserRepository.findByEmail(request.getEmail());
            if (dupe.isPresent()) {
                throw new NotAcceptable406("Account linked to this email already exists.");
            }

            var full_name = request.getFirst_name()+" "+ request.getLast_name();
            SystemUserModel user = new SystemUserModel();
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirst_name());
            user.setLastName(request.getLast_name());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIsActive(true);
            user.setGenderPronoun(request.getGender_pronoun());
//            user.setRoleCode("ADMINISTRATOR");
            user.setRoleCode("PROGRAMMER");
            user.setUsername(full_name);

            Optional<SystemRoleModel> roleCheck = roleRepository.findByRoleCode(request.getRole());

            if (roleCheck.isEmpty()) {
                throw new NotAcceptable406("Unsupported user role");
            }
            SystemRoleModel role = roleCheck.get();
            user.setRoleId(role.getId());
            user.setRoleCode(role.getRoleCode());

            user.setServpaceId(Helpers.generateServspaceId("SD"));
            //user.setProfileComplete(false);
            SystemUserModel savedUser = systemUserRepository.save(user);

            return savedUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private OperationReturnObject register(JSONObject request) {
        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            requires(List.of("firstName", "lastName", "email", "password", "gender"), data);

            var full_name = data.getString("firstName")+" "+data.getString("lastName");
            UserForm userForm = new UserForm();
            userForm.setFirst_name(data.getString("firstName").trim());
            userForm.setLast_name(data.getString("lastName").trim());
            userForm.setEmail(data.getString("email").trim());
//            userForm.setRole("ADMINISTRATOR");
            userForm.setRole("PROGRAMMER");
            userForm.setUsername(full_name);
            userForm.setPassword(data.getString("password"));
            if (!EnumUtils.isValidEnum(Genders.class, data.getString("gender"))) {
                throw new NotAcceptable406("Invalid Gender");
            }
            userForm.setGender_pronoun(Genders.valueOf(data.getString("gender")));
            SystemUserModel savedUser = saveUser(userForm);

            returnObject.setCodeAndMessageAndReturnObject(200, "Account Created successfully", savedUser);
            return returnObject;
        } catch (NotAcceptable406 ex) {
            returnObject.setReturnCodeAndReturnMessage(406, "Failed to create Account");
            return returnObject;
        }
    }


    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action){
            case "login" -> login(request);
            case "register" -> register(request);
            default -> throw new IllegalArgumentException("Action " + action + " not known in this context");
        };
    }
}
