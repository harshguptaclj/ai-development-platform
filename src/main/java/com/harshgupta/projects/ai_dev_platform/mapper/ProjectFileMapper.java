package com.harshgupta.projects.ai_dev_platform.mapper;

import com.harshgupta.projects.ai_dev_platform.dto.project.FileNode;
import com.harshgupta.projects.ai_dev_platform.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFiles);
}
