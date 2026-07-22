package com.harshgupta.projects.ai_dev_platform.mapper;

import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectSummaryResponse;
import com.harshgupta.projects.ai_dev_platform.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse  toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);
}
