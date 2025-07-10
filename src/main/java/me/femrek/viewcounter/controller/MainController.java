package me.femrek.viewcounter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.GithubUserDTO;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import me.femrek.viewcounter.security.CustomOAuth2User;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {
    private final SubscriptionService subscriptionService;

    @Value(value = "${requestTrackerUrl:#{null}}")
    private String requestTrackerUrl;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null && user.getGithubUser() != null) {
            model.addAttribute("user", new GithubUserDTO(user.getGithubUser()));
            subscriptionService.mountSubscriptionsOnlyExist(user.getGithubUser(), true);
            model.addAttribute("subscriptions",
                    SubscriptionDTO.from(user.getGithubUser().getSubscriptions()));
        }

        if (requestTrackerUrl != null) {
            model.addAttribute("requestTrackerUrl", requestTrackerUrl);
        }

        return "index";
    }
}
