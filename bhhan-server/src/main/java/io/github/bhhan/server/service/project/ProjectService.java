package io.github.bhhan.server.service.project;

import io.github.bhhan.server.domain.common.Period;
import io.github.bhhan.server.domain.project.Image;
import io.github.bhhan.server.domain.project.Project;
import io.github.bhhan.server.domain.project.ProjectRepository;
import io.github.bhhan.server.domain.skill.Skill;
import io.github.bhhan.server.domain.skill.SkillRepository;
import io.github.bhhan.server.service.project.dto.ProjectDto;
import io.github.bhhan.server.service.project.exception.ProjectException;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import io.github.bhhan.storage.api.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final StorageService storageService;

    @CacheEvict(value = "paging", allEntries = true)
    public ProjectDto.ProjectRes addProject(ProjectDto.ProjectFormReq projectFormReq) {
        try {
            List<String> images = storageService.storeAll(projectFormReq.getFiles());
            Project project = projectFormReqToProject(projectFormReq, images);
            Project savedProject = projectRepository.save(project);

            return projectToProjectRes(savedProject);

        } catch (Exception e) {
            throw new ProjectException("프로젝트 추가에 실패했습니다: " + e.getMessage());
        }
    }

    @Cacheable(value = "project", key = "#projectId")
    public ProjectDto.ProjectRes getProject(Long projectId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(IllegalArgumentException::new);

            return projectToProjectRes(project);

        } catch (Exception e) {
            throw new ProjectException("단일 프로젝트 불러오기에 실패했습니다.");
        }
    }

    @Cacheable(value = "paging", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public ProjectDto.ProjectPagingRes getProjects(Pageable pageable) {
        try {
            Page<Project> projects = projectRepository.findAll(pageable);

            return ProjectDto.ProjectPagingRes.builder()
                    .totalElements(projects.getTotalElements())
                    .totalPages(projects.getTotalPages())
                    .size(projects.getSize())
                    .number(projects.getNumber())
                    .last(projects.isLast())
                    .first(projects.isFirst())
                    .empty(projects.isEmpty())
                    .content(projects.getContent()
                            .stream()
                            .map(this::projectToProjectRes)
                            .collect(Collectors.toList()))
                    .build();

        } catch (Exception e) {
            throw new ProjectException("페이징 프로젝트 불러오기에 실패했습니다.");
        }
    }

    //Non Caching
    public ProjectDto.ProjectViewRes getProjectView(Long projectId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(IllegalArgumentException::new);

            return ProjectDto.ProjectViewRes.builder()
                    .id(project.getId())
                    .description(project.getDescription())
                    .summary(project.getSummary())
                    .member(project.getMember())
                    .title(project.getTitle())
                    .startDate(project.getPeriod().getStartDate())
                    .endDate(project.getPeriod().getEndDate())
                    .images(projectToImageRes(project))
                    .skills(projectToSkillRes(project))
                    .prevId(getPrevProjectId(projectId))
                    .nextId(getNextProjectId(projectId))
                    .build();

        } catch (Exception e) {
            throw new ProjectException("프로젝트 불러오기에 실패했습니다.");
        }
    }

    @Caching(evict = {@CacheEvict(value = "paging", allEntries = true)},
            put = {@CachePut(value = "project", key = "#projectId")})
    public ProjectDto.ProjectRes updateProject(Long projectId, ProjectDto.ProjectFormReq projectUpdateFormReq) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(IllegalAccessError::new);

            List<String> files = storageService.storeAll(projectUpdateFormReq.getFiles());
            updateProjectFromProjectUpdateFormReqAndFiles(project, projectUpdateFormReq, files);

            Project updatedProject = projectRepository.save(project);

            return projectToProjectRes(updatedProject);

        } catch (Exception e) {
            throw new ProjectException("프로젝트 업데이트에 실패했습니다.");
        }
    }

    @Caching(evict = {@CacheEvict(value = "project", key = "#projectId"), @CacheEvict(value = "paging", allEntries = true)})
    public void removeProject(Long projectId) {
        try{
            projectRepository.deleteById(projectId);
        }catch(Exception e){
            throw new ProjectException("프로젝트 삭제에 실패했습니다.");
        }
    }

    @Caching(evict = {@CacheEvict(value = "project", allEntries = true), @CacheEvict(value = "paging", allEntries = true)})
    public void removeProjects() {
        try{
            projectRepository.deleteAll();
        }catch(Exception e){
            throw new ProjectException("프로젝트 삭제에 실패했습니다.");
        }
    }

    private void updateProjectFromProjectUpdateFormReqAndFiles(Project project, ProjectDto.ProjectFormReq projectUpdateFormReq, List<String> files) {
        project.updateTitle(projectUpdateFormReq.getTitle());
        project.updateDescription(projectUpdateFormReq.getDescription());
        project.updateSummary(projectUpdateFormReq.getSummary());
        project.updateMember(projectUpdateFormReq.getMember());
        project.updatePeriod(Period.builder()
                .startDate(LocalDate.parse(projectUpdateFormReq.getTimeRange().getStartDate()))
                .endDate(LocalDate.parse(projectUpdateFormReq.getTimeRange().getEndDate()))
                .build());
        project.updateSkills(getProjectSkills(projectUpdateFormReq.getSkills()));
        project.updateImages(makeProjectImages(files));
    }

    private Project projectFormReqToProject(ProjectDto.ProjectFormReq projectFormReq, List<String> images) {
        return Project.builder()
                .title(projectFormReq.getTitle())
                .description(projectFormReq.getDescription())
                .summary(projectFormReq.getSummary())
                .member(projectFormReq.getMember())
                .period(Period.builder()
                        .startDate(LocalDate.parse(projectFormReq.getTimeRange().getStartDate()))
                        .endDate(LocalDate.parse(projectFormReq.getTimeRange().getEndDate()))
                        .build())
                .skills(getProjectSkills(projectFormReq.getSkills()))
                .images(makeProjectImages(images))
                .build();
    }

    private ProjectDto.ProjectRes projectToProjectRes(Project savedProject) {
        return ProjectDto.ProjectRes.builder()
                .id(savedProject.getId())
                .title(savedProject.getTitle())
                .member(savedProject.getMember())
                .startDate(savedProject.getPeriod().getStartDate())
                .endDate(savedProject.getPeriod().getEndDate())
                .description(savedProject.getDescription())
                .summary(savedProject.getSummary())
                .skills(projectToSkillRes(savedProject))
                .images(projectToImageRes(savedProject))
                .build();
    }

    private List<SkillDto.SkillRes> projectToSkillRes(Project savedProject) {
        return savedProject.getSkills()
                .stream()
                .map(skill -> SkillDto.SkillRes.builder()
                        .id(skill.getId())
                        .use(skill.getUse().name())
                        .type(skill.getType().name())
                        .description(skill.getDescription())
                        .path(skill.getPath())
                        .name(skill.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProjectDto.ImageRes> projectToImageRes(Project savedProject) {
        return savedProject.getImages().stream()
                .map(image -> ProjectDto.ImageRes.builder()
                        .id(image.getId())
                        .path(image.getPath())
                        .build())
                .sorted(Comparator.comparingLong(ProjectDto.ImageRes::getId))
                .collect(Collectors.toList());
    }

    private List<Skill> getProjectSkills(List<Long> skillIds) {
        return skillIds.stream()
                .map(skillId -> skillRepository.findById(skillId).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
    }

    private List<Image> makeProjectImages(List<String> imagePaths) {
        return imagePaths.stream()
                .map(imagePath -> Image.builder()
                        .path(imagePath)
                        .build())
                .collect(Collectors.toList());
    }

    private Long getNextProjectId(Long projectId) {
        try {
            return projectRepository.findFirstByIdLessThanOrderByIdDesc(projectId)
                    .orElseThrow(IllegalArgumentException::new)
                    .getId();
        } catch (Exception e) {
            return 0L;
        }
    }

    private Long getPrevProjectId(Long projectId) {
        try {
            return projectRepository.findFirstByIdGreaterThanOrderByIdAsc(projectId)
                    .orElseThrow(IllegalArgumentException::new)
                    .getId();
        } catch (Exception e) {
            return 0L;
        }
    }
}
