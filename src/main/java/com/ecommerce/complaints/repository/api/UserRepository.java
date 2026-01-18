package com.ecommerce.complaints.repository.api;

import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteById(Long id);
    List<User> findByRoleIn(List<UserRole> roles);
    List<User> findByRole(UserRole role);
    long count();
}
