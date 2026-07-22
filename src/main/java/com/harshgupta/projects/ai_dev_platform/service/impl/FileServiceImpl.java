package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.project.FileContentResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.FileNode;
import com.harshgupta.projects.ai_dev_platform.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getFileTree(long projectId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(long projectId, String path) {
        return null;
    }
}
