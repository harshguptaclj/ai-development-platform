package com.harshgupta.projects.ai_dev_platform.dto.member;

import com.harshgupta.projects.ai_dev_platform.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
       @NotNull ProjectRole role) {
}
