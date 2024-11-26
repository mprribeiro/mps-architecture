package com.mps.user.services;

import com.mps.user.enums.RoleType;
import com.mps.user.models.RoleModel;

import java.util.Optional;

public interface RoleService {
    Optional<RoleModel> findByRoleName(RoleType roleType);
}
