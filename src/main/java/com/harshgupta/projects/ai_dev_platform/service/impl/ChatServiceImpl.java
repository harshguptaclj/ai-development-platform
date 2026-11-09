package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.chat.ChatResponse;
import com.harshgupta.projects.ai_dev_platform.entity.ChatMessage;
import com.harshgupta.projects.ai_dev_platform.entity.ChatSession;
import com.harshgupta.projects.ai_dev_platform.entity.ChatSessionId;
import com.harshgupta.projects.ai_dev_platform.mapper.ChatMapper;
import com.harshgupta.projects.ai_dev_platform.repository.ChatMessageRepository;
import com.harshgupta.projects.ai_dev_platform.repository.ChatSessionRepository;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId,userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessages(chatMessageList);
    }
}
