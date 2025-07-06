package me.femrek.viewcounter.controller;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.GithubUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/debug")
@Log4j2
public class DebugController {
    @GetMapping("/logged_in")
    public String loggedIn(Model model) {
        log.info("Debug endpoint accessed, returning mock user data");
        model.addAttribute("user", GithubUserDTO.builder()
                .username("femrek")
                .fullName("Faruk Emre")
                .avatarUrl("https://avatars.githubusercontent.com/u/29581978?v=4")
                .profileUrl("https://github.com/femrek")
                .build());
        return "index";
    }

    @GetMapping("/logged_out")
    public String notLoggedIn(Model model) {
        log.info("Debug endpoint accessed without user, returning empty user data");
        GithubUserDTO user = (GithubUserDTO) null;
        //noinspection ConstantValue
        model.addAttribute("user", user);
        return "index";
    }
}
