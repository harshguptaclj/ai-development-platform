package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
