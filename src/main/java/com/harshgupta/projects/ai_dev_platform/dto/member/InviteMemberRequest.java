package com.harshgupta.projects.ai_dev_platform.dto.member;

import com.harshgupta.projects.ai_dev_platform.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email
        @NotBlank
        String username,

        @NotNull
        ProjectRole role
) {
}
