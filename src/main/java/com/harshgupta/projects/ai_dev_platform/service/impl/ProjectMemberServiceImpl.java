package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.member.InviteMemberRequest;
import com.harshgupta.projects.ai_dev_platform.dto.member.MemberResponse;
import com.harshgupta.projects.ai_dev_platform.dto.member.UpdateMemberRoleRequest;
import com.harshgupta.projects.ai_dev_platform.entity.Project;
import com.harshgupta.projects.ai_dev_platform.entity.ProjectMember;
import com.harshgupta.projects.ai_dev_platform.entity.ProjectMemberId;
import com.harshgupta.projects.ai_dev_platform.entity.User;
import com.harshgupta.projects.ai_dev_platform.mapper.ProjectMemberMapper;
import com.harshgupta.projects.ai_dev_platform.repository.ProjectMemberRepository;
import com.harshgupta.projects.ai_dev_platform.repository.ProjectRepository;
import com.harshgupta.projects.ai_dev_platform.repository.UserRepository;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true , level = AccessLevel.PRIVATE)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;
    AuthUtil authUtil;

    @Override
    @PreAuthorize("@security.getCanViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId) {

        Project project = getAccessibleProjectById(projectId);

        return projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();

    }

    @Override
    @PreAuthorize("@security.getCanManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {

        Long userId = authUtil.getCurrentUserId();

        Project project = getAccessibleProjectById(projectId);

        User invitee = (User) userRepository.findByUsername(request.username()).orElseThrow();

        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Not allowed to invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,userId);

        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Project member already invited");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    @PreAuthorize("@security.getCanManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request) {

        Project project = getAccessibleProjectById(projectId);


        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();

        projectMember.setProjectRole(request.role());

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);

    }

    @Override
    @PreAuthorize("@security.getCanManageMembers(#projectId)")
    public void deleteProjectMember(Long projectId, Long memberId) {

        Project project = getAccessibleProjectById(projectId);

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);

        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Project member does not exist");
        }

        projectMemberRepository.deleteById(projectMemberId);

    }

    //Internal Methods

    private Project getAccessibleProjectById(Long id){
        Long userId = authUtil.getCurrentUserId();
        return  projectRepository.findAllAccessibleProjectById(userId,id).orElseThrow();
    }
}
