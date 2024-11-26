package com.mps.user.services.impl;

import com.mps.user.enums.RoleType;
import com.mps.user.models.RoleModel;
import com.mps.user.repositories.RoleRepository;
import com.mps.user.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<RoleModel> findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType);
    }
}
