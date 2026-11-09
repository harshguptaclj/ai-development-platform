package com.harshgupta.projects.ai_dev_platform.dto.chat;

import com.harshgupta.projects.ai_dev_platform.entity.*;
import com.harshgupta.projects.ai_dev_platform.enums.MessageRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

public record ChatResponse(

        Long id,

        ChatSession chatSession,

        MessageRole role, // USER, ASSISTANT

        List<ChatEvent> chatEvents,
        String content,

        Integer tokensUsed,

        Instant createdAt
) {
}
