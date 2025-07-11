package me.femrek.viewcounter.controller;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.GithubUserDTO;
import me.femrek.viewcounter.dto.RequestDTO;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

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
        GithubUserDTO user = null;
        //noinspection ConstantValue
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/subscriptions")
    public String subscriptions(Model model) {
        log.info("Debug endpoint accessed for subscriptions");

        model.addAttribute("subscriptions", List.of(
                _generateSubscription(new Timestamp(1741713236000L)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 24)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 12)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 6)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 2)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 30)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 15)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 5)),
                _generateSubscription(new Timestamp(System.currentTimeMillis() - 1000 * 2))
        ));
        model.addAttribute("user", GithubUserDTO.builder()
                .username("femrek")
                .fullName("Faruk Emre")
                .avatarUrl("https://avatars.githubusercontent.com/u/29581978?v=4")
                .profileUrl("https://github.com/femrek")
                .build());
        return "index";
    }

    private SubscriptionDTO _generateSubscription(Timestamp lastRequestAt) {
        RequestDTO requestDTO = RequestDTO.builder()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/123 (KHTML, like Gecko) Chrome/58.123 Safari/123.3")
                .ipAddress("123.123.123.123")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return SubscriptionDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Subscription")
                .count(42L)
                .lastRequestAt(lastRequestAt)
                .requests(List.of(requestDTO, requestDTO, requestDTO, requestDTO,
                        requestDTO, requestDTO, requestDTO, requestDTO, requestDTO))
                .build();
    }
}
