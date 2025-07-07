package me.femrek.viewcounter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.RequestDTO;
import me.femrek.viewcounter.dto.ResponseSubscriptionMinimal;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import me.femrek.viewcounter.dto.UpdateSubscription;
import me.femrek.viewcounter.security.CustomOAuth2User;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscriptions")
@Log4j2
public class SubscriptionRestController {
    private final SubscriptionService service;

    @GetMapping("/request/{uuid}")
    public ResponseEntity<ResponseSubscriptionMinimal> performRequest(@PathVariable(name = "uuid") UUID uuid,
                                                                      HttpServletRequest request) {
        RequestDTO requestDTO = RequestDTO.builder()
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .build();
        return ResponseEntity.ok(service.performRequest(uuid, requestDTO));
    }

    @PostMapping("/")
    public ResponseEntity<SubscriptionDTO> createSubscription(@AuthenticationPrincipal CustomOAuth2User user) {
        SubscriptionDTO subscription = service.createSubscription(user);
        return ResponseEntity.ok(subscription);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable(name = "uuid") UUID uuid,
                                                              @RequestBody UpdateSubscription updateSubscription) {
        SubscriptionDTO updatedSubscription = service.updateSubscription(uuid, updateSubscription);
        return ResponseEntity.ok(updatedSubscription);
    }
}
