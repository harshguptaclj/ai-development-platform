package com.harshgupta.projects.ai_dev_platform.dto.auth;

public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
