package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.auth.AuthResponse;
import com.harshgupta.projects.ai_dev_platform.dto.auth.LoginRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.SignUpRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.UserProfileResponse;
import com.harshgupta.projects.ai_dev_platform.entity.User;
import com.harshgupta.projects.ai_dev_platform.error.BadRequestException;
import com.harshgupta.projects.ai_dev_platform.mapper.UserMapper;
import com.harshgupta.projects.ai_dev_platform.repository.UserRepository;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level= AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignUpRequest request) {
        userRepository.findByUsername((request.username())).ifPresent(user ->{
                throw new BadRequestException("User already exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        String token = authUtil.generateAccessToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);
        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
