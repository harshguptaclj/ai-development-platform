package com.harshgupta.projects.ai_dev_platform.controller;

import com.harshgupta.projects.ai_dev_platform.dto.chat.ChatRequest;
import com.harshgupta.projects.ai_dev_platform.dto.chat.ChatResponse;
import com.harshgupta.projects.ai_dev_platform.service.AiGenerationService;
import com.harshgupta.projects.ai_dev_platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    public final AiGenerationService aiGenerationService;
    public final ChatService chatService;

    @PostMapping(value= "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request) {

        return aiGenerationService.streamResponse(request.message(), request.projectId())
                .map(data -> ServerSentEvent.<String>builder()
                .data(data)
                .build());
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<List<ChatResponse>> getChatHistory(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(chatService.getProjectChatHistory(projectId));
    }
}
