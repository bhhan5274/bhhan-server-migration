package io.github.bhhan.server.web.dashboard.project;

import io.github.bhhan.server.service.project.ProjectService;
import io.github.bhhan.server.service.project.dto.ProjectDto;
import io.github.bhhan.server.service.skill.SkillService;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final SkillService skillService;
    private final ProjectService projectService;

    @Value("${user.name}")
    private String username;

    @Value("${user.password}")
    private String password;

    @ModelAttribute("currentMenu")
    public String currentMenu(){
        return "project";
    }

    @ModelAttribute("basicAuthToken")
    public String basicAuthToken(){
        return getBasicAuthToken(username, password);
    }

    @GetMapping("/board")
    public String projectBoard(Authentication authentication, Model model){
        model.addAttribute("admin", Objects.nonNull(authentication) ? "admin" : "anonymous");
        return "project/projectDashboard";
    }

    @GetMapping("/addForm")
    public String projectAddForm(Model model){
        model.addAttribute("skills", skillService.getSkills());
        return "project/addForm";
    }

    @GetMapping("/updateForm/{projectId}")
    public String projectUpdateForm(@PathVariable Long projectId, Model model){
        final List<SkillDto.SkillSelectedRes> allSkills = skillService.getSkills()
                .stream()
                .map(SkillDto.SkillSelectedRes::new)
                .collect(Collectors.toList());

        final ProjectDto.ProjectRes projectRes = projectService.getProject(projectId);

        projectRes.setImages(
                projectRes.getImages()
                        .stream()
                        .sorted(Comparator.comparingLong(ProjectDto.ImageRes::getId).reversed())
                        .collect(Collectors.toList())
        );

        projectRes.getSkills()
                .forEach(projectSkill -> {
                    for (SkillDto.SkillSelectedRes skill : allSkills) {
                        if(projectSkill.getId().equals(skill.getId())){
                            skill.select();
                            break;
                        }
                    }
                });

        model.addAttribute("skills", allSkills);
        model.addAttribute("project", projectRes);
        return "project/updateForm";
    }

    public static String getBasicAuthToken(String username, String password){
        return new String(Base64.encode(String.format("%s:%s", username, password).getBytes()));
    }
}
