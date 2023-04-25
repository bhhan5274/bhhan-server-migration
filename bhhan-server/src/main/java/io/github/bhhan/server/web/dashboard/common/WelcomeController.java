package io.github.bhhan.server.web.dashboard.common;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class WelcomeController {
    @ModelAttribute("currentMenu")
    public String currentMenu(){
        return "dashboard";
    }

    @GetMapping("/")
    public String index(@RequestParam(value="error", defaultValue = "false") boolean error,
                        Authentication authentication,
                        Model model){
        if(error){
            model.addAttribute("security_exception", "Login Failed!!!");
        }

        model.addAttribute("admin", Objects.nonNull(authentication) ? "admin" : "anonymous");

        return "project/projectDashboard";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
}
