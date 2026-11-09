package com.harshgupta.projects.ai_dev_platform.repository;

import com.harshgupta.projects.ai_dev_platform.entity.ChatSession;
import com.harshgupta.projects.ai_dev_platform.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
