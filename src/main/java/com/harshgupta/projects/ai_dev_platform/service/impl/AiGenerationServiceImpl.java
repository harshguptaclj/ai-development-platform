package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.entity.*;
import com.harshgupta.projects.ai_dev_platform.enums.ChatEventType;
import com.harshgupta.projects.ai_dev_platform.enums.MessageRole;
import com.harshgupta.projects.ai_dev_platform.error.ResourceNotFoundException;
import com.harshgupta.projects.ai_dev_platform.llm.LlmResponseParser;
import com.harshgupta.projects.ai_dev_platform.llm.PromptUtils;
import com.harshgupta.projects.ai_dev_platform.llm.advisors.FileTreeContextAdvisor;
import com.harshgupta.projects.ai_dev_platform.llm.tools.CodeGenerationTools;
import com.harshgupta.projects.ai_dev_platform.repository.*;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.service.AiGenerationService;
import com.harshgupta.projects.ai_dev_platform.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    private final ProjectFileService projectFileService;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final LlmResponseParser llmResponseParser;
    private final ChatSessionRepository chatSessionRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository chatEventRepository;

    @Override
   @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String message, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = createChatSessionIfNotExists(projectId,userId);

        Map<String,Object> advisorParams = Map.of(
            "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();

        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);

        AtomicReference<Long> startTime = new AtomicReference<>( System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(message)
                .tools(codeGenerationTools)
                .advisors( advisorSpec -> {
                    advisorSpec.params(advisorParams);
                    advisorSpec.advisors(fileTreeContextAdvisor);
                })
                .stream()
                .chatResponse()
                .doOnNext( response -> {
                    String content = response.getResult().getOutput().getText();

                    if(content != null && !content.isEmpty() && endTime.get() == 0){
                        endTime.set(System.currentTimeMillis());
                    }
                    if (content != null) {
                        fullResponseBuffer.append(content);
                    }
                })
                .doOnComplete(()-> {
                    Schedulers.boundedElastic().schedule(() -> {
                        long duration  = (endTime.get() - startTime.get())/1000;
                        finalizeChat(message, chatSession, fullResponseBuffer.toString(), projectId, duration);
                    });
                })
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException e) {
                        log.error("Error during streaming for projectId {}: {} {}", projectId, e.getStatusCode(), e.getResponseBodyAsString());
                    } else {
                        log.error("Error during streaming for projectId {}", projectId, error);
                    }
                })
                .mapNotNull(response ->  response.getResult().getOutput().getText());

    }

    private void finalizeChat(String userMessage, ChatSession chatSession, String fullText, Long projectId, Long duration) {
        //Save the user message
        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .role(MessageRole.USER)
                        .content(userMessage)
                        .build()
        );

        ChatMessage assistantChatMessage = ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .chatSession(chatSession)
                .content("Assistant Message Here...")
                .build();

        assistantChatMessage = chatMessageRepository.save(assistantChatMessage);

        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistantChatMessage);
        chatEventList.add(0, ChatEvent.builder()
                        .type(ChatEventType.THOUGHT)
                        .chatMessage(assistantChatMessage)
                        .content("Thought for " + duration + " seconds.")
                        .sequenceOrder(0)
                .build());

        chatEventList.stream()
                .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
                .forEach(e -> {projectFileService.saveFile(projectId, e.getFilePath(), e.getContent());});

        chatEventRepository.saveAll(chatEventList);
    }


    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession == null){
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(()-> new ResourceNotFoundException("Project", projectId.toString()));

            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new ResourceNotFoundException("User", userId.toString()));

            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();

            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}
