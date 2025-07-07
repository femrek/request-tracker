package me.femrek.viewcounter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.RequestDTO;
import me.femrek.viewcounter.dto.ResponseSubscriptionMinimal;
import me.femrek.viewcounter.error.AppSubscriptionNotFound;
import me.femrek.viewcounter.model.AppRequest;
import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.repository.RequestRepository;
import me.femrek.viewcounter.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RequestRepository requestRepository;

    public ResponseSubscriptionMinimal performRequest(UUID subscriptionId, RequestDTO requestDTO) {
        log.trace("Performing request for subscription: {}", requestDTO);
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null.");
        }

        AppSubscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new AppSubscriptionNotFound(
                        "Subscription with ID " + subscriptionId + " not found."));

        AppRequest request = AppRequest.builder()
                .subscription(subscription)
                .userAgent(requestDTO.getUserAgent())
                .ipAddress(requestDTO.getIpAddress())
                .build();
        requestRepository.saveAndFlush(request);

        AppSubscription resultSubscription = subscriptionRepository.findById(subscriptionId).orElseThrow(
                () -> new AppSubscriptionNotFound(
                        "Subscription with ID " + subscriptionId + " not found after saving request.")
        );

        log.trace("old: {}, new {}.Request performed for subscription: {}",
                subscription.getCounter(), resultSubscription.getCounter(), subscriptionId);

        return ResponseSubscriptionMinimal.builder()
                .count(resultSubscription.getCounter())
                .build();
    }
}
