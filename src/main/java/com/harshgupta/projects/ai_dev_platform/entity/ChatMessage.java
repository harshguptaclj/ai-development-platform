package com.harshgupta.projects.ai_dev_platform.entity;

import com.harshgupta.projects.ai_dev_platform.enums.MessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    Long id;
    ChatSession chatSession;

    String content;

    MessageRole role;

    String toolCalls;

    Integer tokensUsed;

    Instant createdAt;
}
