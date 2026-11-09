package com.harshgupta.projects.ai_dev_platform.mapper;

import com.harshgupta.projects.ai_dev_platform.dto.chat.ChatResponse;
import com.harshgupta.projects.ai_dev_platform.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessages(List<ChatMessage> chatMessages);
}
