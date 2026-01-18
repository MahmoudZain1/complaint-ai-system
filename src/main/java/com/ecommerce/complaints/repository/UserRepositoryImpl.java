package com.ecommerce.complaints.repository;

import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl  implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {return jpaRepository.findById(id);}

    @Override
    public Optional<User> findByEmail(String email) {return jpaRepository.findByEmail(email);}

    @Override
    public boolean existsByEmail(String email) {return jpaRepository.existsByEmail(email);}

    @Override
    public void deleteById(Long id) {jpaRepository.deleteById(id);}

    @Override
    public List<User> findByRoleIn(List<UserRole> roles) {return jpaRepository.findByRoleIn(roles);}
    @Override
    public List<User> findByRole(UserRole role) {return jpaRepository.findByRole(role);}

    @Override
    public long count() {return jpaRepository.count();}
}
