package com.harshgupta.projects.ai_dev_platform.mapper;

import com.harshgupta.projects.ai_dev_platform.dto.auth.SignUpRequest;
import com.harshgupta.projects.ai_dev_platform.dto.auth.UserProfileResponse;
import com.harshgupta.projects.ai_dev_platform.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignUpRequest signUpRequest);

    UserProfileResponse toUserProfileResponse(User user);
}
