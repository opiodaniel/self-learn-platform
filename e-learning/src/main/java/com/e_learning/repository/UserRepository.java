package com.e_learning.repository;

import com.e_learning.model.Role;
import com.e_learning.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByUsername(@Param("username") String username);
    User findByRole(Role role);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByActive(int active);
}
