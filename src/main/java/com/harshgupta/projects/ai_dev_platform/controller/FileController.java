package com.harshgupta.projects.ai_dev_platform.controller;

import com.harshgupta.projects.ai_dev_platform.dto.project.FileContentResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.FileNode;
import com.harshgupta.projects.ai_dev_platform.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable long projectId) {
        return ResponseEntity.ok(fileService.getFileTree(projectId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable long projectId, @PathVariable String path) {
        return ResponseEntity.ok(fileService.getFileContent(projectId, path));
    }

}
