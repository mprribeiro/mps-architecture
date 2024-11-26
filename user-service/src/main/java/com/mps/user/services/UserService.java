package com.mps.user.services;

import com.mps.user.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel save(UserModel userModel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel saveUser(UserModel userModel);
    void deleteUser(UserModel userModel);
    UserModel updateUser(UserModel userModel);
    UserModel updatePassword(UserModel userModel);

}
