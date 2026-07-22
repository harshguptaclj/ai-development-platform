package com.harshgupta.projects.ai_dev_platform.dto.member;

import com.harshgupta.projects.ai_dev_platform.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
