package com.harshgupta.projects.ai_dev_platform.dto.chat;

import com.harshgupta.projects.ai_dev_platform.entity.ChatMessage;
import com.harshgupta.projects.ai_dev_platform.enums.ChatEventType;
import jakarta.persistence.*;

public record ChatEventResponse(
        Long id,
        ChatMessage chatmessage,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
