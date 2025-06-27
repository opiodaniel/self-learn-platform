package com.ceres.project.repositories;

import com.ceres.project.models.database.SystemRoleModel;
import com.ceres.project.models.jpa_helpers.enums.AppDomains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<SystemRoleModel, Long> {
    Optional<SystemRoleModel> findByRoleName(String name);
    List<SystemRoleModel> findAllByRoleDomain(AppDomains roleDomain);
    Optional<SystemRoleModel> findByRoleCode(String code);
    Optional<SystemRoleModel> findRoleModelByRoleName(String name);
    SystemRoleModel findRoleModelById(Long id);
}
