package com.harshgupta.projects.ai_dev_platform.controller;

import com.harshgupta.projects.ai_dev_platform.dto.auth.AuthResponse;
import com.harshgupta.projects.ai_dev_platform.dto.auth.LoginRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.SignUpRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.UserProfileResponse;
import com.harshgupta.projects.ai_dev_platform.service.AuthService;
import com.harshgupta.projects.ai_dev_platform.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L;
        return ResponseEntity.ok(authService.getProfile(userId));

    }
}
