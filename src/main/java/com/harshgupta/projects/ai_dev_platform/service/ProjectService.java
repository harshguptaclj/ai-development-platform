package com.harshgupta.projects.ai_dev_platform.service;


import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectRequest;
import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectSummaryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {

     ProjectResponse getUserProjectById(Long id);

     List<ProjectSummaryResponse> getUserProjects();

     ProjectResponse createProject(ProjectRequest request);

     ProjectResponse updateProject(Long id, ProjectRequest request);

     void softDelete(Long id);
}
