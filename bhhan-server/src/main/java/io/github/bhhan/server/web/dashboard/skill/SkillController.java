package io.github.bhhan.server.web.dashboard.skill;

import io.github.bhhan.server.service.skill.SkillService;
import io.github.bhhan.server.web.dashboard.project.ProjectController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @Value("${user.name}")
    private String username;

    @Value("${user.password}")
    private String password;

    @ModelAttribute("currentMenu")
    public String currentMenu() {
        return "skill";
    }

    @ModelAttribute("basicAuthToken")
    public String basicAuthToken(){
        return ProjectController.getBasicAuthToken(username, password);
    }

    @GetMapping("/board")
    public String projectBoard(Model model) {
        model.addAttribute("skills", skillService.getSkills());
        return "skill/skillDashboard";
    }
}
