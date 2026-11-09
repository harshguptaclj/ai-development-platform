package com.harshgupta.projects.ai_dev_platform.repository;

import com.harshgupta.projects.ai_dev_platform.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
