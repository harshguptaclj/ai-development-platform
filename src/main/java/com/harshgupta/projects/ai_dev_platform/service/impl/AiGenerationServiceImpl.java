package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.llm.PromptUtils;
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
import java.util.Map;
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

    @Override
   @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String message, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        createChatSessionIfNotExists(projectId,userId);

        Map<String,Object> advisorParams = Map.of(
            "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(message)
                .advisors( advisorSpec -> {
                    advisorSpec.params(advisorParams);
                })
                .stream()
                .chatResponse()
                .doOnNext( response -> {
                    String content = response.getResult().getOutput().getText();
                    fullResponseBuffer  .append(content);
                })
                .doOnComplete(()-> {
                    Schedulers.boundedElastic().schedule(() -> {
                        parseAndSaveFiles(fullResponseBuffer.toString(), projectId);
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

    private void parseAndSaveFiles(String fullResponse, Long projectId) {
//        String dummy  = """
//                <message>I'm going to read file and generate the code</message>
//                <file path = "src/App.jsx">
//                       import App from './App.jsx'
//                       ......
//                </file>
//                <message>I'm going to read file and generate the code</message>
//                <file path = "src/App.jsx">
//                       import App from './App.jsx'
//                       ......
//                </file>
//                """;
        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponse);

        while (matcher.find()) {
            String filePath  = matcher.group(1);
            String fileContent =  matcher.group(2).trim();

            projectFileService.saveFile(projectId, filePath, fileContent);
        }

    }

    private void createChatSessionIfNotExists(Long projectId, Long userId) {
    }
}
