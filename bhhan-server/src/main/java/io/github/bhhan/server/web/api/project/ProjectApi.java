package io.github.bhhan.server.web.api.project;

import io.github.bhhan.server.service.common.dto.Pageable;
import io.github.bhhan.server.service.project.ProjectService;
import io.github.bhhan.server.service.project.dto.ProjectDto;
import io.github.bhhan.server.service.project.exception.ProjectException;
import io.github.bhhan.server.web.api.common.error.ErrorCode;
import io.github.bhhan.server.web.api.common.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/projects")
public class ProjectApi {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto.ProjectRes addProject(@Valid ProjectDto.ProjectFormReq projectFormReq){
        return projectService.addProject(projectFormReq);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto.ProjectPagingRes getProjects(@Valid @ModelAttribute Pageable pageable){
        return projectService.getProjects(pageable.of());
    }

    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto.ProjectRes getProject(@PathVariable Long projectId){
        return projectService.getProject(projectId);
    }

    @GetMapping("/{projectId}/view")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto.ProjectViewRes getProjectView(@PathVariable Long projectId){
        return projectService.getProjectView(projectId);
    }


    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable Long projectId){
        projectService.removeProject(projectId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProjects(){
        projectService.removeProjects();
    }

    @PutMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto.ProjectRes updateProject(@PathVariable Long projectId, @Valid ProjectDto.ProjectFormReq projectUpdateFormReq){
        return projectService.updateProject(projectId, projectUpdateFormReq);
    }

    @ExceptionHandler(ProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleProjectException(ProjectException e){
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR)
                .setMessage(e.getMessage());
    }
}
