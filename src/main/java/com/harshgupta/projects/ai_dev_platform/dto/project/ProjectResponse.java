package com.harshgupta.projects.ai_dev_platform.dto.project;

import com.harshgupta.projects.ai_dev_platform.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
