package com.mps.user.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {
    String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable);
}
