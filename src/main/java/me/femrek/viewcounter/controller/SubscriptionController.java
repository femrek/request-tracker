package me.femrek.viewcounter.controller;

import lombok.RequiredArgsConstructor;
import me.femrek.viewcounter.dto.UpdateSubscription;
import me.femrek.viewcounter.security.CustomOAuth2User;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/deleted")
    public String deletedSubscriptions(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null && user.getGithubUser() != null) {
            model.addAttribute("user", user.getGithubUser());
            subscriptionService.mountSubscriptionsOnlyExist(user.getGithubUser());
            model.addAttribute("deletedSubscriptions",
                    subscriptionService.getSubscriptionsOnlyDeleted(user.getGithubUser()));
        }
        return "deleted-subscriptions";
    }

    @PostMapping
    public String createSubscription(@AuthenticationPrincipal CustomOAuth2User user) {
        subscriptionService.createSubscription(user);
        return "redirect:/";
    }

    @PostMapping("/{uuid}")
    public String updateSubscription(@PathVariable(name = "uuid") UUID uuid,
                                     @ModelAttribute UpdateSubscription updateSubscription) {
        subscriptionService.updateSubscription(uuid, updateSubscription);
        return "redirect:/";
    }

    @PostMapping("/{uuid}/delete")
    public String deleteSubscription(@AuthenticationPrincipal CustomOAuth2User user,
                                     @PathVariable(name = "uuid") UUID uuid) {
        subscriptionService.deleteSubscription(user.getGithubUser(), uuid);
        return "redirect:/";
    }

    @PostMapping("/{uuid}/restore")
    public String restoreSubscription(@AuthenticationPrincipal CustomOAuth2User user,
                                      @PathVariable(name = "uuid") UUID uuid) {
        subscriptionService.restoreById(user.getGithubUser(), uuid);
        return "redirect:/subscriptions/deleted";
    }
}
