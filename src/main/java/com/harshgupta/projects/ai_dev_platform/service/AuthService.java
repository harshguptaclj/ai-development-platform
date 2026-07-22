package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.auth.AuthResponse;
import com.harshgupta.projects.ai_dev_platform.dto.auth.LoginRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.SignUpRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.UserProfileResponse;

public interface AuthService {
    AuthResponse signup(SignUpRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileResponse getProfile(Long userId);
}
