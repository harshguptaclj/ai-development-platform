package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.project.FileContentResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.FileNode;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(long projectId);

    FileContentResponse getFileContent(long projectId, String path);
}
