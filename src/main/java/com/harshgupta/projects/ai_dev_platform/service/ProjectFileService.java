package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.project.FileContentResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(long projectId);

    FileContentResponse getFileContent(long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
