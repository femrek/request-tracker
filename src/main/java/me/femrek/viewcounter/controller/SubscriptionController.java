package me.femrek.viewcounter.controller;

import lombok.RequiredArgsConstructor;
import me.femrek.viewcounter.dto.UpdateSubscription;
import me.femrek.viewcounter.security.CustomOAuth2User;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

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
}
