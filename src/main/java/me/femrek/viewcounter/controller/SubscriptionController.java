package me.femrek.viewcounter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.femrek.viewcounter.dto.RequestDTO;
import me.femrek.viewcounter.dto.ResponseSubscriptionMinimal;
import me.femrek.viewcounter.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
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
}
