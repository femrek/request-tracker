package me.femrek.viewcounter.controller;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.security.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MainController {
    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null) {
            model.addAttribute("user", user.getGithubUser());
        }
        return "index";
    }
}
