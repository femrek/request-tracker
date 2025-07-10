package me.femrek.viewcounter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.RequestRequest;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import me.femrek.viewcounter.service.BadgeService;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscriptions")
@Log4j2
public class SubscriptionRestController {
    private final SubscriptionService subscriptionService;
    private final BadgeService badgeService;

    @GetMapping("/request/{uuid}")
    public ResponseEntity<SubscriptionDTO> performRequest(@PathVariable(name = "uuid") UUID uuid,
                                                          HttpServletRequest request) {
        RequestRequest requestRequest = RequestRequest.builder()
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(subscriptionService.getClientIp(request))
                .build();
        return ResponseEntity.ok(subscriptionService.performRequest(uuid, requestRequest));
    }

    @GetMapping("/request/badge/{uuid}")
    public ResponseEntity<String> performRequestWithBadge(
            @PathVariable(name = "uuid") UUID uuid,
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request) {
        RequestRequest requestRequest = RequestRequest.builder()
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(subscriptionService.getClientIp(request))
                .build();
        String svgContent = subscriptionService.performRequestWithBadge(uuid, requestRequest, queryParams);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/svg+xml")
                .body(svgContent);
    }

    @GetMapping("/no-request/{uuid}")
    public ResponseEntity<SubscriptionDTO> noRequest(@PathVariable(name = "uuid") UUID uuid) {
        return ResponseEntity.ok(subscriptionService.getSubscription(uuid));
    }

    @GetMapping("/no-request/badge/{uuid}")
    public ResponseEntity<String> noRequestWithBadge(
            @PathVariable(name = "uuid") UUID uuid,
            @RequestParam Map<String, String> queryParams) {
        String svgContent = badgeService.getViewsBadge(uuid.toString(), queryParams);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/svg+xml")
                .body(svgContent);
    }
}
