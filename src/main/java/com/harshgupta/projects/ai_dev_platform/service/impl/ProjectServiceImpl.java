package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectRequest;
import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectResponse;
import com.harshgupta.projects.ai_dev_platform.dto.project.ProjectSummaryResponse;
import com.harshgupta.projects.ai_dev_platform.entity.Project;
import com.harshgupta.projects.ai_dev_platform.entity.ProjectMember;
import com.harshgupta.projects.ai_dev_platform.entity.ProjectMemberId;
import com.harshgupta.projects.ai_dev_platform.entity.User;
import com.harshgupta.projects.ai_dev_platform.enums.ProjectRole;
import com.harshgupta.projects.ai_dev_platform.error.ResourceNotFoundException;
import com.harshgupta.projects.ai_dev_platform.mapper.ProjectMapper;
import com.harshgupta.projects.ai_dev_platform.repository.ProjectMemberRepository;
import com.harshgupta.projects.ai_dev_platform.repository.ProjectRepository;
import com.harshgupta.projects.ai_dev_platform.repository.UserRepository;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.security.SecurityExpressions;
import com.harshgupta.projects.ai_dev_platform.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;
    private final SecurityExpressions security;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {

        Long userId = authUtil.getCurrentUserId();

//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User", userId.toString())
//        );
        User owner = userRepository.getReferenceById(userId);

        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();

        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(),owner.getId());

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .project(project)
                .user(owner)
                .invitedAt(Instant.now())
                .acceptedAt(Instant.now())
                .build();

        projectMemberRepository.save(projectMember);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {

        Long userId = authUtil.getCurrentUserId();

        return projectMapper.toListOfProjectSummaryResponse(projectRepository
                .findAllAccessibleByUser(userId));

    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getUserProjectById(Long projectId) {

        Project project = getAccessibleProjectById(projectId);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {

        Project project = getAccessibleProjectById(projectId);

        project.setName(request.name());
        project = projectRepository.save(project);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.getCanDeleteProject(#projectId)")
    public void softDelete(Long projectId) {

        Project project = getAccessibleProjectById(projectId);

        project.setDeletedAt(Instant.now());

        projectRepository.save(project);
    }

    //Internal Methods

    private Project getAccessibleProjectById(Long id){
        Long userId = authUtil.getCurrentUserId();
        return  projectRepository.findAllAccessibleProjectById(userId,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));
    }
}
