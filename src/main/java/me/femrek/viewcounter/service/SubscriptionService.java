package me.femrek.viewcounter.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.RequestDTO;
import me.femrek.viewcounter.dto.ResponseSubscriptionMinimal;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import me.femrek.viewcounter.dto.UpdateSubscription;
import me.femrek.viewcounter.error.AppSubscriptionNotFound;
import me.femrek.viewcounter.model.AppRequest;
import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.model.GithubUser;
import me.femrek.viewcounter.repository.RequestRepository;
import me.femrek.viewcounter.repository.SubscriptionRepository;
import me.femrek.viewcounter.security.CustomOAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;

    @Transactional
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
        entityManager.refresh(subscription);

        log.trace("count {}. Request performed for subscription: {}",
                subscription.getCounter(), subscriptionId);

        return ResponseSubscriptionMinimal.builder()
                .count(subscription.getCounter())
                .build();
    }

    public SubscriptionDTO createSubscription(CustomOAuth2User user) {
        log.trace("Creating a new subscription");

        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to create a subscription.");
        }
        GithubUser githubUser = user.getGithubUser();
        if (githubUser == null) {
            throw new IllegalArgumentException("User not found: " + user.getName());
        }

        AppSubscription newSubscription = AppSubscription.builder()
                .name("-")
                .counter(0L)
                .createdBy(githubUser)
                .build();
        AppSubscription savedSubscription = subscriptionRepository.save(newSubscription);

        log.debug("Created new subscription with ID: {}", savedSubscription.getId());

        return new SubscriptionDTO(savedSubscription);
    }

    public SubscriptionDTO updateSubscription(UUID uuid, UpdateSubscription updateSubscription) {
        log.trace("Updating subscription with ID: {}", uuid);

        AppSubscription subscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppSubscriptionNotFound("Subscription with ID " + uuid + " not found."));

        if (updateSubscription.validate() != null) {
            throw new IllegalArgumentException("Invalid update data: " + updateSubscription.validate());
        }

        subscription.setName(updateSubscription.getName());
        AppSubscription updatedSubscription = subscriptionRepository.save(subscription);

        log.debug("Updated subscription with ID: {}", updatedSubscription.getId());
        return new SubscriptionDTO(updatedSubscription);
    }

    public void mountSubscriptions(GithubUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to mount subscriptions.");
        }
        log.trace("Mounting subscriptions for user: {}", user.getUsername());
        user.setSubscriptions(subscriptionRepository.findAllByCreatedBy(user));
    }
}
